output "db_password" {
  value     = module.rds.db_password
  sensitive = true
}

output "db_endpoint" {
  value = module.rds.db_endpoint
}
output "db_username" {
    value = module.rds.db_username
}

output "api_endpoint" {
  value = module.apigateway.api_endpoint
}