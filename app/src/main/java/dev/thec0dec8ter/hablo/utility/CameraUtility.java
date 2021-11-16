package dev.thec0dec8ter.hablo.utility;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class CameraUtility {
    private final String TAG = "CameraUtility: ";
    //    https://www.freecodecamp.org/news/android-camera2-api-take-photos-and-videos/
    private final Context context;

    private String mCameraId;
    private CameraDevice mCameraDevice;
    private CaptureRequest.Builder captureRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;

    private Handler handler;
    private HandlerThread handlerThread;
//    private final Semaphore lock;

    CameraManager cameraManager;

    public CameraUtility(Context context){
        this.context = context;
//        lock = new Semaphore(1);
    }

    public void openCamera(final TextureView textureView) throws CameraAccessException {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Please grant camera permission", Toast.LENGTH_SHORT).show();

            return;
        }
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        mCameraId = cameraManager.getCameraIdList()[0];
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(mCameraId);
        StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        Size previewSize = map.getOutputSizes(ImageFormat.JPEG)[0];
        ImageReader reader = ImageReader.newInstance(
                previewSize.getWidth(),
                previewSize.getHeight(),
                ImageFormat.JPEG, 1);

        reader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                image.close();
            }
        }, handler);


        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        Surface surface = new Surface(surfaceTexture);

        CameraCaptureSession.StateCallback sessionCallback = new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                mCameraCaptureSession = session;
                try {
                    CaptureRequest previewRequest = captureRequestBuilder.build();
                    mCameraCaptureSession.setRepeatingRequest(previewRequest, null, handler);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {

            }
        };

        CameraDevice.StateCallback cameraStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull final CameraDevice cameraDevice) {
                mCameraDevice = cameraDevice;
                try {
                    mCameraDevice.createCaptureSession(Arrays.asList(surface, reader.getSurface()), sessionCallback, null);
                    captureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                    captureRequestBuilder.addTarget(surface);
                    captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice cameraDevice) {
                closeCamera();
            }

            @Override
            public void onError(@NonNull CameraDevice cameraDevice, int i) {
                switch (i) {
                    case ERROR_CAMERA_DEVICE:
                        //TODO: "Fatal (device)"
                    case ERROR_CAMERA_DISABLED:
                        //TODO: "Device policy"
                    case ERROR_CAMERA_IN_USE:
                        //TODO: Camera in use"
                    case ERROR_CAMERA_SERVICE:
                        //TODO: "Fatal (service)"
                    case ERROR_MAX_CAMERAS_IN_USE:
                        //TODO: "Maximum cameras in use"
                    default:
                        //TODO: "Unknown"

                }
                Log.e(TAG, "Error when trying to connect camera $errorMsg");
                closeCamera();
            }
        };

        cameraManager.openCamera(mCameraId, cameraStateCallback, handler);

//        try {
//            if (!lock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
//                throw new RuntimeException("Time out waiting to lock camera opening.");
//            }
//        } catch (InterruptedException e) { e.printStackTrace(); }

    }



    public void closeCamera(){
        mCameraDevice.close();
        mCameraDevice = null;
//        try {
//            lock.acquire();
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            lock.release();
//        }
    }

    public void startBackgroundThread(){
        handlerThread = new HandlerThread("HabloCamera", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public void stopBackgroundThread(){
        handlerThread.quitSafely();
        try {
            handlerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        closeCamera();
        handler = null;
        handlerThread = null;
    }


}
