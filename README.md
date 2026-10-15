# Smart Expense Tracker

![Android build check](https://github.com/gahejodev/smart-expense-tracker/actions/workflows/android-build.yml/badge.svg)

An offline Android expense tracker built with Kotlin, Jetpack Compose and Room. Transactions and budgets are stored on the device.

## Features

- Add income or expenses with an amount, category, date and optional note.
- See income, spending, balance and budget progress for a selected month.
- Browse previous and future months.
- Delete transactions.
- No account or internet connection required for daily use.

## Build status

The automated **debug APK build passed** on GitHub Actions. This verifies that the project can compile in the configured CI environment. It **does not** verify installation, real-device behaviour, usability or a signed Play Store release.

[View build runs and downloadable artifacts](https://github.com/gahejodev/smart-expense-tracker/actions/workflows/android-build.yml).

After a successful workflow run, open that run's **Artifacts** section and download **smart-expense-debug-apk**. Extract the ZIP to find `app-debug.apk`. This is a debug build for testing, **not a production release**. GitHub may require sign-in to download workflow artifacts; artifacts expire after 30 days.

## Open in Android Studio

1. Clone or download this repository.
2. Open the project directory in Android Studio.
3. Sync Gradle and run the `app` configuration on an emulator or Android device.

This repository does not contain the generated Gradle wrapper files. The CI workflow uses Gradle 8.9 and Java 17 explicitly.

## Tech stack

Kotlin 2.0.21, Jetpack Compose Material 3, Room 2.6.1, KSP and Android Gradle Plugin 8.7.3.

## Limits and privacy

- Amounts are whole Pakistani rupees (PKR); decimals are not supported.
- Data is saved in the app's local Room database. There is no cloud sync, export or backup.
- Clearing the app's data or uninstalling it may delete saved transactions.
- No real-device test or Play Store release verification has been recorded yet.

## Manual test checklist

- Install and open the debug APK.
- Add one income entry and one expense entry.
- Verify the monthly totals and balance.
- Set a budget and check the progress indicator.
- Change months and confirm the displayed entries update.
- Delete an entry and check the totals again.
- Close and reopen the app to check that records persist.

## License

MIT. See [LICENSE](LICENSE).
