
import 'dart:async';

import 'package:flutter/services.dart';
enum PAlignment {
  ALIGNMENT_NORMAL,
  ALIGNMENT_OPPOSITE,
  ALIGNMENT_CENTER,
}

enum TAlignment {
  LEFT,
  CENTER,
  RIGHT,
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
  static Future setPrinterWidth(int printerId) async {
    //send 1 for 3 inch and 0 for 2 inch
    final state = await _channel.invokeMethod('setPrinterWidth',{
      'printerId':printerId
    });
    return state;
  }

  static Future<int> getState() async {
    final state = await _channel.invokeMethod('getState');
    return state;
  }

  static Future printText(String message,{PAlignment alignment:PAlignment.ALIGNMENT_NORMAL,int fontSize:30,TAlignment textAlign:TAlignment.LEFT}) async {
    int algmnt =0;
    switch(alignment){
      case PAlignment.ALIGNMENT_CENTER:
        algmnt = 2;
        break;
      case PAlignment.ALIGNMENT_NORMAL:
        algmnt = 0;
        break;
      case PAlignment.ALIGNMENT_OPPOSITE:
        algmnt = 1;
        break;
    }
    int textAlignment = 0;
    switch(textAlign){
      case TAlignment.LEFT:
        textAlignment = 0;
        break;
      case TAlignment.CENTER:
        textAlignment = 1;
        break;
      case TAlignment.RIGHT:
        textAlignment = 2;
        break;
    }
    final version = await _channel.invokeMethod('printText',{
      'message':message,
      'Alignment':algmnt,
      'fontSize':fontSize,
      "textAlignment":textAlignment
    });
    return version;
  }
  static Future printSeperator() async {
    String separator = "---------------------";
    final version = await _channel.invokeMethod('printText',{
      'message':separator,
      'Alignment':0,
      'fontSize':30,
      "textAlignment":0
    });
    return version;
  }
  static Future printEmptyLine() async {
    String newLine = "\n";

    final version = await _channel.invokeMethod('printText',{
      'message':newLine,
      'Alignment':0,
      'fontSize':30,
      "textAlignment":0
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
