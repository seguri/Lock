package name.seguri.android.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;
    private static final String EMUI_LOCK_PKG = "com.android.systemui";
    private static final String EMUI_LOCK_CLS = "com.huawei.keyguard.onekeylock.OneKeyLockActivity";

    private ComponentName mCN;
    private DevicePolicyManager mDPM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCN = new ComponentName(this, MainReceiver.class); // Receiver, not Activity!
        mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

        if (isHuaweiNougat()) {
            launchEmuiLockActivity();
            finish();
        } else if (isAdminActive()) {
            lock();
            finish();
        } else {
            enableAppAsAdministrator();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        i("[onActivityResult] requestCode=%d resultCode=%d resultCodeString=%s", requestCode, resultCode, getResultString(resultCode));
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN && resultCode == RESULT_OK) {
            lock();
        }
        finish();
    }

    private static boolean isHuaweiNougat() {
        return Build.MANUFACTURER.equalsIgnoreCase("huawei")
                && Build.VERSION.SDK_INT == Build.VERSION_CODES.N;
    }

    private boolean isAdminActive() {
        return mDPM.isAdminActive(mCN);
    }

    private void lock() {
        i("[lock] lockNow");
        mDPM.lockNow();
    }

    private void enableAppAsAdministrator() {
        i("[enableAppAsAdministrator] startActivityForResult: requestCode=%d", REQUEST_CODE_ENABLE_ADMIN);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCN);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.receiver_expl));
        startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
    }

    private String getResultString(final int resultCode) {
        switch (resultCode) {
            case RESULT_CANCELED: return "RESULT_CANCELED";
            case RESULT_FIRST_USER: return "RESULT_FIRST_USER";
            case RESULT_OK: return "RESULT_OK";
            default: return "UNKNOWN";
        }
    }

    private void launchEmuiLockActivity() {
        i("[launchEmuiLockActivity] startActivity");
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(EMUI_LOCK_PKG, EMUI_LOCK_CLS));
        startActivity(intent);
    }

    private static void i(final String s) {
        Log.i(TAG, s);
    }

    private static void i(final String fmt, final Object... args) {
        i(String.format(fmt, args));
    }
}
