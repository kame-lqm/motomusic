package com.kame.motomusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import com.kame.motomusic.R;
import com.kame.motomusic.activity.OnlineMusicActivity;
import com.kame.motomusic.adapter.PlaylistAdapter;
import com.kame.motomusic.application.AppCache;
import com.kame.motomusic.constants.Extras;
import com.kame.motomusic.constants.Keys;
import com.kame.motomusic.enums.LoadStateEnum;
import com.kame.motomusic.model.SongListInfo;
import com.kame.motomusic.utils.NetworkUtils;
import com.kame.motomusic.utils.ViewUtils;
import com.kame.motomusic.utils.binding.Bind;

/**
 * 在线音乐
 * Created by wcy on 2015/11/26.
 */
public class PlaylistFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.lv_playlist)
    private ListView lvPlaylist;
    @Bind(R.id.ll_loading)
    private LinearLayout llLoading;
    @Bind(R.id.ll_load_fail)
    private LinearLayout llLoadFail;

    private List<SongListInfo> mSongLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!NetworkUtils.isNetworkAvailable(getContext())) {
            ViewUtils.changeViewState(lvPlaylist, llLoading, llLoadFail, LoadStateEnum.LOAD_FAIL);
            return;
        }

        mSongLists = AppCache.getSongListInfos();
        if (mSongLists.isEmpty()) {
            String[] titles = getResources().getStringArray(R.array.online_music_list_title);
            String[] types = getResources().getStringArray(R.array.online_music_list_type);
            for (int i = 0; i < titles.length; i++) {
                SongListInfo info = new SongListInfo();
                info.setTitle(titles[i]);
                info.setType(types[i]);
                mSongLists.add(info);
            }
        }
        PlaylistAdapter adapter = new PlaylistAdapter(mSongLists);
        lvPlaylist.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        lvPlaylist.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongListInfo songListInfo = mSongLists.get(position);
        Intent intent = new Intent(getContext(), OnlineMusicActivity.class);
        intent.putExtra(Extras.MUSIC_LIST_TYPE, songListInfo);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int position = lvPlaylist.getFirstVisiblePosition();
        int offset = (lvPlaylist.getChildAt(0) == null) ? 0 : lvPlaylist.getChildAt(0).getTop();
        outState.putInt(Keys.PLAYLIST_POSITION, position);
        outState.putInt(Keys.PLAYLIST_OFFSET, offset);
    }

    public void onRestoreInstanceState(final Bundle savedInstanceState) {
        lvPlaylist.post(new Runnable() {
            @Override
            public void run() {
                int position = savedInstanceState.getInt(Keys.PLAYLIST_POSITION);
                int offset = savedInstanceState.getInt(Keys.PLAYLIST_OFFSET);
                lvPlaylist.setSelectionFromTop(position, offset);
            }
        });
    }
}
