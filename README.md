# IMPILO23 — Secure Personal Healthcare Tracker Prototype

IMPILO23 is a comprehensive, client-safe health tracking platform engineered for high-integrity vital status logging, personal wellness target tracking, and asynchronous regional public health data metrics acquisition. 

This repository houses the fully functional Part 2 Prototype constructed for the OPSC6312 Android Software Architecture curriculum.

---

## 📱 Demonstration Video & Assets

- **Demonstration Video Link:** [Click Here to View Video Presentation (YouTube Unlisted / Professional Voiceover)](https://www.youtube.com/)
- **Voiceover Scope:** Includes detailed visual data validation cross-checks covering the underlying SQLite persistence engine, Room data mapping definitions, Retrofit request interceptor states, and live API responses.

---

## 🚀 Key Feature Implementations

1. **Secure Cryptographic Authentication Engine**
   - Implements strict onboarding forms equipped with input boundary filters.
   - Encrypts account codes by employing **SHA-256 hash digests mixed with independent random salts per user** via a custom platform-safe hex encoder, preventing standard data leakage.
2. **Interactive Daily Health Analytics Log**
   - Facilitates real-time aggregation of multi-layered categories: Water Intake values (ml), Measured Body Mass (kg), Heart Rate metrics (bpm), and Blood Pressure status (Sys/Dia).
3. **Asynchronous REST API Integration**
   - Utilizes **Retrofit & Gson** to connect dynamically to the `disease.sh` health statistics REST server.
   - Asynchronously queries country names and populates complex responses securely without thread lock or UI freezing.
4. **Resilient Boundary Error Recovery**
   - Uses strict ViewBinding references combined with numerical pattern interceptors to safely handle null, negative, or alphanumeric format inputs without crashing.

---

## 🏛️ Architectural Framework Design

The system relies fully on the clean separation of concerns:
- **Presentation Layer (`com.example.impilo23.ui`)**: Manages Activities powered by reactive Material 3 View Components.
- **Business/Security Layer (`com.example.impilo23.crypto` / `.util`)**: Directs data validation structures and cryptographic encryption routines.
- **Data Persistence Layer (`com.example.impilo23.data`)**: Directs multi-entity Room database mappings with safe thread operations.
- **Remote Gateway Layer (`com.example.impilo23.api`)**: Manages Retrofit callback execution flows.

---

## 🛠️ Automated CI/CD Pipeline

Continuous Integration is driven natively by **GitHub Actions** via `.github/workflows/build.yml`:
- Installs an automated virtual stack running **JDK 17** on every code check-in or request trigger.
- Auto-runs core unit test suites to enforce code quality metrics.
- Assembles a signed executable debug package (`app-debug.apk`) as a downloadable pipeline artifact.

---

## 📚 Code Attribution & Citations

- **Google Room Database SDK Components:** [Android Developer Documentation](https://developer.android.com/training/data-storage/room)
- **Retrofit HTTP Communication Framework Client:** [Square Open Source Repository](https://square.github.io/retrofit/)
- **CI/CD Pipeline Build Actions:** [GitHub Marketplace Automation Guide](https://github.com/marketplace/actions/automated-build-android-app-with-github-action)
- All algorithms, logging implementations, and design matrices have been explicitly labeled with comprehensive developer code commentary.
