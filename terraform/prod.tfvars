env = "prod"

lambda_function = "trading_signals_lambda"
lambda_handler  = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
runtime         = "java17"
cron            = "rate(6 hours)"
timeout         = 900

