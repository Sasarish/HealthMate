# HealthMate - Personal Health & Wellness Tracker

This is a simple native Android app to record daily steps, calories burned and water intake.
Built for Mobile Application Development, Coursework 1.
Purpose of this project is to track user's daily health records

**Author:** Sivakumar Sasarish

## Features
- Dashboard with today's totals and daily goal progress bars
- Add, view, edit and delete health records 
- Search records by date
- Input validation on the Add/Edit form
- Dummy records loaded on first launch
- Black and white design with blue (water) and red (calories) accents (Design inspired from my fitnessPal)

## Technologies
- Kotlin, Jetpack Compose (Material 3)
- SQLite (SQLiteOpenHelper)
- MVVM structure with a ViewModel
- Git for version control
  & "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -s Xiaomi 23076RA4BC install -r app\release\app-release.apk
## Project structure
- `data` - HealthRecord model and SQLite database helper
- `viewmodel` - HealthViewModel (state and validation)
- `ui/screens` - Dashboard, Records list, Add/Edit screens
- `ui/components` - reusable SummaryCard and RecordItem
- `ui/theme` - colours and theme

## How to run
1. Open the project in Android Studio.
2. Connect an Android phone with USB debugging on.
3. Press Run.

## Database
Table `health_records`: id (PK), date (text), steps (int), calories (int), water (int, ml)