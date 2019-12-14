

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by jixiaoyong1995@gmail.com
 * Data: 2018/11/15.
 * Description: 输入法工具类
 */
public class ImmUtils {

    public static void hideImm(Context context, IBinder windowToken) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(windowToken, WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideImm(Activity activity) {
        try {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                boolean result1 = imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                if (!result1) {
                    hideKeyboard(activity);
                }
            }
        } catch (Exception e) {
            hideKeyboard(activity);
        }

    }

    public static void hideImm(Activity activity, Dialog dialog) {
        try {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean result = manager.hideSoftInputFromWindow(dialog.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            hideKeyboard(activity);
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            boolean result = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
