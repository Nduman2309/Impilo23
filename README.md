# 🌿 IMPILO23 SA — South African Health & Harvest Hub
> **Final Portfolio of Evidence (PoE) Submission Release (`v1.0-final-poe`)**
> *A modern, nature-inspired South African health & gardening lifestyle application built with Material Design 3, real-time cloud synchronization, cryptographic security, and custom 3D motion physics.*

---

##  Demonstration Video & Submission Evidence

-  **Professional Demonstration Video (YouTube Unlisted)**: [Watch Demonstration Video](https://youtu.be/yBSBGuG6j28?si=NTBhUT5JElkbROYF)
- **Alternative Video Link (Google Drive)**: [Watch on Google Drive](https://drive.google.com/file/d/1ASR8GXBpMRu60s_iFBE9t2Fvt-feCYI0/view?usp=drivesdk)
-  **GitHub Tagged Release**: `v1.0-final-poe`
-  **Signed Executable APK**: `app-debug.apk` (Generated via GitHub Actions CI/CD Pipeline)

---

##  Part 3 Release Notes & Innovative Feature Updates

This final PoE release builds upon the prototype foundation to deliver a fully integrated, commercial-grade graphic experience featuring **custom 3D animated components**, **real-time cloud state synchronization**, and **interactive wellness and harvest hubs**.

###  Major Updates Since Prototype:

#### 1. 🎨 Material Design 3 Visual & Palette Overhaul
- **Synced Design Tokens**: Soft Sage Green (`#E8F5E9`), Deep Forest Green (`#2D6A4F` / `#1B4332`), Warm Terracotta (`#FFF3E0` text `#E65100`), and Sky Water Blue (`#38BDF8`).
- **Dark Mode Support**: Complete dark sage theme (`res/values-night/themes.xml`) maintaining calm nature aesthetic (`#121B14`, `#1B261D`).
- **Custom Vector Icon Suite**: Handcrafted vectors for leaves, water drops, flames, stars, bells, meal icons, workouts, flower beds, and sparkles.

#### 2.  Custom 3D Motion & Animated Physics Suite
- **`ThreeDLeafParticleView.kt`**: Hardware-accelerated custom Canvas view rendering gently floating and 3D rotating leaf particles (Y-axis perspective tilt) across upper arched headers.
- **`DimensionalProgressRing.kt`**: Multi-layered 3D circular progress view with radial gradient depth, soft drop shadow, and animated sweep fill (`75% of 2.5L target reached`).
- **`BreathingCardView.kt`**: MaterialCardView subclass introducing a subtle 3D breathing float elevation animation (`2dp` to `6dp` pulsing).
- **`ActivityBarChartView.kt`**: Weekly activity vertical bar chart (`M T W T F S S`) with rounded forest green columns.
- **3D Micro-Interactions**:
  - Tapping garden tasks on Dashboard triggers a **3D checkmark pop animation**.
  - Watering crops in Garden Hub triggers a **3D growth scale pop & water drop particle effect**.
  - Completing challenges triggers an **animated 3D XP crystal float burst**.

#### 3.  Animated Success & Congratulations Dialogs
- **Animated Green Tick Success Popup (`AnimatedSuccessDialog.kt`)**: Scale-overshoot bounce checkmark popup for:
  - **Account Creation**: *"You have successfully created an account"*.
  - **Sign In / Google SSO**: *"You have successfully signed in"*.
- **Animated Congratulations Popup (`AnimatedCongratsDialog.kt`)**: Celebratory dialog displaying headline *"Congratulations! 🎉"*, task title, star badge, and reward tag (*"+50 XP Granted!"*).

#### 4. Real-time Total XP System & Dynamic Streak Tracker
- **Centralized `XpManager.kt`**: Single source of truth managing total XP in `SharedPreferences` and syncing with Firebase (`users/{uid}/totalXp`). Real-time observers dynamically update `tvXpVal` and level badges across all screens.
- **`StreakManager.kt`**: Dynamically calculates and persists consecutive daily app activity (`🔥 8 Days` / `12 Day Streak`).

#### 5.  Interactive Add Crop Wizard & Web Image Matcher
- **Add Crop Wizard (`dialog_add_crop_wizard.xml`)**: Modal form accepting Crop Name, Patch Location, Growth Stage, and Harvest Days.
- **`CropImageResolver.kt`**: Automatically matches entered crop names (Tomatoes, Spinach, Carrots, Roses, Mint, Maize, Peppers, Lavender, Herbs) to high-resolution web picture URLs and botanical names (*Solanum lycopersicum*, *Rosa rubiginosa*, etc.).
- **Crop Details Sheet (`dialog_crop_details.xml`)**: In-depth inspection modal displaying crop picture, botanical name, South African planting zone (*Zone 9b • South Africa*), growth stage, sunlight needs, soil pH, and watering schedule.

#### 6.  Interactive Meal & Food Activity Logger (Health Hub)
- **`dialog_log_activity.xml`**: Modal dialog allowing users to log meals/nutrition (*"Oatmeal & Berries"*, *"+350 Kcal"*) or workout sessions.
- Dynamically inserts new cards into `Recent Logs`, updates `Kcal Burned` totals, and syncs to Firebase.

#### 7.  Masterclass Plant Care Guide & Smart Watering Calculator
- **6 Interactive Chapters**: Soil & Compost, Smart Watering, Sunlight & Microclimates, Eco Pest Control, Pruning & Propagation, Harvest & Storage.
- **Real-time Search Filter**: Filter care guides dynamically by typing keywords.
- **Smart Watering Calculator**: Interactive crop selector calculating watering frequency for Greens, Herbs, and Succulents.

---

##  Core Technical & Security Architecture

### 1. Single Sign-On (SSO) & Firebase Authentication
- Features Google Single Sign-On (`btnGoogleSso`) alongside salted SHA-256 email/password login and password reset link dispatch (`sendPasswordResetEmail`).
- Brand-new SSO users automatically have their profile nodes initialized in Firebase Realtime Database (`users/{uid}`).

### 2. Cryptographic Security Engine (`PasswordHasher.kt`)
- Employs **SHA-256 digest hashing** combined with **unique, independent 16-byte random salts** generated via `SecureRandom` per user to prevent rainbow table attacks.

### 3. Offline Disk Persistence & Cloud Sync
- Enabled via `FirebaseDatabase.getInstance().setPersistenceEnabled(true)` in `ImpiloApp.kt`.
- Allows health logs, water intake, and garden crops to be written offline and automatically synchronized with Firebase when connection is restored.

### 4. Multi-Language Support Readiness
- Configured in `SettingsActivity` and `strings.xml` for **English**, **isiZulu**, **Sesotho**, and **Afrikaans**.

### 5. Asynchronous REST API Telemetry & Health Finder
- Utilizes `Retrofit` & `Gson` (`RetrofitClient.kt`) to query live global health statistics from `disease.sh`.
- `HealthFinderActivity` maps regional health facility locations across major South African metropolitan areas (Cape Town, Johannesburg, Durban, Pretoria).

---

##  Automated CI/CD Pipeline (GitHub Actions)

Continuous Integration & Automated Testing is driven natively by GitHub Actions via `.github/workflows/build.yml`:

- **Virtual Environment**: Runs on `ubuntu-latest` with Java Development Kit 17 (`JDK 17`).
- **Automated Unit Testing**: Executes core JVM unit test suites (`SecurityValidationTest.kt`, `ValidationUtils`) on every push/pull request.
- **Automated Artifact Generation**: Compiles and uploads signed debug executable packages (`app-debug.apk`) as pipeline artifacts.

---

##  AI Usage Write-Up & Citation

### Summary & Quantification
In accordance with academic integrity guidelines, AI tools (Gemini / Claude / Copilot) were utilized as an advisory and assistance tool, accounting for approximately **10% of total project effort**. The remaining **90% of architectural engineering, Material Design 3 component styling, 3D Canvas math, Firebase database structuring, and video demonstration voiceover** were executed independently by the developer.

### Specific Contribution Areas (10% Total):
1. **GitHub Actions Workflow Syntax (4%)**: Assisted in formatting the YAML matrix structure for `.github/workflows/build.yml` to establish JDK 17 environments and run automated Gradle test tasks.
2. **3D Matrix Rotation Mathematics (3%)**: Consulted on standard Android `android.graphics.Camera` Y-axis rotation matrix operations inside `ThreeDLeafParticleView.kt` for perspective depth calculations.
3. **Regex Boundary Validation Patterns (3%)**: Refactored email and numeric validation patterns in `ValidationUtils.kt` to ensure compatibility across JVM unit tests without needing Android framework mocking.

---

##  Code Attribution & References

- **Android Jetpack Compose & Material 3**: [Android Developer Documentation](https://developer.android.com/jetpack/compose)
- **Firebase Android SDK & Realtime Database**: [Firebase Docs](https://firebase.google.com/docs/android/setup)
- **Retrofit HTTP Framework**: [Square Open Source](https://square.github.io/retrofit/)
- **GitHub Actions Android Workflow**: [Automated Build Android App Action](https://github.com/marketplace/actions/automated-build-android-app-with-github-action)

---
*IMPILO23 SA — Nurturing Health & Nature.*
