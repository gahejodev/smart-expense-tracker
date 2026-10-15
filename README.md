# Smart Expense Tracker

An offline-first Android expense tracker written in **Kotlin + Jetpack Compose**.

## Features
- Add income and expense transactions
- Category, amount, date and notes
- Monthly income, spending and balance
- Set a monthly expense budget and track progress
- Filter transactions by month
- Delete transactions
- Local persistence with Room (no account, no internet)

## Open in Android Studio
1. Clone or download this repository.
2. Open the project folder in Android Studio.
3. Use JDK 17 and let Gradle sync dependencies.
4. Run the `app` configuration on an emulator or Android device.

This repository intentionally does **not** include generated Gradle wrapper binaries. If your Android Studio requires a wrapper, generate one using an installed Gradle distribution (`gradle wrapper --gradle-version 8.10.2`), or use Android Studio's Gradle setup.

## Tech
Kotlin, Jetpack Compose Material 3, Room, KSP, Android Gradle Plugin 8.7.3, Kotlin 2.0.21.

## Notes
Amounts are stored as whole Pakistani rupees (PKR); fractional amounts are not supported. No cloud sync or backup is included. The project has not yet been compiled or tested on a device in this session.

MIT licensed.