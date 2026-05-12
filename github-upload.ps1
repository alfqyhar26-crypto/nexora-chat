# NEXORA CHAT - GitHub Upload Script
# Run this in PowerShell to upload your project to GitHub

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  NEXORA CHAT - GitHub Upload Tool" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if git is installed
Write-Host "Checking Git installation..." -ForegroundColor Yellow
$gitVersion = git --version 2>$null
if (-not $gitVersion) {
    Write-Host "Git is not installed!" -ForegroundColor Red
    Write-Host "Please install Git first: https://git-scm.com/download/win" -ForegroundColor Yellow
    exit 1
}
Write-Host "Git found: $gitVersion" -ForegroundColor Green

# Navigate to project directory
$projectPath = Split-Path -Parent $PSScriptRoot
Set-Location $projectPath

Write-Host ""
Write-Host "Current directory: $(Get-Location)" -ForegroundColor Cyan

# Initialize git if not initialized
if (-not (Test-Path ".git")) {
    Write-Host ""
    Write-Host "Initializing Git repository..." -ForegroundColor Yellow
    git init
    git add .
    git commit -m "Initial commit - NEXORA CHAT v1.0.0-alpha"
    Write-Host "Git repository initialized!" -ForegroundColor Green
} else {
    Write-Host "Git repository already exists" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Next Steps:" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "1. Create a new repository on GitHub:" -ForegroundColor White
Write-Host "   https://github.com/new" -ForegroundColor Cyan
Write-Host ""
Write-Host "2. In your new repository, copy the git remote URL" -ForegroundColor White
Write-Host ""
Write-Host "3. Run these commands:" -ForegroundColor White
Write-Host ""
Write-Host "   git remote add origin YOUR_GITHUB_URL" -ForegroundColor Yellow
Write-Host "   git branch -M main" -ForegroundColor Yellow
Write-Host "   git push -u origin main" -ForegroundColor Yellow
Write-Host ""
Write-Host "4. GitHub Actions will automatically build:" -ForegroundColor White
Write-Host "   - Debug APK" -ForegroundColor Green
Write-Host "   - Release AAB" -ForegroundColor Green
Write-Host ""
Write-Host "5. Download artifacts from:" -ForegroundColor White
Write-Host "   Your Repository > Actions tab" -ForegroundColor Cyan
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan