resource "aws_api_gateway_rest_api" "rest_api" {
  name          = "${var.environment}-franchise-api"
}

resource "aws_api_gateway_resource" "api" {
  parent_id   = aws_api_gateway_rest_api.rest_api.root_resource_id
  path_part   = "api"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "franchise" {
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "franchise"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "create_franchise" {
  parent_id   = aws_api_gateway_resource.franchise.id
  path_part   = "create"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "create_franchise" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.create_franchise.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "create_franchise" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.create_franchise.id
  http_method             = aws_api_gateway_method.create_franchise.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/franchise/create"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

resource "aws_api_gateway_resource" "franchise_id" {
  parent_id   = aws_api_gateway_resource.franchise.id
  path_part   = "{franchiseId}"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "franchise_top_products" {
  parent_id   = aws_api_gateway_resource.franchise_id.id
  path_part   = "top-products"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "franchise_top_products" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.franchise_top_products.id
  http_method   = "GET"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.franchiseId" = true
    }
}

resource "aws_api_gateway_integration" "franchise_top_products" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.franchise_top_products.id
  http_method             = aws_api_gateway_method.franchise_top_products.http_method
  integration_http_method = "GET"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/franchise/{franchiseId}/top-products"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.franchiseId" = "method.request.path.franchiseId"
  }
}

resource "aws_api_gateway_resource" "update_franchise_name" {
  parent_id   = aws_api_gateway_resource.franchise_id.id
  path_part   = "update-name"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "update_franchise_name" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.update_franchise_name.id
  http_method   = "PUT"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.franchiseId" = true
    }
}

resource "aws_api_gateway_integration" "update_franchise_name" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.update_franchise_name.id
  http_method             = aws_api_gateway_method.update_franchise_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/franchise/{franchiseId}/update-name"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.franchiseId" = "method.request.path.franchiseId"
  }
}

resource "aws_api_gateway_resource" "branch" {
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "branch"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "create_branch" {
  parent_id   = aws_api_gateway_resource.branch.id
  path_part   = "create"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "create_branch" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.create_branch.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "create_branch" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.create_branch.id
  http_method             = aws_api_gateway_method.create_branch.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/branch/create"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

resource "aws_api_gateway_resource" "branch_id" {
  parent_id   = aws_api_gateway_resource.branch.id
  path_part   = "{branchId}"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "update_branch_name" {
  parent_id   = aws_api_gateway_resource.branch_id.id
  path_part   = "update-name"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id

}

resource "aws_api_gateway_method" "update_branch_name" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.update_branch_name.id
  http_method   = "PUT"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.branchId" = true
    }
}

resource "aws_api_gateway_integration" "update_branch_name" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.update_branch_name.id
  http_method             = aws_api_gateway_method.update_branch_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/branch/{branchId}/update-name"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.branchId" = "method.request.path.branchId"
  }
}

resource "aws_api_gateway_resource" "product" {
  parent_id   = aws_api_gateway_resource.api.id
  path_part   = "product"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "create_product" {
  parent_id   = aws_api_gateway_resource.product.id
  path_part   = "create"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "create_product" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.create_product.id
  http_method   = "POST"
  authorization = "NONE"
}

resource "aws_api_gateway_integration" "create_product" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.create_product.id
  http_method             = aws_api_gateway_method.create_product.http_method
  integration_http_method = "POST"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/product/create"
  passthrough_behavior    = "WHEN_NO_MATCH"
}

resource "aws_api_gateway_resource" "product_id" {
  parent_id   = aws_api_gateway_resource.product.id
  path_part   = "{productId}"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "update_product_name" {
  parent_id   = aws_api_gateway_resource.product_id.id
  path_part   = "update-name"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "update_product_name" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.update_product_name.id
  http_method   = "PUT"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.productId" = true
    }
}

resource "aws_api_gateway_integration" "update_product_name" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.update_product_name.id
  http_method             = aws_api_gateway_method.update_product_name.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/product/{productId}/update-name"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
  }
}

resource "aws_api_gateway_resource" "delete_product" {
  parent_id   = aws_api_gateway_resource.product.id
  path_part   = "delete"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "delete_product_product" {
  parent_id   = aws_api_gateway_resource.delete_product.id
  path_part   = "product"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "delete_product_product_id" {
  parent_id   = aws_api_gateway_resource.delete_product_product.id
  path_part   = "{productId}"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "delete_product_branch" {
  parent_id   = aws_api_gateway_resource.delete_product_product_id.id
  path_part   = "branch"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "delete_product_branch_id" {
  parent_id   = aws_api_gateway_resource.delete_product_branch.id
  path_part   = "{branchId}"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "delete_product" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.delete_product_branch_id.id
  http_method   = "DELETE"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.productId" = true
        "method.request.path.branchId" = true
    }
}

resource "aws_api_gateway_integration" "delete_product" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.delete_product_branch_id.id
  http_method             = aws_api_gateway_method.delete_product.http_method
  integration_http_method = "DELETE"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/product/delete/product/{productId}/branch/{branchId}"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
    "integration.request.path.branchId" = "method.request.path.branchId"
  }
}

resource "aws_api_gateway_resource" "update_product_stock" {
  parent_id   = aws_api_gateway_resource.product.id
  path_part   = "update"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_resource" "update_product_stock_stock" {
  parent_id   = aws_api_gateway_resource.update_product_stock.id
  path_part   = "stock"
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
}

resource "aws_api_gateway_method" "update_product_stock" {
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  resource_id   = aws_api_gateway_resource.update_product_stock_stock.id
  http_method   = "PUT"
  authorization = "NONE"
    request_parameters = {
        "method.request.path.productId" = true
        "method.request.path.branchId" = true
    }
}

resource "aws_api_gateway_integration" "update_product_stock" {
  rest_api_id             = aws_api_gateway_rest_api.rest_api.id
  resource_id             = aws_api_gateway_resource.update_product_stock_stock.id
  http_method             = aws_api_gateway_method.update_product_stock.http_method
  integration_http_method = "PUT"
  type                    = "HTTP_PROXY"
  uri                     = "http://${var.alb_dns}/api/product/update/stock"
  passthrough_behavior    = "WHEN_NO_MATCH"
  request_parameters = {
    "integration.request.path.productId" = "method.request.path.productId"
    "integration.request.path.branchId" = "method.request.path.branchId"
  }
}

resource "aws_api_gateway_deployment" "api_deployment" {
  depends_on = [
    aws_api_gateway_integration.create_franchise,
    aws_api_gateway_integration.franchise_top_products,
    aws_api_gateway_integration.update_franchise_name,
    aws_api_gateway_integration.create_branch,
    aws_api_gateway_integration.update_branch_name,
    aws_api_gateway_integration.create_product,
    aws_api_gateway_integration.update_product_name,
    aws_api_gateway_integration.delete_product,
    aws_api_gateway_integration.update_product_stock
  ]
  rest_api_id = aws_api_gateway_rest_api.rest_api.id
  triggers = {
    redeploy = timestamp()
  }
  lifecycle {
    create_before_destroy = true
  }
}

resource "aws_api_gateway_stage" "api_stage" {
  stage_name    = var.environment
  rest_api_id   = aws_api_gateway_rest_api.rest_api.id
  deployment_id = aws_api_gateway_deployment.api_deployment.id
  depends_on = [aws_api_gateway_deployment.api_deployment]
  variables = {
    alb_dns = var.alb_dns
  }
}