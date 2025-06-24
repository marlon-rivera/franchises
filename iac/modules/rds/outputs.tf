output "db_endpoint" {
  description = "RDS instance endpoint"
  value       = aws_db_instance.this.endpoint
}

output "db_username" {
  description = "Database master username"
  value       = var.db_username
}

output "db_password" {
  description = "Database master password"
  value       = random_password.db_password.result
}
