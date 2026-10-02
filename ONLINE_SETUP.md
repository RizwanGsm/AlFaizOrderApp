# Al-Faiz Fast Food Online

The Android app now supports a remote menu/settings backend. It keeps the built-in menu as a fallback and can load menu prices, enabled/disabled items, WhatsApp number, from Firebase Realtime Database.

## Firebase setup
1. Create a Firebase project.
2. Create a **Realtime Database**.
3. Create an **Authentication > Email/Password** admin account.
4. Put the database data from `online/initial-data.json` into the database root.
5. Set database rules so reads are allowed to the customer app and writes require authenticated users.
6. Copy the Firebase Web config into `admin/index.html`.
7. Copy your Realtime Database URL into `ONLINE_DB_URL` in `MainActivity.java`, without the trailing `.json`.
8. Host the `admin` folder on Firebase Hosting, GitHub Pages, or another HTTPS host.

The customer APK reads the database directly. Normal menu/price/WhatsApp changes then take effect for installed apps without rebuilding the APK.

## Security
Do not put a Firebase Admin SDK service-account key in the website or APK. Use Firebase Authentication and Realtime Database rules for admin writes.
