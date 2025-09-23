#!/usr/bin/env bash
set -euo pipefail

# === Config ===
OPENSSL="openssl"

# === Check input argument ===
if [ $# -lt 1 ]; then
  echo "❌ ERROR: Output directory not provided."
  echo "Usage: $0 <output-directory>"
  exit 1
fi

OUTPUT_DIR="$1"
KEY_FILE="$OUTPUT_DIR/key.pem"
CERT_FILE="$OUTPUT_DIR/cert.pem"
P12_FILE="$OUTPUT_DIR/keystore.p12"
PRIVATE_KEY="$OUTPUT_DIR/private.pem"
PUBLIC_KEY="$OUTPUT_DIR/public.pem"

# === Create output directory if it doesn't exist ===
mkdir -p "$OUTPUT_DIR"

echo "🔐 Generating RSA private key..."
$OPENSSL genpkey -algorithm RSA -out "$PRIVATE_KEY" -pkeyopt rsa_keygen_bits:2048

echo "🔓 Extracting public key..."
if ! $OPENSSL rsa -pubout -in "$PRIVATE_KEY" -out "$PUBLIC_KEY"; then
  echo "❌ Failed to extract public key"
  exit 1
fi

echo "✅ RSA key pair generated:"
echo "   • $PRIVATE_KEY"
echo "   • $PUBLIC_KEY"

echo "🔐 Generating key and certificate in $OUTPUT_DIR..."

# === Generate key and self-signed cert ===
if ! $OPENSSL req -x509 -newkey rsa:2048 -nodes -days 365 \
  -keyout "$KEY_FILE" -out "$CERT_FILE" \
  -subj "/CN=localhost"; then
  echo "❌ ERROR: Failed to generate cert/key."
  exit 1
fi

# === Convert to PKCS#12 (.p12) ===
if ! $OPENSSL pkcs12 -export \
  -inkey "$KEY_FILE" -in "$CERT_FILE" \
  -out "$P12_FILE" -name "selfsigned" \
  -passout pass:changeit; then
  echo "❌ ERROR: Failed to create keystore.p12"
  exit 1
fi

echo "✅ Done! Files generated:"
echo "   • $KEY_FILE"
echo "   • $CERT_FILE"
echo "   • $P12_FILE"
