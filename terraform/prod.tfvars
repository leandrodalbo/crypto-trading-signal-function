env = "prod"

lambda_function = "trading_signals_lambda"
lambda_filename = "refresh-function-0.0.1-SNAPSHOT-aws.jar"
file_location   = "../build/libs/refresh-function-0.0.1-SNAPSHOT-aws.jar"
lambda_handler  = "com.trading.signal.functions.Refresh"
runtime         = "java17"
cron            = "rate(6 hour)"
timeout         = 2

