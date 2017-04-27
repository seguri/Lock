package name.seguri.android.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_ENABLE_ADMIN = 1;

    private ComponentName mCN;
    private DevicePolicyManager mDPM;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCN = new ComponentName(this, MainReceiver.class); // Receiver, not Activity!
        mDPM = (DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);

        if (isAdminActive()) {
            lock();
            finish();
        } else {
            enableAppAsAdministrator();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: requestCode=" + requestCode + " resultCode=" + resultCode + " resultCodeString=" + getResultString(resultCode));
        if (requestCode == REQUEST_CODE_ENABLE_ADMIN && resultCode == RESULT_OK) {
            lock();
        }
        finish();
    }

    private boolean isAdminActive() {
        return mDPM.isAdminActive(mCN);
    }

    private void lock() {
        Log.i(TAG, "lockNow");
        mDPM.lockNow();
    }

    private void enableAppAsAdministrator() {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mCN);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.receiver_expl));
        Log.i(TAG, "startActivityForResult: requestCode=" + REQUEST_CODE_ENABLE_ADMIN);
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

    private void flash(final String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();
    }
}
