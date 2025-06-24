variable "db_identifier" {
  description = "Identifier for the RDS instance"
  type        = string
}

variable "db_name" {
  description = "Database name"
  type        = string
}

variable "db_username" {
  description = "Master username"
  type        = string
}

variable "engine" {
    description = "Database engine"
    type        = string
}

variable "engine_version" {
    description = "Version of the database engine"
    type        = string
}

variable "db_port" {
  description = "Port for the database"
  type        = number
}

variable "instance_class" {
  description = "Instance class for RDS"
  type        = string
  default     = "db.t3.micro"
}

variable "allocated_storage" {
  description = "Storage allocated in GB"
  type        = number
  default     = 20
}

variable "subnet_ids" {
  description = "List of subnet IDs for DB subnet group"
  type        = list(string)
}

variable "environment" {
  description = "Environment name"
  type        = string
  default     = "dev"
}

variable "rds_sg_id" {
  description = "Security group ID for the RDS instance"
  type        = string
}