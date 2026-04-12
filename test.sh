#!/bin/bash

set -u

./run-valid-tests.sh
valid_status=$?

./run-invalid-tests.sh
invalid_status=$?

if [ "$valid_status" -eq 0 ] && [ "$invalid_status" -eq 0 ]; then
    exit 0
fi

exit 1