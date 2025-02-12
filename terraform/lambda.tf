resource "aws_s3_object" "s3_lambda_object" {
  bucket = var.resources_bucket
  key    = "lambda-function-${var.hash}-aws.jar"
  source = "../target/lambda-function-${var.hash}-aws.jar"
  etag   = filemd5("../target/lambda-function-${var.hash}-aws.jar")
}


resource "aws_lambda_function" "h1_trading_signals_lambda" {
  function_name    = "${var.env}-h1-${var.lambda_function}"
  role             = data.terraform_remote_state.resources.outputs.lambda_role_arn
  handler          = var.lambda_handler
  source_code_hash = aws_s3_object.s3_lambda_object.key
  s3_bucket        = var.resources_bucket
  s3_key           = "lambda-function-${var.hash}-aws.jar"
  runtime          = var.runtime
  timeout          = var.timeout

  environment {
    variables = {
      CONF_API                         = "https://api.binance.com"
      CONF_TIMEFRAME                   = "H1"
      SPRING_CLOUD_FUNCTION_DEFINITION = "refresh"
      SPRING_DATASOURCE_USERNAME       = var.dbuser
      SPRING_DATASOURCE_PASSWORD       = data.terraform_remote_state.resources.outputs.dbpswd
      SPRING_DATASOURCE_URL            = "jdbc:postgresql://${data.terraform_remote_state.resources.outputs.dbhost}:5432/${var.dbname}"
    }
  }
}

resource "aws_lambda_function" "h4_trading_signals_lambda" {
  function_name    = "${var.env}-h4-${var.lambda_function}"
  role             = data.terraform_remote_state.resources.outputs.lambda_role_arn
  handler          = var.lambda_handler
  source_code_hash = aws_s3_object.s3_lambda_object.key
  s3_bucket        = var.resources_bucket
  s3_key           = "lambda-function-${var.hash}-aws.jar"
  runtime          = var.runtime
  timeout          = var.timeout

  environment {
    variables = {
      CONF_API                         = "https://api.binance.com"
      CONF_TIMEFRAME                   = "H4"
      SPRING_CLOUD_FUNCTION_DEFINITION = "refresh"
      SPRING_DATASOURCE_USERNAME       = var.dbuser
      SPRING_DATASOURCE_PASSWORD       = data.terraform_remote_state.resources.outputs.dbpswd
      SPRING_DATASOURCE_URL            = "jdbc:postgresql://${data.terraform_remote_state.resources.outputs.dbhost}:5432/${var.dbname}"
    }
  }
}

resource "aws_lambda_function" "d1_trading_signals_lambda" {
  function_name    = "${var.env}-d1-${var.lambda_function}"
  role             = data.terraform_remote_state.resources.outputs.lambda_role_arn
  handler          = var.lambda_handler
  source_code_hash = aws_s3_object.s3_lambda_object.key
  s3_bucket        = var.resources_bucket
  s3_key           = "lambda-function-${var.hash}-aws.jar"
  runtime          = var.runtime
  timeout          = var.timeout

  environment {
    variables = {
      CONF_API                         = "https://api.binance.com"
      CONF_TIMEFRAME                   = "D1"
      SPRING_CLOUD_FUNCTION_DEFINITION = "refresh"
      SPRING_DATASOURCE_USERNAME       = var.dbuser
      SPRING_DATASOURCE_PASSWORD       = data.terraform_remote_state.resources.outputs.dbpswd
      SPRING_DATASOURCE_URL            = "jdbc:postgresql://${data.terraform_remote_state.resources.outputs.dbhost}:5432/${var.dbname}"
    }
  }
}

resource "aws_cloudwatch_event_rule" "h1_trigger_rule" {
  name                = "trigger_h1_lambda"
  schedule_expression = var.h1_cron
}

resource "aws_cloudwatch_event_rule" "h4_trigger_rule" {
  name                = "trigger_h4_lambda"
  schedule_expression = var.h4_cron
}

resource "aws_cloudwatch_event_rule" "d1_trigger_rule" {
  name                = "trigger_d1_lambda"
  schedule_expression = var.d1_cron
}


resource "aws_cloudwatch_event_target" "h1_trigger_target" {
  arn       = aws_lambda_function.h1_trading_signals_lambda.arn
  rule      = aws_cloudwatch_event_rule.h1_trigger_rule.name
  target_id = aws_lambda_function.h1_trading_signals_lambda.function_name
}

resource "aws_cloudwatch_event_target" "h4_trigger_target" {
  arn       = aws_lambda_function.h4_trading_signals_lambda.arn
  rule      = aws_cloudwatch_event_rule.h4_trigger_rule.name
  target_id = aws_lambda_function.h4_trading_signals_lambda.function_name
}

resource "aws_cloudwatch_event_target" "d1_trigger_target" {
  arn       = aws_lambda_function.d1_trading_signals_lambda.arn
  rule      = aws_cloudwatch_event_rule.d1_trigger_rule.name
  target_id = aws_lambda_function.d1_trading_signals_lambda.function_name
}

resource "aws_lambda_permission" "h1_lambda_permission" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.h1_trading_signals_lambda.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.h1_trigger_rule.arn
}

resource "aws_lambda_permission" "h4_lambda_permission" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.h4_trading_signals_lambda.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.h4_trigger_rule.arn
}

resource "aws_lambda_permission" "d1_lambda_permission" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.d1_trading_signals_lambda.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.d1_trigger_rule.arn
}

