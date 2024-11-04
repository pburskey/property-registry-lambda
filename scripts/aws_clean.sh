#!/bin/bash
aws cloudformation delete-stack --stack-name propertyconfiguration



#stackName=$1
stackName=propertyconfiguration
#resoureceId=$2
resoureceId=PipelineStartBucket



pipelineBucket=$(. describe_stack.sh $stackName $resoureceId)
pipelineBucket=$(echo "$pipelineBucket" | sed 's/"//g')

aws s3 rm s3://${pipelineBucket} --recursive
aws s3 rb s3://${pipelineBucket} --force