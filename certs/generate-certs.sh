#!/bin/bash

set -e

# Extract Windows output directory from arguments
while getopts "o:" opt; do
  case $opt in
    o)
      WINDOWS_PATH="$OPTARG"
      ;;
    *)
      echo "Usage: $0 -o <output_directory>"
      exit 1
      ;;
  esac
done

# Convert Windows path to Linux path
convert_windows_path_to_linux() {
  local windows_path="$1"
  local linux_path
  linux_path=$(echo "$windows_path" | sed 's|^C:\\|/c/|;s|\\|/|g') # Something is not working !!!!
  echo "$linux_path"
}

# Convert the provided Windows path to Linux format
OUTPUT_DIR=$(convert_windows_path_to_linux "$WINDOWS_PATH")
echo "windows path: $WINDOWS_PATH"
echo "Output directory: $OUTPUT_DIR"


mkdir -p "$OUTPUT_DIR"

# Common names and password
COMMON_NAME="localhost"
P12_PASSWORD="changeit"

echo "🔑 Generating private key..."
openssl genpkey -algorithm RSA -out "$OUTPUT_DIR/private.key" -pkeyopt rsa_keygen_bits:2048

echo "📄 Generating public certificate signing request..."
openssl req -new -key "$OUTPUT_DIR/private.key" -out "$OUTPUT_DIR/request.csr" \
  -subj "/C=US/ST=Dev/L=Local/O=Test/CN=$COMMON_NAME"

echo "📜 Generating self-signed certificate..."
openssl x509 -req -days 365 -in "$OUTPUT_DIR/request.csr" -signkey "$OUTPUT_DIR/private.key" -out "$OUTPUT_DIR/selfsigned.crt"

echo "📦 Creating PKCS#12 (.p12) keystore..."
openssl pkcs12 -export \
  -inkey "$OUTPUT_DIR/private.key" \
  -in "$OUTPUT_DIR/selfsigned.crt" \
  -out "$OUTPUT_DIR/keystore.p12" \
  -name "$COMMON_NAME" \
  -passout pass:$P12_PASSWORD

echo "✅ Generated files:"
ls -l "$OUTPUT_DIR"
