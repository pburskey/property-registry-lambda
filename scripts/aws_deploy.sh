#!/bin/bash
scriptBase=$(pwd)
cd ..
mavenBase=$(pwd)
cd $scriptBase

environment_variable=aws_bucket
$(. $scriptBase/check_environment.sh $environment_variable)
if [ $? -ne 0 ]; then
  echo "Missing environment variable: $environment_variable"
  exit 1
fi

aws_bucket=$aws_bucket

resources_file=resources.txt
rm $mavenBase/src/test/resources/$resources_file

cd $mavenBase
mvn package

sam deploy \
--s3-bucket $aws_bucket

cd $scriptBase
. aws_build_url_for_function.sh PropertyFindByIDLambda / 2>&1 | tee -a $scriptBase/$resources_file
. aws_build_url_for_function.sh PropertySaveLambda /save 2>&1 | tee -a $scriptBase/$resources_file
. aws_build_url_for_function.sh PropertyFindByNameCategoryLambda /find 2>&1 | tee -a $scriptBase/$resources_file
mv $resources_file $mavenBase/src/test/resources
