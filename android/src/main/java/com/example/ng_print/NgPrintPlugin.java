package com.example.ng_print;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
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
        result.success(true);
      }catch(Exception ex){
        result.error("INIT_ERROR",ex.getMessage(),"");
      }
    }else if (call.method.equals("showDevices")) {
      try{
        mBtp.showDeviceList(this.mActivity);
        result.success(true);
      }catch(Exception ex){
        result.error("DEVICE_LIST_ERROR",ex.getMessage(),"");
      }
    }else if (call.method.equals("getState")) {
      try{
        result.success(mBtp.getState());
      }catch(Exception ex){
        result.error("GET_STATE_ERROR",ex.getMessage(),"");
      }
    }else if (call.method.equals("setPrinterWidth")) {
      try{
        mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
        result.success(true);
      }catch(Exception ex){
        result.error("DEVICE_LIST_ERROR",ex.getMessage(),"");
      }
    }else if (call.method.equals("printText")) {
      try{
        int alignment = (int)call.argument("Alignment");
        Layout.Alignment alignment1 = Layout.Alignment.ALIGN_NORMAL;
        switch (alignment){
          case 1:
            alignment1 = Layout.Alignment.ALIGN_OPPOSITE;
            break;
          case 2:
            alignment1 = Layout.Alignment.ALIGN_CENTER;
            break;
        }

        int textAlignment = (int)call.argument("textAlignment");
        Paint.Align textAlign = Paint.Align.LEFT;
        switch (textAlignment){
          case 1:
            textAlign = Paint.Align.CENTER;
            break;
          case 2:
            textAlign = Paint.Align.RIGHT;
            break;
        }
        int fontSize = (int)call.argument("fontSize");
        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DroidSansMono.ttf");
        tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
        tp.setTextAlign(textAlign);
        tp.setTextSize(fontSize);
        mBtp.printUnicodeText(call.argument("message").toString(),alignment1,tp);
        result.success(true);
      }catch(Exception ex){
        result.error("PRINT_TEXT_ERROR",ex.getMessage(),"");
      }
    }
    else if (call.method.equals("printImage")) {
      try{
        Bitmap bitmap = BitmapFactory.decodeFile(call.argument("path").toString());
        mBtp.printRasterImage(bitmap);
        result.success(true);
      }catch(Exception ex){
        result.error("PRINT_IMAGE_ERROR",ex.getMessage(),null);
      }
    } else {
      result.notImplemented();
    }
  }

  private void showToast(String message){
    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
  }

  private void writeToFile(String data) {
//    try {
//      File sdCard = Environment.getExternalStorageDirectory();
//      File dir = new File (sdCard.getAbsolutePath() + "/fish");
//      dir.mkdirs();
//      File file = new File(dir, "filename.txt");
//      if(!file.exists()){
//        file.createNewFile();
//      }
//      FileOutputStream f = new FileOutputStream(file,true);
//      PrintWriter pw = new PrintWriter(f);
//      pw.println(data);
//      pw.close();
//      f.close();
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