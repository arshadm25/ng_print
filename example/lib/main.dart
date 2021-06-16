import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:ng_print/ng_print.dart';
import 'package:image/image.dart' as im;
import 'dart:ui' as ui;

import 'package:path_provider/path_provider.dart';
import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home:HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  const HomePage({Key key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  bool initiated = false;
  bool isConnected = false;
  @override
  void initState() {
    super.initState();
    initialize();
  }
  initialize() async {
    if(!(await Permission.location.isGranted)){
      await Permission.location.request();
    }
    if(!(await Permission.storage.isGranted)){
      await Permission.storage.request();
    }
    await NgPrint.initialize();
    setState(() {
      initiated = true;
    });
  }
  GlobalKey previewContainer = new GlobalKey();
  @override
  Widget build(BuildContext context) {
    return  Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: initiated?RepaintBoundary(
         key:previewContainer,

        child:SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              SizedBox(height: 10),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("Sample Shop"), //Goverment of Karnataka
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("Shop address"), //Department Of Fisheries
                ],
              ),
              SizedBox(
                height: 5,
              ),

              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("PIN -679335"),
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Divider(
                color: Colors.black,
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text("Date" + ":" + "13/06/2021"), //Date
                  Text("Bill No." + ":" + "100") //BillNo
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(
                    width: 2,
                  ),
                  Text("ಹೆಸರು:Rahul"), //Name
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(
                    width: 2,
                  ),
                  Text("Phone : 123456789"), //Mobile
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Divider(
                color: Colors.black,
              ),
              DataTable(
                columnSpacing: 0,

                horizontalMargin: 0,

                columns: const <DataColumn>[
                  DataColumn(

                    label: Text(
                      'SI',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),
                  ),
                  DataColumn(
                    label: Text(
                      'ITEM',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),
                  ),
                  DataColumn(
                    label: Text(
                      'Qty',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),
                  ),
                  DataColumn(
                    label: Text(
                      'AMOUNT',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),

                  ),

                ],
                rows: const <DataRow>[
                  DataRow(

                    cells: <DataCell>[
                      DataCell(Text('1')),
                      DataCell(Text('Item 1')),
                      DataCell(Text('10')),
                      DataCell(Text('100')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('2')),
                      DataCell(Text('Item 2')),
                      DataCell(Text('5')),
                      DataCell(Text('50')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('3')),
                      DataCell(Text('Item 3')),
                      DataCell(Text('12')),
                      DataCell(Text('120')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('4')),
                      DataCell(Text('Item 4')),
                      DataCell(Text('12')),
                      DataCell(Text('120')),
                    ],
                  ),
                ],
              ),

              Divider(
                color: Colors.black,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(
                    width: 5,
                  ),
                  Text("Total Rs :"), //Amunt in words
                  Text(120.toString()),
                ],
              ),
              SizedBox(
                height: 3,
              ),
              Divider(
                color: Colors.black,
              ),
              RaisedButton(
                child:Text("Connect printer"),
                onPressed: () async {
                  try{
                      await NgPrint.showDevices();
                      // setState(() {
                      //   isConnected = true;
                      // });
                  }catch(ex){
                    showDialog(context: context,builder: (context)=>AlertDialog(
                      title: Text("Print"),
                      content: Text(ex.toString()),
                    ));
                  }

                },
              ),
                    RaisedButton(
                      child:Text("print Image"),
                      onPressed: () async {
                        try{
                          int state = await NgPrint.getState();
                          switch(state){
                            case 0:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("STATE_NONE"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 1:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("STATE_LISTEN"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 2:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("Connecting"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 3:
                              printTicket();
                              break;
                            case 4:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("Connection Exception"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                          }
                        }catch(ex){
                          showDialog(context: context,builder: (context)=>AlertDialog(
                            title: Text("Print"),
                            content: Text(ex.toString()),
                          ));
                        }

                      },
                    ),
                    RaisedButton(
                      child:Text("print Text"),
                      onPressed: () async {
                        try{
                          int state = await NgPrint.getState();
                          switch(state){
                            case 0:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("STATE_NONE"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 1:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("STATE_LISTEN"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 2:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("Connecting"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                              break;
                            case 3:
                              await NgPrint.printText();
                              break;
                            case 4:
                              showDialog(context: context,builder: (context)=>AlertDialog(
                                title: Text("Print"),
                                content: Text("Connection Exception"),
                                actions: [
                                  FlatButton(onPressed: (){
                                    Navigator.pop(context);
                                  }, child: Text("OK"))
                                ],
                              ));
                          }
                        }catch(ex){
                          showDialog(context: context,builder: (context)=>AlertDialog(
                            title: Text("Print"),
                            content: Text(ex.toString()),
                          ));
                        }

                      },
                    ),
                  ],
                ),
          )):Center(child:CircularProgressIndicator() ,),
    );
  }

  Future<void> printTicket() async {
      RenderRepaintBoundary boundary = previewContainer.currentContext.findRenderObject();
      ui.Image image = await boundary.toImage();
      final directory = (await getApplicationDocumentsDirectory()).path;
      ByteData byteData = await image.toByteData(format: ui.ImageByteFormat.png);
      Uint8List pngBytes = byteData.buffer.asUint8List();
      File imgFile = new File('$directory/screenshot.png');
      await imgFile.writeAsBytes(pngBytes);
      await NgPrint.printImage(imgFile.path);
    // Navigator.pop(context);
  }
}
