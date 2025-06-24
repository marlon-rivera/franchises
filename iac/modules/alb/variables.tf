variable "vpc_id" {
    description = "The ID of the VPC where the ALB and target group will be created."
    type        = string
}

variable "subnets" {
    description = "List of subnet IDs where the ALB will be deployed."
    type        = list(string)
}

variable "environment" {
    description = "The environment for which the ALB and target group are being created (e.g., dev, staging, prod)."
    type        = string
}

variable "target_port" {
    description = "The port on which the target group will listen."
    type        = number
    default     = 8080
}

variable "alb_sg_id" {
    description = "The security group ID for the ALB."
    type        = string
}