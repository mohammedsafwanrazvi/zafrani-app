# Zafrani — Android project

Wraps https://zafranizeera.com (package `com.zafranizeera.zafrani`, v1.0.0).

## Build locally

1. Install Node 20, JDK 17 and Android Studio.
2. `npm install && npx cap add android && cp -rf android-overrides/. android/ && npx cap sync android`
3. `sh scripts/generate-keystore.sh` — creates release.jks and prints your SHA-256 fingerprint.
4. Paste the fingerprint into `.well-known/assetlinks.json` and upload it to https://zafranizeera.com/.well-known/assetlinks.json
5. `npm run build:apk` (test APK) and `npm run build:aab` (Play Store bundle).

Outputs: android/app/build/outputs/apk/release and bundle/release.

Pushing to GitHub builds both files automatically and publishes them as a release with direct download links.

**Keep release.jks and keystore.properties safe. Losing them means you cannot update your app on Play Store.**
