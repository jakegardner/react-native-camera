package org.reactnative.camera.tasks;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;

import java.util.List;

public interface FirebaseVisionDetectorAsyncTaskDelegate {

    void onFirebaseVisionBarcodesDetected(List<FirebaseVisionBarcode> barcodes);

    void onFirebaseVisionBarcodeDetectionError(FirebaseVisionBarcodeDetector barcodeDetector);

    void onFirebaseVisionBarcodeDetectingTaskCompleted();
}
