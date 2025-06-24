variable "alb_dns" {
  description = "ALB DNS to be used as integration target"
  type        = string
}

variable "environment" {
    description = "Environment for the API Gateway"
    type        = string
}

variable "region" {
    description = "AWS region where the API Gateway will be deployed"
    type        = string
}