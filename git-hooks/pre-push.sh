#!/usr/bin/env bash
echo "Check code changes..."
OUTPUT="/tmp/pre-push-static-analysis-$(date +%s)"
./gradlew ktlintCheck > $OUTPUT
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  cat $OUTPUT
  rm $OUTPUT
  echo "***********************************************"
  echo "            Static analysis failed             "
  echo " Please fix the above issues before committing "
  echo "***********************************************"
  exit $EXIT_CODE
fi
rm $OUTPUT