package org.reactnative.camera.events;

import android.support.v4.util.Pools;
import android.util.SparseArray;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import org.reactnative.barcodedetector.BarcodeFormatUtils;
import org.reactnative.camera.CameraViewManager;

import java.util.List;

public class FirebaseVisionBarcodesDetectedEvent extends Event<FirebaseVisionBarcodesDetectedEvent> {

  private static final Pools.SynchronizedPool<FirebaseVisionBarcodesDetectedEvent> EVENTS_POOL =
      new Pools.SynchronizedPool<>(3);

  private List<FirebaseVisionBarcode> mBarcodes;

  private FirebaseVisionBarcodesDetectedEvent() {
  }

  public static FirebaseVisionBarcodesDetectedEvent obtain(
      int viewTag,
      List<FirebaseVisionBarcode> barcodes
  ) {
    FirebaseVisionBarcodesDetectedEvent event = EVENTS_POOL.acquire();
    if (event == null) {
      event = new FirebaseVisionBarcodesDetectedEvent();
    }
    event.init(viewTag, barcodes);
    return event;
  }

  private void init(
      int viewTag,
      List<FirebaseVisionBarcode> barcodes
  ) {
    super.init(viewTag);
    mBarcodes = barcodes;
  }

  @Override
  public short getCoalescingKey() {
    if (mBarcodes.size() > Short.MAX_VALUE) {
      return Short.MAX_VALUE;
    }

    return (short) mBarcodes.size();
  }

  @Override
  public String getEventName() {
    return CameraViewManager.Events.EVENT_ON_FIREBASE_BARCODES_DETECTED.toString();
  }

  @Override
  public void dispatch(RCTEventEmitter rctEventEmitter) {
    rctEventEmitter.receiveEvent(getViewTag(), getEventName(), serializeEventData());
  }

  private WritableMap serializeEventData() {
    WritableArray barcodesList = Arguments.createArray();

    for (int i = 0; i < mBarcodes.size(); i++) {
      FirebaseVisionBarcode barcode = mBarcodes.get(i);
      WritableMap serializedBarcode = Arguments.createMap();
      serializedBarcode.putString("data", barcode.getRawValue());
      serializedBarcode.putString("type", BarcodeFormatUtils.get(barcode.getValueType()));
      barcodesList.pushMap(serializedBarcode);
    }

    WritableMap event = Arguments.createMap();
    event.putString("type", "barcode");
    event.putArray("barcodes", barcodesList);
    event.putInt("target", getViewTag());
    return event;
  }
}
