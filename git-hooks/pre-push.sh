#!/usr/bin/env bash
echo "Check code changes..."
OUTPUT="/tmp/pre-push-unit-tests-$(date +%s)"
./gradlew RunUnitTests > $OUTPUT
EXIT_CODE=$?
if [ $EXIT_CODE -ne 0 ]; then
  cat $OUTPUT
  rm $OUTPUT
  echo "***********************************************"
  echo "            Unit tests failed              "
  echo " Please fix the above issues before committing "
  echo "***********************************************"
  exit $EXIT_CODE
fi
rm $OUTPUT