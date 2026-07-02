#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

cd "$PROJECT_ROOT"
./gradlew compileJava

mkdir -p "$SCRIPT_DIR/out"
javac -cp build/classes/java/main -d "$SCRIPT_DIR/out" "$SCRIPT_DIR/Harness.java"
java -cp "build/classes/java/main:$SCRIPT_DIR/out" Harness > "$SCRIPT_DIR/cases.csv"

if [ ! -d "$SCRIPT_DIR/venv" ]; then
  python3 -m venv "$SCRIPT_DIR/venv"
  "$SCRIPT_DIR/venv/bin/pip" install -q -r "$SCRIPT_DIR/requirements.txt"
fi

"$SCRIPT_DIR/venv/bin/python" "$SCRIPT_DIR/verify_repayment_calculator.py" "$SCRIPT_DIR/cases.csv"
