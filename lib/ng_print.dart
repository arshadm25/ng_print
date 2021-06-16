
import 'dart:async';

import 'package:flutter/services.dart';

class NgPrint {
  static const MethodChannel _channel =
      const MethodChannel('ng_print');

  static Future initialize() async {
    final version = await _channel.invokeMethod('initialize');
    return version;
  }

  static Future showDevices() async {
    final version = await _channel.invokeMethod('showDevices');
    return version;
  }
  static Future setPrinterWidth() async {
    final state = await _channel.invokeMethod('setPrinterWidth');
    return state;
  }

  static Future<int> getState() async {
    final state = await _channel.invokeMethod('getState');
    return state;
  }

  static Future printText(String message) async {
    final version = await _channel.invokeMethod('printText',{
      'message':message
    });
    return version;
  }
  static Future printImage(String path) async {
    final version = await _channel.invokeMethod('printImage',{
      'path':path
    });
    return version;
  }
}
