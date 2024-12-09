env = "prod"

lambda_function = "trading_signals_lambda"
lambda_handler  = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
runtime         = "java17"
h1_cron         = "rate(8 hours)"
h4_cron         = "rate(16 hours)"
d1_cron         = "rate(36 hours)"
timeout         = 900

