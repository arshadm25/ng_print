package com.example.ng_print;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
              System.out.println(mConnectedDeviceName);
              break;
            case BluetoothPrinter.STATE_CONNECTING:
              System.out.println("Connecting");
              break;
            case BluetoothPrinter.STATE_LISTEN:
            case BluetoothPrinter.STATE_NONE:
              System.out.println("No Connection");
              break;
          }
          break;
        case BluetoothPrinter.MESSAGE_DEVICE_NAME:
          // save the connected device's name
          mConnectedDeviceName = msg.getData().getString(
                  BluetoothPrinter.DEVICE_NAME);
          break;
        case BluetoothPrinter.MESSAGE_STATUS:
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
          result.success("Android " + android.os.Build.VERSION.RELEASE);
      }catch(Exception ex){
        result.error("COULDNT_INITATE",ex.getMessage(),null);
      }
    }else if (call.method.equals("showDevices")) {
     try{
         mBtp.showDeviceList(this.mActivity);
         result.success("Android " + android.os.Build.VERSION.RELEASE);
     }catch(Exception ex){
         result.error("COULDNT_LIST",ex.getMessage(),null);
     }
    }else if (call.method.equals("printImage")) {
        try{
            boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
            mBtp.printImage(call.argument("path").toString());
            result.success("Android " + android.os.Build.VERSION.RELEASE);
        }catch(Exception ex){
            result.error("COULDNT_PRINT",ex.getMessage(),null);
        }
    } else {
      result.notImplemented();
    }
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
