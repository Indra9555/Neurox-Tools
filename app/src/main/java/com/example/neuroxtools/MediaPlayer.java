package com.example.neuroxtools;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MediaPlayer extends AppCompatActivity {

    static final int PERMISSION_REQUEST_CODE = 999;

    TextView tv1, tvTrackArtist, tvCurrentTime, tvTotalTime;
    SeekBar sb;
    MaterialButton btnPlayPause;

    private android.media.MediaPlayer mediaPlayer;
    private Handler seekHandler = new Handler();
    private boolean isPrepared = false;
    private ArrayList<Uri> songUriList = new ArrayList<>();
    private ArrayList<String> songTitleList = new ArrayList<>();
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        tv1= findViewById(R.id.tvTrackTitle);
        tvTrackArtist = findViewById(R.id.tvTrackArtist);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        sb= findViewById(R.id.playerSeekBar);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        MaterialButton btnPrevious = findViewById(R.id.btnPrevious);
        MaterialButton btnNext = findViewById(R.id.btnNext);
        Button btnPlayerBack = findViewById(R.id.btnPlayerBack);

        tv1.setText("Checking permissions...");
        tvTrackArtist.setText("");

        if (btnPlayerBack != null) {
            btnPlayerBack.setOnClickListener(v -> finish());
        }

        btnPlayPause.setOnClickListener(v -> togglePlayPause());
        btnNext.setOnClickListener(v -> playNextSong());
        btnPrevious.setOnClickListener(v -> playPreviousSong());

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null && isPrepared) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTimeMetrics(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        checkStoragePermission();
    }

    private void checkStoragePermission() {
        String permissionTarget;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionTarget = Manifest.permission.READ_MEDIA_AUDIO;
        } else {
            permissionTarget = Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permissionTarget) == PackageManager.PERMISSION_GRANTED) {
            loadLocalAudioFiles();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permissionTarget}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLocalAudioFiles();
            } else {
                tv1.setText("Permission Denied");
                Toast.makeText(this, "Storage permission is required to fetch MP3 files!", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void loadLocalAudioFiles() {
        Uri collectionUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            collectionUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
        } else {
            collectionUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

        try (Cursor cursor = getContentResolver().query(collectionUri, projection, selection, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
                int titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
                int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);

                do {
                    long id = cursor.getLong(idColumn);
                    String title = cursor.getString(titleColumn);
                    String artist = cursor.getString(artistColumn);
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    songUriList.add(contentUri);
                    songTitleList.add(title + " - " + artist);

                } while (cursor.moveToNext());
            }
        }

        if (!songUriList.isEmpty()) {
            setupTrackSelection(currentSongIndex);
        } else {
            tv1.setText("No MP3 files found on your device!");
        }
    }

    // Step 3: Initialize core media player stack point referencing local path data pointers
    private void setupTrackSelection(int index) {
        isPrepared = false;
        btnPlayPause.setEnabled(false);
        btnPlayPause.setText("LOADING");

        String currentSongInfo = songTitleList.get(index);
        tv1.setText(currentSongInfo.split(" - ")[0]);
        tvTrackArtist.setText(currentSongInfo.contains(" - ") ? currentSongInfo.split(" - ")[1] : "Unknown");

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = new android.media.MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );

        try {
            // Set data source to our device file Uri path
            mediaPlayer.setDataSource(getApplicationContext(), songUriList.get(index));
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                isPrepared = true;
                btnPlayPause.setText("PLAY");
                btnPlayPause.setEnabled(true);
                sb.setMax(mediaPlayer.getDuration());
                sb.setProgress(0);
                tvCurrentTime.setText("0:00");
                tvTotalTime.setText(formatTimeMetrics(mediaPlayer.getDuration()));
            });

            mediaPlayer.setOnCompletionListener(mp -> playNextSong());

        } catch (IOException e) {
            Toast.makeText(this, "Error loading chosen file pathway segment.", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePlayPause() {
        if (mediaPlayer == null || !isPrepared) return;

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setText("PLAY");
        } else {
            mediaPlayer.start();
            btnPlayPause.setText("PAUSE");
            updateSeekBarTracker();
        }
    }

    private void playNextSong() {
        if (songUriList.isEmpty()) return;
        currentSongIndex = (currentSongIndex + 1) % songUriList.size();
        setupTrackSelection(currentSongIndex);
    }

    private void playPreviousSong() {
        if (songUriList.isEmpty()) return;
        currentSongIndex = (currentSongIndex - 1 + songUriList.size()) % songUriList.size();
        setupTrackSelection(currentSongIndex);
    }

    private final Runnable seekUpdater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPos = mediaPlayer.getCurrentPosition();
                sb.setProgress(currentPos);
                tvCurrentTime.setText(formatTimeMetrics(currentPos));
                seekHandler.postDelayed(this, 1000);
            }
        }
    };

    private void updateSeekBarTracker() {
        seekHandler.post(seekUpdater);
    }

    private String formatTimeMetrics(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setText("PLAY");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            seekHandler.removeCallbacks(seekUpdater);
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}