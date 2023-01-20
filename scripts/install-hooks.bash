#!/bin/bash
GIT_DIR=$(git rev-parse --git-dir)
echo "Installing hooks…"
echo "Pre-Push"
ln -s ../../scripts/pre-push.bash $GIT_DIR/hooks/pre-push

echo "Pre-Commit"
ln -s ../../scripts/pre-commit.bash $GIT_DIR/hooks/pre-commit
echo “Done!”