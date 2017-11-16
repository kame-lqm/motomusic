package com.kame.motomusic.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.io.File;

import com.kame.motomusic.R;
import com.kame.motomusic.application.AppCache;
import com.kame.motomusic.executor.DownloadMusicInfo;
import com.kame.motomusic.service.PlayService;
import com.kame.motomusic.utils.ToastUtils;
import com.kame.motomusic.utils.id3.ID3TagUtils;
import com.kame.motomusic.utils.id3.ID3Tags;

/**
 * 下载完成广播接收器
 * Created by hzwangchenyan on 2015/12/30.
 */
public class DownloadReceiver extends BroadcastReceiver {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onReceive(Context context, Intent intent) {
        long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
        DownloadMusicInfo downloadMusicInfo = AppCache.getDownloadList().get(id);
        if (downloadMusicInfo != null) {
            ToastUtils.show(context.getString(R.string.download_success, downloadMusicInfo.getTitle()));

            String musicPath = downloadMusicInfo.getMusicPath();
            String coverPath = downloadMusicInfo.getCoverPath();
            if (!TextUtils.isEmpty(musicPath) && !TextUtils.isEmpty(coverPath)) {
                // 设置专辑封面
                File musicFile = new File(musicPath);
                File coverFile = new File(coverPath);
                if (musicFile.exists() && coverFile.exists()) {
                    ID3Tags id3Tags = new ID3Tags.Builder()
                            .setCoverFile(coverFile)
                            .build();
                    ID3TagUtils.setID3Tags(musicFile, id3Tags, false);
                }
            }

            // 由于系统扫描音乐是异步执行，因此延迟刷新音乐列表
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanMusic();
                }
            }, 1000);
        }
    }

    private void scanMusic() {
        PlayService playService = AppCache.getPlayService();
        if (playService != null) {
            playService.updateMusicList(null);
        }
    }
}
