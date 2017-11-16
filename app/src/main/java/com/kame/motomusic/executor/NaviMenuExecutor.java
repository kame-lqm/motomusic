package com.kame.motomusic.executor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;

import com.kame.motomusic.R;
import com.kame.motomusic.activity.AboutActivity;
import com.kame.motomusic.activity.MusicActivity;
import com.kame.motomusic.activity.SettingActivity;
import com.kame.motomusic.application.AppCache;
import com.kame.motomusic.service.PlayService;
import com.kame.motomusic.service.QuitTimer;
import com.kame.motomusic.utils.Preferences;
import com.kame.motomusic.utils.ToastUtils;

/**
 * 导航菜单执行器
 * Created by hzwangchenyan on 2016/1/14.
 */
public class NaviMenuExecutor {

    public static boolean onNavigationItemSelected(MenuItem item, MusicActivity activity) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(activity, SettingActivity.class);
                return true;
            case R.id.action_night:
                nightMode(activity);
                break;
            case R.id.action_timer:
                timerDialog(activity);
                return true;
            case R.id.action_exit:
                exit(activity);
                return true;
            case R.id.action_about:
                startActivity(activity, AboutActivity.class);
                return true;
        }
        return false;
    }

    private static void startActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        context.startActivity(intent);
    }

    private static void nightMode(final MusicActivity activity) {
        Preferences.saveNightMode(!Preferences.isNightMode());
        activity.recreate();
    }

    private static void timerDialog(final MusicActivity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(R.string.menu_timer)
                .setItems(activity.getResources().getStringArray(R.array.timer_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int[] times = activity.getResources().getIntArray(R.array.timer_int);
                        startTimer(activity, times[which]);
                    }
                })
                .show();
    }

    private static void startTimer(Context context, int minute) {
        QuitTimer.getInstance().start(minute * 60 * 1000);
        if (minute > 0) {
            ToastUtils.show(context.getString(R.string.timer_set, String.valueOf(minute)));
        } else {
            ToastUtils.show(R.string.timer_cancel);
        }
    }

    private static void exit(MusicActivity activity) {
        activity.finish();
        PlayService service = AppCache.getPlayService();
        if (service != null) {
            service.quit();
        }
    }
}
