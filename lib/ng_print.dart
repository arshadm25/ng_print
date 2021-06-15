
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
  static Future printText() async {
    final version = await _channel.invokeMethod('printText',);
    return version;
  }
  static Future printImage(String path) async {
    final version = await _channel.invokeMethod('printImage',{
      'path':path
    });
    return version;
  }
}
