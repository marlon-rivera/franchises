resource "random_password" "db_password" {
  length  = 16
  special = true
  override_special = "!@#$%^&*()_+"
}

resource "aws_secretsmanager_secret" "db_password" {
  name        = "${var.environment}-${var.db_identifier}-db-password"
  description = "RDS database password for ${var.db_identifier}"

  tags = {
    Name        = "${var.environment}-${var.db_identifier}-db-password"
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "db_password" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = random_password.db_password.result
}

resource "aws_db_subnet_group" "this" {
  name       = "${var.db_identifier}-${var.environment}-subnet-group"
  subnet_ids = var.subnet_ids

  tags = {
    Name        = "${var.db_identifier}-${var.environment}-subnet-group"
    Environment = var.environment
  }
}

resource "aws_db_instance" "this" {
  identifier              = var.db_identifier
  engine                  = var.engine
  engine_version          = var.engine_version
  instance_class          = var.instance_class
  allocated_storage       = var.allocated_storage
  username                = var.db_username
  password                = random_password.db_password.result
  db_name                 = var.db_name
  port                    = var.db_port
  vpc_security_group_ids  = [var.rds_sg_id]
  db_subnet_group_name    = aws_db_subnet_group.this.name
  storage_encrypted       = true
  skip_final_snapshot     = var.environment == "prod" ? false : true
  final_snapshot_identifier = var.environment == "prod" ? "${var.db_identifier}-final-snapshot" : null
  publicly_accessible     = false
  deletion_protection     = var.environment == "prod" ? true : false
  auto_minor_version_upgrade = true
  backup_retention_period = var.environment == "prod" ? 7 : 1
  multi_az                = var.environment == "prod" ? true : false

  tags = {
    Name        = var.db_identifier
    Environment = var.environment
  }
}
