output "alb_dns_name" {
  description = "Public DNS of the ALB"
  value       = aws_lb.alb.dns_name
}

output "alb_arn" {
  value = aws_lb.alb.arn
}

output "target_group_arn" {
  value = aws_lb_target_group.target_group.arn
}

output "listener_arn" {
  value = aws_lb_listener.http.arn
}

output "alb_dns" {
  value = aws_lb.alb.dns_name
}
