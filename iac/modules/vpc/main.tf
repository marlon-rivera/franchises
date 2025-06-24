#VPC
resource "aws_vpc" "nequi-challenge-vpc" {
  cidr_block = var.vpc_cidr
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = {
    Name = "nequi-challenge-vpc"
    Environment = var.environment
  }
}

# Public subnet
resource "aws_subnet" "public-subnet" {
  count = length(var.public_subnet_cidrs)
  vpc_id     = aws_vpc.nequi-challenge-vpc.id
  cidr_block = var.public_subnet_cidrs[count.index]
  map_public_ip_on_launch = true
  availability_zone = var.availability_zones[count.index]
    tags = {
        Name = "nequi-challenge-public-subnet"
        Environment = var.environment
    }
}

#Private subnet
resource "aws_subnet" "private-subnet" {
  count = length(var.private_subnet_cidrs)
  vpc_id     = aws_vpc.nequi-challenge-vpc.id
  cidr_block = var.private_subnet_cidrs[count.index]
  availability_zone = var.availability_zones[count.index]
    tags = {
        Name = "nequi-challenge-private-subnet"
        Environment = var.environment
    }
}

#Internet Gateway
resource "aws_internet_gateway" "igw" {
    vpc_id = aws_vpc.nequi-challenge-vpc.id
    tags = {
        Name = "nequi-challenge-igw"
        Environment = var.environment
    }
}

#Eip for NAT Gateway
resource "aws_eip" "nat_eip" {
  count = 1
  domain = "vpc"
}

#NAT Gateway
resource "aws_nat_gateway" "nat-gateway" {
  count = 1
  allocation_id = aws_eip.nat_eip[0].id
  subnet_id     = aws_subnet.public-subnet[0].id
  tags = {
    Name = "nequi-challenge-nat-gateway"
    Environment = var.environment
  }
}

#Route Table for public subnet
resource "aws_route_table" "publicRT" {
  vpc_id = aws_vpc.nequi-challenge-vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
}

#Associate public subnet with the public route table
resource "aws_route_table_association" "publicRTAssociation" {
  count = length(aws_subnet.public-subnet)
  subnet_id      = aws_subnet.public-subnet[count.index].id
  route_table_id = aws_route_table.publicRT.id
}

#Route Table for private subnet
resource "aws_route_table" "privateRT" {
  vpc_id = aws_vpc.nequi-challenge-vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat-gateway[0].id
  }
}

#Associate private subnet with the private route table
resource "aws_route_table_association" "privateRTAssociation" {
    count = length(aws_subnet.private-subnet)
  subnet_id      = aws_subnet.private-subnet[count.index].id
  route_table_id = aws_route_table.privateRT.id
}

#Security group for ALB
resource "aws_security_group" "alb_sg" {
  name        = "${var.environment}-alb-sg"
  description = "Security group for ALB in ${var.environment} environment"
  vpc_id      = aws_vpc.nequi-challenge-vpc.id

  ingress {
    from_port = 80
    to_port   = 80
    protocol  = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name        = "${var.environment}-alb-sg"
    Environment = var.environment
  }
}

#Security group for ECS tasks
resource "aws_security_group" "ecs_sg" {
  name        = "${var.environment}-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = aws_vpc.nequi-challenge-vpc.id

  ingress {
    from_port   = var.container_port
    to_port     = var.container_port
    protocol    = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
    description = "Allow traffic from ALB to ECS tasks"
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Environment = var.environment
    Name        = "${var.environment}-ecs-sg"
  }
}

#Security group for RDS
resource "aws_security_group" "rds_sg" {
  name        = "${var.environment}-rds-sg"
  description = "Security group for RDS instance"
  vpc_id      = aws_vpc.nequi-challenge-vpc.id

  ingress {
    from_port = var.db_port
    to_port   = var.db_port
    protocol  = "tcp"
    security_groups = [aws_security_group.ecs_sg.id]
    description = "Allow traffic from ECS tasks to RDS"
  }
  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    description = "Allow all outbound traffic"
  }

  tags = {
    Name        = "${var.environment}-rds-sg"
    Environment = var.environment
  }
}
