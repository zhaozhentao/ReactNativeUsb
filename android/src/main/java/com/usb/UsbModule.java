package com.usb;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;

@ReactModule(name = UsbModule.NAME)
public class UsbModule extends NativeUsbSpec implements SerialInputOutputManager.Listener {

  public static final String NAME = "Usb";

  SerialInputOutputManager usbIoManager;

  public UsbModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return NAME;
  }

  // Example method
  // See https://reactnative.dev/docs/native-modules-android
  @Override
  public String connect() {
    UsbManager manager = (UsbManager) getReactApplicationContext().getSystemService(Context.USB_SERVICE);
    List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);

    if (availableDrivers.isEmpty()) {
      Toast.makeText(getReactApplicationContext(), "找不到设备", Toast.LENGTH_SHORT).show();
      return "1";
    }

    UsbSerialDriver driver = availableDrivers.get(0);
    UsbDeviceConnection connection = manager.openDevice(driver.getDevice());

    if (connection == null) {
      Toast.makeText(getReactApplicationContext(), "连接不上", Toast.LENGTH_SHORT).show();
      return "2";
    }

    try {
      // Most devices have just one port (port 0)
      UsbSerialPort port = driver.getPorts().get(0);
      port.open(connection);
      port.setParameters(19200, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

      usbIoManager = new SerialInputOutputManager(port, this);
      usbIoManager.start();

      Toast.makeText(getReactApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();

      return driver.getDevice().getDeviceName();
    } catch (IOException e) {
      Toast.makeText(getReactApplicationContext(), "连接异常", Toast.LENGTH_SHORT).show();
      throw new RuntimeException(e);
    }
  }

  @Override
  public void send(ReadableArray data) {
    if (usbIoManager == null) {
      Toast.makeText(getReactApplicationContext(), "未连接", Toast.LENGTH_SHORT).show();

      return;
    }

    byte[] buf = new byte[data.size()];

    for (int i = 0; i < data.size(); i++) {
      buf[i] = (byte) data.getInt(i);
    }

    usbIoManager.writeAsync(buf);
  }


  @Override
  public void onNewData(byte[] bytes) {
    WritableArray array = Arguments.createArray();

    for (byte b : bytes) {
      array.pushInt(b & 0xff);
    }

    getReactApplicationContext()
      .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
      .emit("onSerialDataReceive", array);
  }

  @Override
  public void onRunError(Exception e) {

  }
}
