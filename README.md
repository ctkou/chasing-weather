# chasing-weather-api

## Deployment

Source -> sam build -> sam package -> same deploy

```bash
sam build
```

```bash
sam package --s3-bucket [s3-bucket-name] --output-template-file output.yaml
```

first time deployment
```bash
sam deploy --template-file output.yaml --stack-name [stack-name] --capabilities CAPABILITY_IAM
```
