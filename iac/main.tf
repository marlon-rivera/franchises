terraform {
  required_version = ">= 1.5.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "~> 3.0"
    }
  }

  # Backend remoto con S3 para proteger el state y habilitar locking
  # Crear previamente el bucket S3
  backend "s3" {
    bucket       = "franchising-project-tfstate-nequi"
    key          = "terraform.tfstate"
    region       = "us-east-2"
    encrypt      = true
    use_lockfile = true
  }
}

provider "aws" {
  region = var.region
  profile = var.profile
}

module "vpc" {
  source = "./modules/vpc"
  vpc_cidr = var.vpc_cidr
  environment = var.environment
  public_subnet_cidrs = var.public_subnets
  private_subnet_cidrs = var.private_subnets
  availability_zones = var.availability_zones
  db_port = 3306
  container_port = 8080
}

module "alb" {
  source = "./modules/alb"
  vpc_id = module.vpc.vpc_id
  subnets = module.vpc.public_subnet_ids
  environment = var.environment
  alb_sg_id = module.vpc.alb_sg_id
}

module "ecr" {
  source = "./modules/ecr"
  repository_name = var.ecr_repository_name
  environment = var.environment
}

module "codebuild" {
  source              = "./modules/codebuild"
  environment         = var.environment
  region              = var.region
  ecr_repository_url  = module.ecr.repository_url
}

module "rds" {
  source = "./modules/rds"
  db_identifier = "nequi-challenge-db"
  db_name = "franchising_db"
  db_username = "admin"
  engine = "mysql"
  engine_version = "8.0"
  db_port = 3306
  subnet_ids = module.vpc.private_subnet_ids
  environment = var.environment
  rds_sg_id = module.vpc.rds_sg_id
}

module "ecs" {
  source              = "./modules/ecs"
  environment         = var.environment
  cluster_name        = "franchise-cluster"
  service_name        = "franchise-service"
  task_family         = "franchise-task"
  task_cpu            = "256"
  task_memory         = "512"
  container_name      = "franchise-api"
  container_image     = "${module.ecr.repository_url}:latest"
  container_port      = 8080
  subnets_ids         = module.vpc.private_subnet_ids
  security_groups     = [module.vpc.ecs_sg_id]
  target_group_arn    = module.alb.target_group_arn

  database_url        = "r2dbc:mysql://${module.rds.db_endpoint}/franchising_db"
  flyway_url          = "jdbc:mysql://${module.rds.db_endpoint}/franchising_db"
  database_user       = module.rds.db_username
  database_password_secret_arn = module.rds.db_password_secret_arn
  region              = var.region

  alb_sg_id = module.vpc.alb_sg_id
  vpc_id    = module.vpc.vpc_id
}

module "apigateway" {
  source = "./modules/apigateway"
  environment = var.environment
  alb_dns = module.alb.alb_dns_name
  region = var.region
}