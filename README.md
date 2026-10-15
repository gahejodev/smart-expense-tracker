# Smart Expense Tracker

An Android project for recording everyday income and expenses. The app is written in Kotlin, uses Jetpack Compose for the interface, and stores transactions locally with Room.

## Current features

- Add income and expense entries with a category, date and optional note.
- View monthly income, spending and balance.
- Set a monthly spending budget and check progress.
- Filter transactions by month and delete entries.
- Use the app without signing in or connecting to a cloud service.

## Open the project

1. Clone or download this repository.
2. Open the project folder in Android Studio.
3. Let Android Studio sync the Gradle project.
4. Select the `app` configuration and try building it on an emulator or Android device.

The repository does not include generated Gradle wrapper binaries. If Android Studio cannot run the project as-is, check the Gradle setup before building.

## Tech used

Kotlin, Jetpack Compose Material 3, Room, KSP, Android Gradle Plugin 8.7.3 and Kotlin 2.0.21.

## Known limits

Amounts are whole Pakistani rupees (PKR); decimal amounts are not supported. There is no cloud sync or backup feature. **Build and device testing still need to be completed**, so please do not treat this repository as a verified release build.

## License

MIT. See [LICENSE](LICENSE).