variable "region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "vpc_cidr" {
  description = "CIDR block for the VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "public_subnets" {
  description = "List of public subnet CIDR blocks"
  type        = list(string)
  default     = ["10.0.1.0/24", "10.0.3.0/24"]
}

variable "private_subnets" {
  description = "List of private subnet CIDR blocks"
  type        = list(string)
  default     = ["10.0.2.0/24", "10.0.4.0/24"]
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "availibity_zones" {
    description = "List of availability zones to use"
    type        = list(string)
    default     = ["us-east-1a", "us-east-1b"]
}

variable "ecr_repository_name" {
  description = "Name of the ECR repository"
  type        = string
  default     = "ecr-repo-nequi-challenge"
}