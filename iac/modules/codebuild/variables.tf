variable "environment" {
  description = "Environment name"
  type        = string
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "ecr_repository_url" {
  description = "Full URI of the ECR repository (without tag)"
  type        = string
}

variable "project_name" {
  description = "Name of the CodeBuild project"
  type        = string
  default     = "franchising-project-build"
}

