package com.example.ng_print;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.TextPaint;
import android.widget.Toast;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import com.ngx.BluetoothPrinter;
import com.ngx.PrinterWidth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/** NgPrintPlugin */
public class NgPrintPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware, PluginRegistry.NewIntentListener  {
  public static BluetoothPrinter mBtp = BluetoothPrinter.INSTANCE;
  private MethodChannel channel;
  private String mConnectedDeviceName = "";
  private Context mContext;
  private Activity mActivity;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    this.mContext = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "ng_print");
    channel.setMethodCallHandler(this);
  }


  @SuppressLint("HandlerLeak")
  private final Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case BluetoothPrinter.MESSAGE_STATE_CHANGE:
          switch (msg.arg1) {
            case BluetoothPrinter.STATE_CONNECTED:
              writeToFile("Connected to " +mConnectedDeviceName);
              System.out.println(mConnectedDeviceName);
              break;
            case BluetoothPrinter.STATE_CONNECTING:
              writeToFile("Connecting ");
              System.out.println("Connecting");
              break;
            case BluetoothPrinter.STATE_LISTEN:
            case BluetoothPrinter.STATE_NONE:
              writeToFile("No Connection");
              System.out.println("No Connection");
              break;
          }
          break;
        case BluetoothPrinter.MESSAGE_DEVICE_NAME:
          // save the connected device's name
          mConnectedDeviceName = msg.getData().getString(
                  BluetoothPrinter.DEVICE_NAME);
          writeToFile("MESSAGE DEVICE NAME  "+ mConnectedDeviceName);
          break;
        case BluetoothPrinter.MESSAGE_STATUS:
          writeToFile("MESSAGE DEVICE STATUS  " + BluetoothPrinter.STATUS_TEXT);
          System.out.println(BluetoothPrinter.STATUS_TEXT);
          break;
        default:
          break;
      }
    }
  };

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("initialize")) {
      try{
          mBtp.initService(this.mContext,mHandler);
        writeToFile("INITIALIZING CONNECTION");
          result.success("Android " + android.os.Build.VERSION.RELEASE);
      }catch(Exception ex){
        writeToFile("COULDNT_INITATE :" + ex.getMessage());
      }
    }else if (call.method.equals("showDevices")) {
     try{
         mBtp.showDeviceList(this.mActivity);
       writeToFile("SHOW DEVICES");
       result.success("Android " + android.os.Build.VERSION.RELEASE);
     }catch(Exception ex){
       writeToFile("COULDNT_LIST :" + ex.getMessage());
     }
    }else if (call.method.equals("printText")) {
      try{
        mBtp.printUnicodeText("ಒಟ್ಟು ಐಟಂಗಳು: 2 ಮೊತ್ತ");
        writeToFile("Print Kannada");
        mBtp.printUnicodeText("Test Subject");
        writeToFile("Print English");

        result.success("Android " + android.os.Build.VERSION.RELEASE);
      }catch(Exception ex){
        writeToFile("COULDNT_LIST :" + ex.getMessage());
      }
    }else if (call.method.equals("printImage")) {
        try{
            boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
            mBtp.printImage(call.argument("path").toString());
            writeToFile("PRINT IMAGE");
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        }catch(Exception ex){
            writeToFile("COULDNT_PRINT :" + ex.getMessage());
            result.error("COULDNT_PRINT",ex.getMessage(),null);
        }
    } else {
      result.notImplemented();
    }
  }

  private void writeToFile(String data) {
//    try {
//      File sdCard = Environment.getExternalStorageDirectory();
//      File dir = new File (sdCard.getAbsolutePath() + "/fish");
//      dir.mkdirs();
//      File file = new File(dir, "filename.txt");
//      FileOutputStream f = new FileOutputStream(file);
//    }
//    catch (IOException e) {
//      System.out.println(e.getMessage());
//    }
  }


  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    binding.addOnNewIntentListener(this);
    mActivity = binding.getActivity();

  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
    channel.setMethodCallHandler(null);
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    binding.addOnNewIntentListener(this);
    mActivity = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {
    mActivity = null;
    mBtp.onActivityDestroy();
  }

  @Override
  public boolean onNewIntent(Intent intent) {
    return false;
  }

}
