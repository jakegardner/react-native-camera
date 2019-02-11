package org.reactnative.camera.tasks;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseVisionDetectorAsyncTask extends android.os.AsyncTask<Void, Void, List<FirebaseVisionBarcode>> {
  private byte[] mImageData;
  private int mWidth;
  private int mHeight;
  private int mRotation;
  private FirebaseVisionDetectorAsyncTaskDelegate mDelegate;
  private final FirebaseVisionBarcodeDetector mFirebaseBarcodeDetector;

  public FirebaseVisionDetectorAsyncTask(
      FirebaseVisionDetectorAsyncTaskDelegate delegate,
      FirebaseVisionBarcodeDetector firebaseBarcodeDetector,
      byte[] imageData,
      int width,
      int height,
      int rotation
  ) {
    mImageData = imageData;
    mWidth = width;
    mHeight = height;
    mRotation = rotation;
    mDelegate = delegate;
    mFirebaseBarcodeDetector = firebaseBarcodeDetector;
  }

  @Override
  protected List<FirebaseVisionBarcode> doInBackground(Void... ignored) {
    if (isCancelled() || mDelegate == null) {
      return null;
    }

    List<FirebaseVisionBarcode> result = null;

    try {
      FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
        .setWidth(mWidth)
        .setHeight(mHeight)
        .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
        .setRotation(mRotation)
        .build();
      FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(mImageData, metadata);

      Task<List<FirebaseVisionBarcode>> task = mFirebaseBarcodeDetector.detectInImage(image);
      result = Tasks.await(task);
    } catch (ExecutionException e) {
      mDelegate.onFirebaseVisionBarcodeDetectionError(mFirebaseBarcodeDetector);
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (Throwable t) {
      t.printStackTrace();
    }

    return result;
  }

  @Override
  protected void onPostExecute(List<FirebaseVisionBarcode> result) {
    super.onPostExecute(result);
    if (result != null && result.size() > 0) {
      mDelegate.onFirebaseVisionBarcodesDetected(result);
    }
    mDelegate.onFirebaseVisionBarcodeDetectingTaskCompleted();
  }
}
