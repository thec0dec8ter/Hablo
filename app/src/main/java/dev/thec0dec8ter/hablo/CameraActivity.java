package dev.thec0dec8ter.hablo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;

import dev.thec0dec8ter.hablo.adapter.MediaAdapter;
import dev.thec0dec8ter.hablo.model.Media;
import dev.thec0dec8ter.hablo.utility.CameraUtility;

public class CameraActivity extends AppCompatActivity {
    private final int CAMERA_REQUEST_CODE = 1001;

    private CameraUtility cameraUtility;
    private TextureView cameraView;
    private RecyclerView recentRecycler;
    private RecyclerView mediaRecycler;

    private MediaAdapter recentAdapter;
    private MediaAdapter mediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        recentAdapter = new MediaAdapter(this, R.layout.item_thumb);
        mediaAdapter = new MediaAdapter(this, R.layout.item_media);

        cameraView = findViewById(R.id.camera_view);
        recentRecycler = findViewById(R.id.recent_recycler);
        mediaRecycler = findViewById(R.id.media_recycler);

        recentRecycler.setAdapter(recentAdapter);
        mediaRecycler.setAdapter(mediaAdapter);
        startCapturing();
        getMediaFiles();
    }

    public void getMediaFiles() {
        Uri mediaUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.DATE_ADDED
        };

        String selection = new StringBuilder()
                .append(MediaStore.Files.FileColumns.MEDIA_TYPE + " = ")
                .append(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
                .append(" OR ")
                .append(MediaStore.Files.FileColumns.MEDIA_TYPE + " = ")
                .append(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
                .toString();

        CursorLoader cursorLoader = new CursorLoader(
                this,
                mediaUri,
                projection,
                selection,
                null, // Selection args (none).
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"// Sort order.
        );

        Cursor cursor = cursorLoader.loadInBackground();
        if ((cursor != null) && (cursor.moveToFirst())) {
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID);
            int typeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE);
            while (cursor.moveToNext()) {
                Media media = new Media();
                String mediaID = String.valueOf(cursor.getLong(idColumn));
                media.setId(mediaID);
                media.setUri(Uri.withAppendedPath(mediaUri, "" + mediaID).toString());
                if(cursor.getInt(typeColumn) == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE){
                    media.setType("image");
                }else {
                    media.setUri(Uri.withAppendedPath(mediaUri, "" + mediaID).toString());
                    media.setType("video");
                }
                recentAdapter.addMedia(media);
                mediaAdapter.addMedia(media);
            }
        }
    }

    private void startCapturing(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            return;
        }
        cameraUtility = new CameraUtility(this);
        cameraUtility.startBackgroundThread();
        if(cameraView.getSurfaceTexture() != null){
            try {
                cameraUtility.openCamera(cameraView);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }else {
            cameraView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(@NonNull SurfaceTexture surface, int width, int height) {
                    try {
                        cameraUtility.openCamera(cameraView);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSurfaceTextureSizeChanged(@NonNull SurfaceTexture surface, int width, int height) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(@NonNull SurfaceTexture surface) {
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(@NonNull SurfaceTexture surface) {

                }
            });
        }
    }

    public Uri getImageUri(Bitmap bitmap) {
        Uri uri = null;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        boolean isCompressed = bitmap.compress(Bitmap.CompressFormat.JPEG,100, bytes);
        if(isCompressed){
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);
            uri = Uri.parse(path);
            File file = new File(path);
            boolean deleted = file.delete();
        }
        return uri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startCapturing();
        }else {
            Toast.makeText(this, "Please grant camera permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        cameraUtility.stopBackgroundThread();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

//    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
//                || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
//                || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
//                || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
//                || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
//        )

}