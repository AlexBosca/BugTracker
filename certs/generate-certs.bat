@echo off
setlocal ENABLEDELAYEDEXPANSION

set "OPENSSL=C:\Program Files\Git\usr\bin\openssl.exe"

:: === Check input argument ===
if "%~1"=="" (
    echo ❌ ERROR: Output directory not provided.
    echo Usage: generate-certs.bat ^<output-directory^>
    exit /b 1
)

set "OUTPUT_DIR=%~1"
set "KEY_FILE=%OUTPUT_DIR%\key.pem"
set "CERT_FILE=%OUTPUT_DIR%\cert.pem"
set "P12_FILE=%OUTPUT_DIR%\keystore.p12"
set "PRIVATE_KEY=%OUTPUT_DIR%\private.pem"
set "PUBLIC_KEY=%OUTPUT_DIR%\public.pem"

:: === Create output directory if it doesn't exist ===
if not exist "%OUTPUT_DIR%" (
    mkdir "%OUTPUT_DIR%"
)

echo 🔐 Generating RSA private key...
"%OPENSSL%" genpkey -algorithm RSA -out "%PRIVATE_KEY%" -pkeyopt rsa_keygen_bits:2048

echo 🔓 Extracting public key...
"%OPENSSL%" rsa -pubout -in "%PRIVATE_KEY%" -out "%PUBLIC_KEY%"

if errorlevel 1 (
    echo ❌ Failed to extract public key
    exit /b 1
)

echo ✅ RSA key pair generated:
echo   • %PRIVATE_KEY%
echo   • %PUBLIC_KEY%

echo 🔐 Generating key and certificate in "%OUTPUT_DIR%"...

:: === Generate key and self-signed cert ===
"%OPENSSL%" req -x509 -newkey rsa:2048 -nodes -days 365 ^
    -keyout "%KEY_FILE%" -out "%CERT_FILE%" ^
    -subj "/CN=localhost"

if errorlevel 1 (
    echo ❌ ERROR: Failed to generate cert/key.
    exit /b 1
)

:: === Convert to PKCS#12 (.p12) ===
"%OPENSSL%" pkcs12 -export ^
    -inkey "%KEY_FILE%" -in "%CERT_FILE%" ^
    -out "%P12_FILE%" -name "selfsigned" ^
    -passout pass:changeit

if errorlevel 1 (
    echo ❌ ERROR: Failed to create keystore.p12
    exit /b 1
)

echo ✅ Done! Files generated:
echo   • key.pem
echo   • cert.pem
echo   • keystore.p12
endlocal
