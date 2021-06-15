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
      mBtp.initService(this.mContext,mHandler);
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("showDevices")) {
      mBtp.showDeviceList(this.mActivity);
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("printNow")) {
      boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
      this.printEnglishBill();
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("printNow1")) {
      boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
      printKannadaBill();
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("printNow2")) {
      boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
      printKannadaBill1();
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("printNow3")) {
      boolean r = mBtp.setPrinterWidth(PrinterWidth.PRINT_WIDTH_48MM);
      mBtp.printUnicodeText("ನಗದು");
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if (call.method.equals("printImage")) {
      mBtp.printImage(call.argument("path").toString());
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else {
      result.notImplemented();
    }
  }

  private void printImage(){

  }
  private void printKannadaBill1() {

      if (mBtp.getState() != BluetoothPrinter.STATE_CONNECTED) {
        mBtp.connectToPrinter(this.mContext);
        Toast.makeText(this.mActivity, "Printer is not connected", Toast.LENGTH_SHORT).show();
        return;
      }
      String separator = "--------------------------------";
      Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DroidSansMono.ttf");

      TextPaint tp = new TextPaint();

      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      tp.setTextSize(30);
      tp.setColor(Color.BLACK);
      mBtp.addText("ನಗದು ಬಿಲ್ಲು", Layout.Alignment.ALIGN_CENTER, tp);

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ದಿನಾಂಕ: 31-05-2017  ಬಿಲ್ ಸಂಖ್ಯೆ: 001\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ಹೆಸರು: ವಿನಾಯಕ\n");
      stringBuilder.append("ಸ್ಥಳ: ಬೆಂಗಳೂರು\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ವಿವರಣೆ        ಪ್ರಮಾಣ   ದರ    ಮೊತ್ತ\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ರೆನಾಲ್ಡ್ಸ್ ಪೆನ್        2   10     20\n");
      stringBuilder.append("ನಟರಾಜ್ ಎರೇಸರ್    10    5     50\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ಒಟ್ಟು ಐಟಂಗಳು: 2       ಮೊತ್ತ:  66.50\n");
      stringBuilder.append("ಒಟ್ಟು ಪ್ರಮಾಣ: 12   ವ್ಯಾಟ್ ಮೊತ್ತ: 3.50\n");
      stringBuilder.append("                  -------------");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

      stringBuilder.setLength(0);
      stringBuilder.append("         ನಿವ್ವಳ ಮೊತ್ತ: 70.00");
      tp.setTextSize(25);
      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

      stringBuilder.setLength(0);
      stringBuilder.append("ಪಾವತಿ ಮೋಡ್: ನಗದು\n\n\n");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
      mBtp.print();
  }

  private void printKannadaBill() {

      String separator = "--------------------------------";
      Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/DroidSansMono.ttf");

      TextPaint tp = new TextPaint();

      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      tp.setTextSize(30);
      tp.setColor(Color.BLACK);
      mBtp.addText("ನಗದು ಬಿಲ್ಲು", Layout.Alignment.ALIGN_CENTER, tp);

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("ದಿನಾಂಕ: 31-05-2017  ಬಿಲ್ ಸಂಖ್ಯೆ: 001\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ಹೆಸರು: ವಿನಾಯಕ\n");
      stringBuilder.append("ಸ್ಥಳ: ಬೆಂಗಳೂರು\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ವಿವರಣೆ        ಪ್ರಮಾಣ   ದರ    ಮೊತ್ತ\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ರೆನಾಲ್ಡ್ಸ್ ಪೆನ್        2   10     20\n");
      stringBuilder.append("ನಟರಾಜ್ ಎರೇಸರ್    10    5     50\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("ಒಟ್ಟು ಐಟಂಗಳು: 2       ಮೊತ್ತ:  66.50\n");
      stringBuilder.append("ಒಟ್ಟು ಪ್ರಮಾಣ: 12   ವ್ಯಾಟ್ ಮೊತ್ತ: 3.50\n");
      stringBuilder.append("                  -------------");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

      stringBuilder.setLength(0);
      stringBuilder.append("         ನಿವ್ವಳ ಮೊತ್ತ: 70.00");
      tp.setTextSize(25);
      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

      stringBuilder.setLength(0);
      stringBuilder.append("ಪಾವತಿ ಮೋಡ್: ನಗದು\n\n\n");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
      mBtp.print();
  }

  private void printEnglishBill() {
      String separator = "--------------------------------";
      Typeface tf = Typeface.createFromAsset(this.mActivity.getAssets(), "fonts/DroidSansMono.ttf");

      TextPaint tp = new TextPaint();
      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      tp.setTextSize(30);
      tp.setColor(Color.BLACK);
      mBtp.addText("Cash Bill", Layout.Alignment.ALIGN_CENTER, tp);

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Date: 31-05-2017  Bill No: 001\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("Name: Vinayak\n");
      stringBuilder.append("Place: Bangalore\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("Particulars    Qty   Rate    Amt\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("Reynolds Pen     2     10     20\n");
      stringBuilder.append("Nataraj Eraser  10      5     50\n");
      stringBuilder.append(separator);
      stringBuilder.append("\n");
      stringBuilder.append("Tot Items: 2      Amount: 66.50\n");
      stringBuilder.append("Tot Qty  :12     Vat Amt:  3.50\n");
      stringBuilder.append("                  -------------");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);

      stringBuilder.setLength(0);
      stringBuilder.append("           Net Amt: 70.00");
      tp.setTextSize(25);
      tp.setTypeface(Typeface.create(tf, Typeface.BOLD));
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);


      stringBuilder.setLength(0);
      stringBuilder.append("Payment Mode: CASH\n\n\n");
      tp.setTextSize(20);
      tp.setTypeface(tf);
      mBtp.addText(stringBuilder.toString(), Layout.Alignment.ALIGN_NORMAL, tp);
      mBtp.print();
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
