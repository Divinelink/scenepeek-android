#!/bin/bash
if git diff-index --quiet HEAD -- kmmsharedmodule; then
    echo “No changes“
else
    # Changes
    echo “Running pre-push hook”
    echo “- Build KMM Swift Package -”
    ./gradlew kmmsharedmodule:createSwiftPackage
    if [ $? -ne 0 ]; then
    echo “Swift Package building failed.”
    exit 1
    fi
fi