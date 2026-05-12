# NEXORA CHAT - Push Updates to GitHub
# Run this script to update your repository

Write-Host @"

╔════════════════════════════════════════════════════════════╗
║                                                            ║
║           NEXORA CHAT - Update Script                     ║
║                                                            ║
╚════════════════════════════════════════════════════════════╝

"@ -ForegroundColor Cyan

Write-Host ""
Write-Host "Repository: https://github.com/alfqyhar26-crypto/nexora-chat" -ForegroundColor Yellow
Write-Host ""

# Change to project directory
$projectPath = "C:\Users\alwah\OneDrive\OneDrive\سطح المكتب\تطبيق دردشة\nexora-android"
Set-Location $projectPath

Write-Host "[1/4] Checking git status..." -ForegroundColor Cyan

# Check if git is installed
try {
    $gitVersion = git --version 2>$null
    Write-Host "  Git found: $gitVersion" -ForegroundColor Green
} catch {
    Write-Host "  ERROR: Git is not installed!" -ForegroundColor Red
    Write-Host "  Please install Git from: https://git-scm.com" -ForegroundColor Yellow
    exit 1
}

# Check for changes
$status = git status --porcelain
if ($status) {
    Write-Host "  Found changes to commit" -ForegroundColor Yellow
} else {
    Write-Host "  No changes detected" -ForegroundColor Gray
}

Write-Host ""
Write-Host "[2/4] Adding all files..." -ForegroundColor Cyan
git add -A

Write-Host ""
Write-Host "[3/4] Committing changes..." -ForegroundColor Cyan
git commit -m "Fix: Remove wrong workflow, add gradle-wrapper.jar"

Write-Host ""
Write-Host "[4/4] Pushing to GitHub..." -ForegroundColor Cyan
git push -u origin main

Write-Host ""
Write-Host @"

╔════════════════════════════════════════════════════════════╗
║                                                            ║
║           ✅ Push Complete!                               ║
║                                                            ║
║  Next steps:                                              ║
║  1. Wait 5-10 minutes for build                         ║
║  2. Check: https://github.com/alfqyhar26-crypto/nexora-chat/actions ║
║  3. Download APK from Artifacts                          ║
║                                                            ║
╚════════════════════════════════════════════════════════════╝

"@ -ForegroundColor Green