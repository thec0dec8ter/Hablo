package dev.thec0dec8ter.hablo;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import dev.thec0dec8ter.hablo.adapter.GalleryAdapter;
import dev.thec0dec8ter.hablo.model.Media;

public class GalleryActivity extends AppCompatActivity {
    private final int GALLERY_REQUEST_CODE = 1002;

    private ActivityResultLauncher<Intent> galleryResultLauncher;

    private RecyclerView mediaRecycler;
    private GalleryAdapter galleryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        galleryResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                        }
                    }
                });

        mediaRecycler = findViewById(R.id.media_recycler);

        galleryAdapter = new GalleryAdapter(this);
        mediaRecycler.setAdapter(galleryAdapter);

        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == GALLERY_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getMedia();
        }
    }

    private void requestPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        }else {
            getMedia();
        }
    }

    public void getMedia(){
        Uri queryUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        // Get relevant columns for use later.
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME
        };

        // Return only video and image metadata.
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                + " OR "
                + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;

        CursorLoader cursorLoader = new CursorLoader(
                this,
                queryUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME + " DESC" // Sort order.
        );

        Cursor cursor = cursorLoader.loadInBackground();


        assert cursor != null;
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
        int bucketColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME);
        int typeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            long id = cursor.getLong(idColumn);
            String bucketName = cursor.getString(bucketColumn);
            String type;
            Uri contentUri;
            if(cursor.getInt(typeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE){
                contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                type = "image";

            }else {
                contentUri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                type = "video";
            }

            Media media = new Media();
            galleryAdapter.addMedia(media);
        }

    }

    public void openGalleryApp(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST_CODE);
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galleryResultLauncher.launch(intent);
        }
    }
}