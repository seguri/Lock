# Lock

A very simple and minimal app to lock the screen. It tries whenever possible to keep fingerprint unlock available.

You can now set any tap, double tap or long home press actions to immediately lock the screen, if your launcher allows you to configure custom gestures.

## Description

Its behavior is very simple: on first run it will require the necessary permission according to the Android version you're running:

- On Android 9+ devices, it will ask to be enabled as an accessibility service;
- On Huawei devices running Android Nougat, it will immediately lock the screen, no permission required;
- On other devices, the app will ask to be enabled as a Device Admin; fingerprint unlock will not be available.

After the necessary permission has been enabled, the app will immediately lock the screen the next time you open it.

## Accessibility Service configuration

Here are some screenshots that explain how to configure this app as an Accessibility Service on Android 9+ devices:

- [Huawei P20 Lite](docs/ane-lx1.jpg)
- [Xiaomi Mi 9T](docs/davinci_eea.jpg)

## Download

This app is available on F-Droid:

[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/name.seguri.android.lock)

## Changelog

### 1.4.0

It is now possible to use Accessibility Services on Android 9+ devices, so that you can continue using fingerprint unlock.

### 1.3.0

It is now possible to set Lock as an Assistant app (`Settings > Apps > Advanced > Default apps > Assist & voice input`). A long-press on home button will now lock the device wherever you are.

### 1.1.0

I recently updated to Nougat on my Huawei P9 Lite and it broke my app (screen lock by an administrator requires to enter full password or pin).
Thanks to a [XDA user][3], I updated my app to circumvent this.
Now, on Huawei with Nougat and EMUI 5, it doesn't even require to be an administrator.

## About `gradle-wrapper.jar`

This file is a binary blob of executable code that contains [`GradleWrapperMain`](https://github.com/gradle/gradle/blob/master/subprojects/wrapper/src/main/java/org/gradle/wrapper/GradleWrapperMain.java), the Java class invoked by `gradlew` and responsible for the correct setup of the Gradle wrapper in this project. A [Github action](.github/workflows/gradle-wrapper-validation.yml) has been added as explained [here](https://github.com/marketplace/actions/gradle-wrapper-validation#the-gradle-wrapper-problem-in-open-source) to avoid the injection of malicious code in this file. Please check also [this page](https://docs.gradle.org/current/userguide/gradle_wrapper.html#wrapper_checksum_verification) if you want to manually verify its integrity.

## Uninstalling

[How to remove an app with active device admin enabled on Android?][2]


[1]: https://github.com/ligi/FAST
[2]: https://stackoverflow.com/questions/5387582/how-to-remove-an-app-with-active-device-admin-enabled-on-android
[3]: https://forum.xda-developers.com/showpost.php?p=70712987&postcount=5
