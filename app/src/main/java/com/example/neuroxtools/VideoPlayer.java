package com.example.neuroxtools;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent; // Imported for routing
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class VideoPlayer extends AppCompatActivity {

    static final int PERMISSION_REQUEST_CODE = 200;

    VideoView vv;
    TextView tv;
    MaterialButton btnNext, btnPrev, btnBack;

    final ArrayList<VideoModel> videoList = new ArrayList<>();
    int currentIndex = 0;
    MediaController mediaController;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        tv = findViewById(R.id.tvVideoTitle);
        vv= findViewById(R.id.videoView);
        btnNext = findViewById(R.id.btnNextVideo);
        btnPrev = findViewById(R.id.btnPrevVideo);
        btnBack = findViewById(R.id.btnBack);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(vv);
        vv.setMediaController(mediaController);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(VideoPlayer.this, Application.class);
                startActivity(intent);
                finish();
            });
        }
        if (checkPermission()) {
            loadLocalVideoFiles();
        } else {
            requestPermission();
        }
        setupNavigationControls();
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_VIDEO}, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLocalVideoFiles();
            } else {
                Toast.makeText(this, "Permission Denied! Cannot list device videos.", Toast.LENGTH_LONG).show();
                tv.setText("Permission Required");
            }
        }
    }
    private void loadLocalVideoFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sortOrder = MediaStore.Video.Media.TITLE + " ASC";

        String[] projection = {
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.TITLE
        };

        Cursor cursor = contentResolver.query(uri, projection, null, null, sortOrder);

        if (cursor != null) {
            videoList.clear();
            while (cursor.moveToNext()) {
                VideoModel videoData = new VideoModel(
                        cursor.getString(0),
                        cursor.getString(1)
                );
                videoList.add(videoData);
            }
            cursor.close();
        }
        if (!videoList.isEmpty()) {
            playVideo(currentIndex);
        } else {
            tv.setText("No Video Files Found");
        }
    }
    private void playVideo(int index) {
        if (videoList.isEmpty()) return;

        VideoModel currentVideo = videoList.get(index);
        tv.setText(currentVideo.getTitle());
        vv.setVideoPath(currentVideo.getPath());
        vv.requestFocus();
        vv.start();
    }

    private void setupNavigationControls() {
        btnNext.setOnClickListener(v -> {
            if (videoList.isEmpty()) return;
            currentIndex = (currentIndex + 1) % videoList.size();
            playVideo(currentIndex);
        });

        btnPrev.setOnClickListener(v -> {
            if (videoList.isEmpty()) return;
            currentIndex = (currentIndex - 1 < 0) ? videoList.size() - 1 : currentIndex - 1;
            playVideo(currentIndex);
        });
        vv.setOnCompletionListener(mp -> {
            if (!videoList.isEmpty()) {
                currentIndex = (currentIndex + 1) % videoList.size();
                playVideo(currentIndex);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vv!= null && vv.isPlaying()) {
            vv.pause();
        }
    }
}