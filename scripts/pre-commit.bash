#!/bin/bash
if git diff-index --quiet HEAD -- kmmsharedmodule; then
    echo “No changes“
else
    # Changes
    echo “Running pre-commit hook”
    echo “- Run KMM unit tests -”
    ./gradlew kmmsharedmodule:testDebugUnitTest

     echo “- Running Detekt Format -”
    ./gradlew detektFormat

    # echo “- Build KMM Swift Package -”
    # ./gradlew kmmsharedmodule:createSwiftPackage

    if [ $? -ne 0 ]; then
    echo Unit tests have failed.”
    exit 1
    fi
fi
