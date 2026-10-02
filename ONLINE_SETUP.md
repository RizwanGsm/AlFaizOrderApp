# Al-Faiz GitHub Online Control

The app now reads its remote configuration from GitHub instead of Firebase.

Remote file:
https://raw.githubusercontent.com/RizwanGsm/AlFaizOrderApp/main/online/github-config.json

## What can be controlled
- Restaurant WhatsApp number
- Menu items and prices
- Categories
- Deals and deal contents
- Enable/disable menu items
- Remote app shutdown/reactivation

## Change menu/prices
Edit `online/github-config.json` in GitHub and commit the change to `main`. The official Android app reads the updated configuration on its next start.

## Remote shutdown
Change `"appEnabled": true` to `"appEnabled": false` and commit. The official app will show **APP DEACTIVATED** after connecting to GitHub. Change it back to `true` to reactivate.

## Admin panel
The panel source is `admin/index.html`. Workflow: `.github/workflows/pages.yml`.

Enable GitHub Pages in repository Settings → Pages and select **GitHub Actions**. The panel URL will be:
https://RizwanGsm.github.io/AlFaizOrderApp/

The panel displays the current configuration and links to GitHub's authenticated editor.

## Why the panel uses GitHub's editor
GitHub Pages is static hosting. A browser cannot safely write to the repository without exposing a GitHub access token. This design therefore keeps credentials out of the APK and website.

No Firebase SDK or Firebase database is required for the Android app.
