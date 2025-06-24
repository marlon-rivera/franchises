variable "cluster_name" {
  description = "Name of the ECS cluster"
  type        = string
}

variable "container_name" {
  description = "Name of the container"
  type        = string
}

variable "container_image" {
  description = "Image for the container"
  type        = string
}

variable "container_port" {
  description = "Port the container exposes"
  type        = number
  default     = 8080
}

variable "subnets_ids" {
    description = "List of subnet IDs where the ECS service will be deployed"
    type        = list(string)
}

variable "security_groups" {
    description = "List of security group IDs to associate with the ECS service"
    type        = list(string)
}

variable "target_group_arn" {
    description = "ARN of the target group to register the ECS service with"
    type        = string
}

variable "environment" {
    description = "Environment for which the ECS service is being created (e.g., dev, staging, prod)"
    type        = string
}

variable "task_family" {
    description = "Family name for the ECS task definition"
    type        = string
}

variable "task_cpu" {
    description = "CPU units for the ECS task"
    type        = string
}

variable "task_memory" {
    description = "Memory in MiB for the ECS task"
    type        = string
}

variable "database_url" {
    description = "URL of the database to connect to"
    type        = string
}

variable "database_user" {
    description = "Username for the database"
    type        = string
}

variable "database_password" {
    description = "Password for the database"
    type        = string
}

variable "service_name" {
    description = "Name of the ECS service"
    type        = string
}

variable "vpc_id" {
    description = "ID of the VPC where the ECS service will be deployed"
    type        = string
}

variable "alb_sg_id" {
    description = "Security group ID for the ALB to allow traffic to ECS tasks"
    type        = string
}