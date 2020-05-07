package name.seguri.android.lock;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class LockAccessibilityService extends AccessibilityService {
    public static final String ACTION_LOCK = "name.seguri.android.lock.LOCK";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Nothing to do
    }

    @Override
    public void onInterrupt() {
        // Nothing to do
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ACTION_LOCK.equals(intent.getAction())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN);
            }
        }
        return Service.START_STICKY;
    }
}
