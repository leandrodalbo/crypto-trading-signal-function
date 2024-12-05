# crypto-trading-signal-function

Serverless Function

- Fetching USD trading symbols from Binance
- Fetch OHLC data for every symbols on different timeframes
- Use different trading indicators to find BUY/SELL signal
- Every Signal is sent to rabbitmq to be processed


# trigger function locally

```bash
$curl http://localhost:8080/refresh
```
