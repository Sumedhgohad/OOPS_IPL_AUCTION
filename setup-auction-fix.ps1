# Quick Setup Script for Auction Fix

Write-Host "=== IPL AUCTION - Database Update Script ===" -ForegroundColor Cyan
Write-Host ""

$projectPath = "c:\Users\ANISH\OneDrive\Desktop\Clg OOP\Cp\New_Cp\OOPS_IPL_AUCTION"
Set-Location $projectPath

# Database credentials
$dbUser = "root"
$dbPassword = "AnishK@1234"
$dbPort = "3307"
$dbName = "ipl_auction"

Write-Host "Step 1: Updating database schema..." -ForegroundColor Yellow
Write-Host "Adding team_players table..."

# Create the SQL command
$sqlCommand = @"
USE $dbName;

CREATE TABLE IF NOT EXISTS team_players (
  team_id INT NOT NULL,
  player_id INT NOT NULL,
  purchase_price DOUBLE NOT NULL,
  PRIMARY KEY (team_id, player_id),
  FOREIGN KEY (team_id) REFERENCES teams(id),
  FOREIGN KEY (player_id) REFERENCES players(id)
);

SELECT 'team_players table created successfully!' AS Result;
"@

# Save to temp file
$sqlCommand | Out-File -FilePath "temp_update.sql" -Encoding UTF8

try {
    # Run MySQL command
    $result = mysql -u $dbUser -p"$dbPassword" -P $dbPort < temp_update.sql 2>&1
    Write-Host "✓ Database updated successfully!" -ForegroundColor Green
    Remove-Item "temp_update.sql" -ErrorAction SilentlyContinue
} catch {
    Write-Host "✗ Error updating database: $_" -ForegroundColor Red
    Write-Host "You may need to run the SQL manually. See db\update_schema.sql" -ForegroundColor Yellow
    Remove-Item "temp_update.sql" -ErrorAction SilentlyContinue
}

Write-Host ""
Write-Host "Step 2: Setting environment variables..." -ForegroundColor Yellow
$env:DB_HOST="localhost"
$env:DB_PORT="3307"
$env:DB_NAME="ipl_auction"
$env:DB_USER="root"
$env:DB_PASSWORD="AnishK@1234"
$env:GEMINI_API_KEY="AIzaSyBEJ_SSKWsHFB0ldtltF4Q4jPQI8bkIp2A"
Write-Host "✓ Environment variables set!" -ForegroundColor Green

Write-Host ""
Write-Host "Step 3: Compiling Java files..." -ForegroundColor Yellow
$JAR = "lib\mysql-connector-j-9.5.0\mysql-connector-j-9.5.0.jar"

if (Test-Path "out") {
    Remove-Item -Recurse -Force "out"
}
New-Item -ItemType Directory -Force -Path "out" | Out-Null

javac -d out -cp $JAR src\com\ipl\auction\model\*.java src\com\ipl\auction\db\*.java src\com\ipl\auction\dao\*.java src\com\ipl\auction\data\*.java src\com\ipl\auction\ui\*.java src\com\ipl\auction\chat\*.java src\com\ipl\auction\*.java 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ Compilation successful!" -ForegroundColor Green
} else {
    Write-Host "✗ Compilation failed!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "=== Setup Complete! ===" -ForegroundColor Green
Write-Host ""
Write-Host "To run the application, use:" -ForegroundColor Cyan
Write-Host "  java -cp `"out;$JAR`" com.ipl.auction.AuctionApp" -ForegroundColor White
Write-Host ""
Write-Host "Or simply run:" -ForegroundColor Cyan
Write-Host "  .\run.ps1" -ForegroundColor White
Write-Host ""

$response = Read-Host "Do you want to run the application now? (Y/N)"
if ($response -eq "Y" -or $response -eq "y") {
    Write-Host ""
    Write-Host "Starting IPL Auction Application..." -ForegroundColor Green
    java -cp "out;$JAR" com.ipl.auction.AuctionApp
}
