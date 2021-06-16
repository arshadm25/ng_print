
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
    try{
      final version = await _channel.invokeMethod('showDevices');
      return version;
    }catch(ex){
      rethrow;
    }
  }
  static Future printText(String message) async {
    try{
      final version = await _channel.invokeMethod('printText',{
        'message':message
      });
      return version;
    }catch(ex){
      rethrow;
    }
  }
  static Future printImage(String path) async {
    try{
      final version = await _channel.invokeMethod('printImage',{
        'path':path
      });
      return version;
    }catch(ex){
      rethrow;
    }
  }

  static Future<int> getState() async {
    try{
      final version = await _channel.invokeMethod('getState');
      return version;
    }catch(ex){
      rethrow;
    }
  }
}
