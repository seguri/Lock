package name.seguri.android.lock;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
    private static final String EMUI_LOCK_PKG = "com.android.systemui";
    private static final String EMUI_LOCK_CLS = "com.huawei.keyguard.onekeylock.OneKeyLockActivity";

    private ComponentName mCN;
    private DevicePolicyManager mDPM;
    private AccessibilityManager mAM;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCN = new ComponentName(this, MainReceiver.class); // Receiver, not Activity!
        mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        mAM = (AccessibilityManager)getSystemService(Context.ACCESSIBILITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (isAccessibilityServiceEnabled()) {
                startLockAccessibilityService();
                finish();
            } else {
                enableAppAsAccessibilityService();
            }
        } else if (isHuaweiNougat()) {
            launchEmuiLockActivity();
            finish();
        } else if (isAdminActive()) {
            lockAsDeviceAdmin();
            finish();
        } else {
            enableAppAsAdministrator();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        i("[onActivityResult] requestCode=%d resultCode=%d resultCodeString=%s", requestCode, resultCode, getResultString(resultCode));
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN && resultCode == RESULT_OK) {
            lockAsDeviceAdmin();
        }
        finish();
    }

    private static boolean isHuaweiNougat() {
        return Build.MANUFACTURER.equalsIgnoreCase("huawei")
                && Build.VERSION.SDK_INT == Build.VERSION_CODES.N;
    }

    /**
     * Go through all enabled accessibility services, looking for ours.
     */
    @TargetApi(Build.VERSION_CODES.N)
    private boolean isAccessibilityServiceEnabled() {
        final boolean[] enabled = {false};
        mAM.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK).forEach(enabledAccessibilityService -> {
            ServiceInfo enabledServiceInfo = enabledAccessibilityService.getResolveInfo().serviceInfo;
            i("[isAccessibilityServiceEnabled] packageName='%s' serviceName='%s'", enabledServiceInfo.packageName, enabledServiceInfo.name);
            if (enabledServiceInfo.packageName.equals(getPackageName()) && enabledServiceInfo.name.equals(LockAccessibilityService.class.getName())) {
                enabled[0] = true;
            }
        });
        return enabled[0];
    }

    private boolean isAdminActive() {
        return mDPM.isAdminActive(mCN);
    }

    private void lockAsDeviceAdmin() {
        i("[lockAsDeviceAdmin] lockNow");
        mDPM.lockNow();
    }

    private void enableAppAsAdministrator() {
        i("[enableAppAsAdministrator] startActivityForResult: requestCode=%d", REQUEST_CODE_ENABLE_ADMIN);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCN);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.receiver_expl));
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    private void enableAppAsAccessibilityService() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.enable_accessibility_service)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                })
                .show();
    }

    private String getResultString(final int resultCode) {
        switch (resultCode) {
            case RESULT_CANCELED: return "RESULT_CANCELED";
            case RESULT_FIRST_USER: return "RESULT_FIRST_USER";
            case RESULT_OK: return "RESULT_OK";
            default: return "UNKNOWN";
        }
    }

    private void startLockAccessibilityService() {
        Intent intent = new Intent(LockAccessibilityService.ACTION_LOCK, null, this, LockAccessibilityService.class);
        startService(intent);
    }

    private void launchEmuiLockActivity() {
        i("[launchEmuiLockActivity] startActivity");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(EMUI_LOCK_PKG, EMUI_LOCK_CLS));
        startActivity(intent);
    }

    private static void i(final String fmt, final Object... args) {
        Log.i(TAG, String.format(fmt, args));
    }
}
