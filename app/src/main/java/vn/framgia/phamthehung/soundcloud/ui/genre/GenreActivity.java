package vn.framgia.phamthehung.soundcloud.ui.genre;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.framgia.phamthehung.soundcloud.R;
import vn.framgia.phamthehung.soundcloud.data.model.Genre;
import vn.framgia.phamthehung.soundcloud.data.model.Track;
import vn.framgia.phamthehung.soundcloud.data.repository.TrackRepository;
import vn.framgia.phamthehung.soundcloud.data.source.remote.TrackRemoteDataSource;
import vn.framgia.phamthehung.soundcloud.service.PlayMusicInterface;
import vn.framgia.phamthehung.soundcloud.service.PlayMusicService;
import vn.framgia.phamthehung.soundcloud.ui.detailtrack.DetailTrackFragment;
import vn.framgia.phamthehung.soundcloud.ui.playmusic.PlayMusicActivity;

public class GenreActivity extends AppCompatActivity implements GenreContract.View,
        View.OnClickListener, GenreAdapter.OnItemClickListenerTracks {

    public static final String GENRE_KEY = "GENRE_KEY";
    private ImageView mImageGenre;
    private TextView mTextShuffle;
    private Genre mGenre;
    private GenrePresenter mGenrePresenter;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerTracks;
    private GenreAdapter mGenreAdapter;
    private List<Track> mTracks;
    private PlayMusicService mPlayMusicService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        initActionBar();
        initView();
        getDataIntent();
        initPresenter();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayMusicService.LocalBinder binder = (PlayMusicService.LocalBinder) service;
            mPlayMusicService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    public static Intent getIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(GENRE_KEY, genre);
        intent.putExtras(bundle);
        return intent;
    }

    public void getDataIntent() {
        Bundle bundle = getIntent().getExtras();
        mGenre = bundle.getParcelable(GENRE_KEY);
        mTextShuffle.setText(mGenre.getName());
        mImageGenre.setImageResource(mGenre.getImage());
    }

    public void initView() {
        mImageGenre = findViewById(R.id.image_genres);
        mTextShuffle = findViewById(R.id.text_shuffle_play);
        mProgressBar = findViewById(R.id.progress_tracks);
        mRecyclerTracks =findViewById(R.id.recycler_tracks);
        Intent intent = new Intent(this, PlayMusicService.class);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_genre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.null_string);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void loadTracksSuccess(List<Track> track) {
        mTracks = track;
        mProgressBar.setVisibility(View.GONE);
        mRecyclerTracks.setVisibility(View.VISIBLE);
        mGenreAdapter = new GenreAdapter(this, mTracks,this);
        mRecyclerTracks.setAdapter(mGenreAdapter);
        mGenreAdapter.notifyDataSetChanged();
        mPlayMusicService.setTracks(mTracks);
    }

    @Override
    public void loadTrackFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void initPresenter() {
        TrackRepository trackRepository = TrackRepository
                .getsInstance(TrackRemoteDataSource.getInstance());
        mGenrePresenter = new GenrePresenter(trackRepository, this);
        mGenrePresenter.getTracks(mGenre);
    }

    @Override
    public void onClick(View v) {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onTrackClick(Track track) {
        mPlayMusicService.setTrack(track);
        Intent intent1 = new Intent(this, PlayMusicService.class);
        this.startService(intent1);
        Intent intent = PlayMusicActivity.getIntent(GenreActivity.this);
        startActivity(intent);
    }

    @Override
    public void onMoreClick(Track track) {
        DetailTrackFragment detailTrackFragment = DetailTrackFragment.newInstance(track);
        detailTrackFragment.show(getSupportFragmentManager(), detailTrackFragment.getTag());
    }
}
