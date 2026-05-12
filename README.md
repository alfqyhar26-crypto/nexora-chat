# NEXORA CHAT - Build Guide

## Quick Start

### Building APK/AAB via GitHub Actions (Recommended)

#### Step 1: Create GitHub Repository
1. Go to https://github.com
2. Create new repository: `nexora-chat`
3. Make it Public

#### Step 2: Upload Project Files
1. In your new repository, click "uploading an existing file"
2. Drag and drop the entire `nexora-android` folder
3. Commit changes

#### Step 3: GitHub Actions Will Build Automatically
1. Go to "Actions" tab in your repository
2. Watch the build progress
3. After build completes (~10-15 minutes):
   - Debug APK: Download from Artifacts
   - Release AAB: Download from Releases

---

## Project Structure

```
nexora-android/
├── .github/workflows/build.yml    # CI/CD Pipeline
├── gradlew.bat                     # Windows build script
├── gradle/wrapper/
│   └── gradle-wrapper.properties
├── app/                            # Main application
│   └── src/main/
│       ├── java/com/nexora/app/
│       │   ├── MainActivity.kt
│       │   ├── NexoraApp.kt
│       │   └── AppModule.kt
│       └── AndroidManifest.xml
├── core/                           # Core utilities
│   └── src/main/java/com/nexora/core/
│       ├── di/CoreModule.kt
│       ├── logger/NexoraLogger.kt
│       ├── network/ApiClient.kt
│       └── util/Result.kt
├── shared/                         # Shared UI
│   └── src/main/java/com/nexora/shared/
│       └── theme/
│           ├── NexoraColors.kt
│           └── NexoraTheme.kt
└── features/
    ├── auth/                        # Authentication
    ├── chat/                        # Messaging
    ├── calls/                       # Voice/Video
    ├── stories/                     # Stories
    ├── explore/                     # Social Feed
    ├── profile/                     # User Profile
    └── settings/                    # App Settings
```

---

## Configuration

### Supabase Setup
Project URL: `https://exwrgtbzacmfvkdbcmzx.supabase.co`

Tables created:
- users (13 tables total)
- conversations
- conversation_members
- messages
- message_reactions
- stories
- story_views
- posts
- post_likes
- contacts
- sessions
- device_tokens
- reports

### Authentication
- Email/Password authentication enabled
- No SMS (uses Email Magic Link)

---

## Tech Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Kotlin | 2.2.20 |
| UI | Jetpack Compose | 2026.04.01 |
| Design | Material 3 | 1.3.1 |
| DI | Hilt | 2.59.2 |
| Backend | Supabase | - |
| Database | PostgreSQL | 18.3 |
| Auth | Supabase Auth | - |
| Network | Ktor | 3.1.0 |

---

## Features

### Phase 1 - MVP
- [x] Splash Screen
- [x] Onboarding
- [x] Email Sign Up / Sign In
- [x] Dark Theme
- [x] Chat List
- [x] Messaging

### Phase 2 - Coming Soon
- [ ] Group Chats
- [ ] Voice/Video Calls
- [ ] Stories
- [ ] Social Feed
- [ ] AI Assistant

---

## Build Commands (if you have Java)

```bash
# Build Debug APK
./gradlew assembleDebug

# Build Release AAB
./gradlew bundleRelease

# Clean
./gradlew clean

# Run tests
./gradlew test
```

---

## Support

For issues or questions, create an issue on GitHub.