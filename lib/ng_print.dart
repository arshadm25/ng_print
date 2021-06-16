
import 'dart:async';

import 'package:flutter/services.dart';
enum Alignment {
  ALIGNMENT_NORMAL,
  ALIGNMENT_OPPOSITE,
  ALIGNMENT_CENTER,
}
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

  static Future printText(String message,{Alignment alignment,int fontSize:16}) async {
    int algmnt =0;
    switch(alignment){
      case Alignment.ALIGNMENT_CENTER:
        algmnt = 2;
        break;
      case Alignment.ALIGNMENT_NORMAL:
        algmnt = 0;
        break;
      case Alignment.ALIGNMENT_OPPOSITE:
        algmnt = 1;
        break;
    }
    final version = await _channel.invokeMethod('printText',{
      'message':message,
      'Alignment':algmnt,
      'fontSize':fontSize
    });
    return version;
  }
  static Future printSeperator() async {
    String separator = "--------------------------------";
    int algmnt =0;
    final version = await _channel.invokeMethod('printText',{
      'message':separator,
      'Alignment':algmnt,
      'fontSize':16
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
