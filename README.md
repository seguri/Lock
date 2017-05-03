# Lock

## Introduction

Very simple app to lock the screen.

I use it with [FAST][1] launcher to quickly
lock the screen (either clicking the icon or typing "lk") because I miss my previous launcher feature "lock with double
click on home button".
I tried a series of apps that were not comfortable with FAST launcher because
they required too much steps to lock the screen.

Its behavior is very simple: on first run it will ask the required device
administrator permission and then it will lock the screen. On next launches, it
will immediately lock the screen.

**NEW in 1.1.0**: I recently updated to Nougat on my Huawei P9 Lite and it broke my app (screen lock by an administrator requires to enter full password or pin).
Thanks to a [XDA user][3], I updated my app to circumvent this.
Now, on Huawei with Nougat and EMUI 5, it doesn't even require to be an administrator.

## Download

[<img src="https://f-droid.org/badge/get-it-on.png"
      alt="Get it on F-Droid"
      height="80">](https://f-droid.org/app/name.seguri.android.lock)

## Uninstalling

[How to remove an app with active device admin enabled on Android?][2]


[1]: https://github.com/ligi/FAST
[2]: https://stackoverflow.com/questions/5387582/how-to-remove-an-app-with-active-device-admin-enabled-on-android
[3]: https://forum.xda-developers.com/showpost.php?p=70712987&postcount=5
