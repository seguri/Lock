package name.seguri.android.lock;

import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MainReceiver extends DeviceAdminReceiver {
    private final String TAG = "MainReceiver";

    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Log.i(TAG, "onEnabled: action=" + intent.getAction());
    }

    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onDisabled(context, intent);
        Log.i(TAG, "onDisabled: action=" + intent.getAction());
    }
}