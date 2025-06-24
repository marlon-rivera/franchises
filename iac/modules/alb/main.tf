
# Application Load Balancer
resource "aws_lb" "alb" {
    name               = "${var.environment}-alb"
    internal           = false
    load_balancer_type = "application"
    security_groups    = [var.alb_sg_id]
    subnets            = var.subnets
    enable_deletion_protection = false

    tags = {
        Name        = "${var.environment}-alb"
        Environment = var.environment
    }
}

# Target Group
resource "aws_lb_target_group" "target_group" {
    name     = "${var.environment}-target-group"
    port     = var.target_port
    protocol = "HTTP"
    vpc_id   = var.vpc_id
    target_type = "ip"

    health_check {
        path                = "/actuator/health"
        interval            = 30
        timeout             = 5
        healthy_threshold  = 2
        unhealthy_threshold = 2
        matcher             = "200"
    }

    tags = {
        Name        = "${var.environment}-target-group"
        Environment = var.environment
    }
}

# Listener for the ALB
resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.target_group.arn
  }
}