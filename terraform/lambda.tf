resource "aws_s3_object" "s3_lambda_object" {
  bucket = var.resources_bucket
  key    = var.lambda_filename
  source = var.file_location
  etag   = filemd5(var.file_location)
}

resource "aws_lambda_function" "trading_signals_lambda" {
  function_name    = var.lambda_function
  role             = aws_iam_role.lambda_role.arn
  handler          = var.lambda_handler
  source_code_hash = aws_s3_object.s3_lambda_object.key
  s3_bucket        = var.resources_bucket
  s3_key           = var.lambda_filename
  runtime          = var.runtime
  timeout          = var.timeout

  environment {
    variables = {
      SPRING_DATASOURCE_USERNAME = var.dbuser
      SPRING_DATASOURCE_PASSWORD = data.terraform_remote_state.resources.outputs.dbpswd
      SPRING_DATASOURCE_URL      = "jdbc:postgresql://${data.terraform_remote_state.resources.outputs.dbhost}:5432/${var.dbname}"
    }
  }
}

resource "aws_cloudwatch_event_rule" "trigger_event_rule" {
  name                = "trigger_signals_lambda_event"
  schedule_expression = var.cron
}

resource "aws_cloudwatch_event_target" "trigger_event_target" {
  arn       = aws_lambda_function.trading_signals_lambda.arn
  rule      = aws_cloudwatch_event_rule.trigger_event_rule.name
  target_id = aws_lambda_function.trading_signals_lambda.function_name
}

resource "aws_lambda_permission" "lambda_permission" {
  statement_id  = "AllowExecutionFromCloudWatch"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.trading_signals_lambda.function_name
  principal     = "events.amazonaws.com"
  source_arn    = aws_cloudwatch_event_rule.trigger_event_rule.arn
}