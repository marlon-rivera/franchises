resource "random_password" "db_password" {
  length  = 16
  special = true
  override_special = "!@#$%^&*()_+"
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
  skip_final_snapshot     = true
  publicly_accessible     = false
  deletion_protection     = false
  auto_minor_version_upgrade = true

  tags = {
    Name        = var.db_identifier
    Environment = var.environment
  }
}
