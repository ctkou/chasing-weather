#!/bin/bash

BUCKET="$1"
STACK="$2"

sam build
sam package --s3-bucket $BUCKET --output-template-file output.yaml
sam deploy --template-file output.yaml --stack-name $STACK --capabilities CAPABILITY_IAM
