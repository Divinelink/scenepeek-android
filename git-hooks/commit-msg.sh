#!/usr/bin/env python3
"""
Git commit hook:
 .git/hooks/commit-msg
 Check commit message according to guidelines
"""

import sys
import re

REGEX = "^(build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test)(\([a-z ]+\))?: [\w,\-\"().\/ ]+$|(Merge (.*\\s*)*)|(Initial commit$)"
# e.g. ^(feat|fix|docs|style|refactor|test|build|ci|perf)(\(.+\))?\:\s(.{3,})


with open(sys.argv[1]) as commit:
    lines = commit.readlines()
    if len(lines) == 0:
        sys.stderr.write("\nEmpty commit message\n")
        sys.stderr.write("\n - Refer commit guide: {}\n\n".format(help_address))
        sys.exit(1)

    match_regex = re.match('({})'.format(REGEX), lines[0])

    if match_regex is None:
        sys.stderr.write("\nYour commit message subject line does not follow the guideline\n")
        sys.stderr.write("The commit message should be structured as follows:\n<type>[optional scope]: <description>\n\nAvailable types: build|chore|ci|docs|feat|fix|perf|refactor|revert|style|test\n")
        sys.stderr.write("\nExample 1: feat: allow provided config object to extend other configs\nExample 2: feat(lang): add english language\n\n")
        sys.exit(1)

    sys.stderr.write("\nYour commit message looks good! \n\n")
sys.exit(0)