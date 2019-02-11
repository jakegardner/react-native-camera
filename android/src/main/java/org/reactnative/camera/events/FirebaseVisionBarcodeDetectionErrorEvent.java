package org.reactnative.camera.events;

import android.support.v4.util.Pools;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;

import org.reactnative.camera.CameraViewManager;

public class FirebaseVisionBarcodeDetectionErrorEvent extends Event<FirebaseVisionBarcodeDetectionErrorEvent> {

  private static final Pools.SynchronizedPool<FirebaseVisionBarcodeDetectionErrorEvent> EVENTS_POOL = new Pools.SynchronizedPool<>(3);
  private FirebaseVisionBarcodeDetector mBarcodeDetector;

  private FirebaseVisionBarcodeDetectionErrorEvent() {
  }

  public static FirebaseVisionBarcodeDetectionErrorEvent obtain(int viewTag, FirebaseVisionBarcodeDetector barcodeDetector) {
    FirebaseVisionBarcodeDetectionErrorEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new FirebaseVisionBarcodeDetectionErrorEvent();
    }
    event.init(viewTag, barcodeDetector);
    return event;
  }

  private void init(int viewTag, FirebaseVisionBarcodeDetector barcodeDetector) {
    super.init(viewTag);
    mBarcodeDetector = barcodeDetector;
  }

  @Override
  public short getCoalescingKey() {
    return 0;
  }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_FIREBASE_BARCODE_DETECTION_ERROR.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableMap map = Arguments.createMap();
    map.putBoolean("isOperational", mBarcodeDetector != null);
    return map;
  }
}
