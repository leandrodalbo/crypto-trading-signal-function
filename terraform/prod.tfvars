env = "prod"

lambda_function = "trading_signals_lambda"
lambda_filename = "refresh-function-0.0.1-SNAPSHOT-aws.jar"
function_handler= "refresh"
main            = "com.trading.signal.Handler"
file_location   = "../build/libs/refresh-function-0.0.1-SNAPSHOT-aws.jar"
lambda_handler  = "org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"
runtime         = "java17"
cron            = "rate(6 hours)"
timeout         = 900

