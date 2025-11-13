# Run IPL Auction Application

$projectPath = "c:\Users\ANISH\OneDrive\Desktop\Clg OOP\Cp\New_Cp\OOPS_IPL_AUCTION"
Set-Location $projectPath

Write-Host "=== Starting IPL Auction Application ===" -ForegroundColor Cyan
Write-Host ""

# Set environment variables
$env:DB_HOST="localhost"
$env:DB_PORT="3307"
$env:DB_NAME="ipl_auction"
$env:DB_USER="root"
$env:DB_PASSWORD="AnishK@1234"
$env:GEMINI_API_KEY="AIzaSyBEJ_SSKWsHFB0ldtltF4Q4jPQI8bkIp2A"

$JAR = "lib\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar"

# Run application
java -cp "out;$JAR" com.ipl.auction.AuctionApp
