import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';
import 'package:ng_print/ng_print.dart';
import 'package:image/image.dart' as im;
import 'dart:ui' as ui;

import 'package:path_provider/path_provider.dart';

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
  @override
  void initState() {
    super.initState();
    initialize();
  }
  initialize() async {
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
        child: SingleChildScrollView(
          child: Column(
            children: [
              SizedBox(height: 10),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("ಕರ್ನಾಟಕ ಸರ್ಕಾರ"), //Goverment of Karnataka
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("ಮೀನುಗಾರಿಕೆ ಇಲಾಖೆ"), //Department Of Fisheries
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("ಮೀನುಗಾರಿಕೆ ಸಹಾಯಕ ನಿರ್ದೇಶಕರು"),
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("(ಶ್ರೇಣಿ-1),( ಬಂದ್ ಬ್ರೀಡಿಂಗ್) ಬಿ. ಆರ್"),
                ],
              ),
              SizedBox(
                height: 5,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("ಪ್ರಾಜೆಕ್ಟ್ -577115"),
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
                  Text("ದಿನಾಂಕ" + ":" + "13/06/2021"), //Date
                  Text("ಕ್ರ.ಸಂ ನಂ" + ":" + "100") //BillNo
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
                  Text("ಮೊಬೈಲ್ : 9012345679"), //Mobile
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
                      'ನಂ',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),
                  ),
                  DataColumn(
                    label: Text(
                      'ವಿವರಗಳು',
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
                      'ಮೊಬಲಗು  (ರೂ.)',
                      style: TextStyle(fontStyle: FontStyle.italic),
                    ),

                  ),

                ],
                rows: const <DataRow>[
                  DataRow(

                    cells: <DataCell>[
                      DataCell(Text('1')),
                      DataCell(Text('Kattla')),
                      DataCell(Text('10')),
                      DataCell(Text('100')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('1')),
                      DataCell(Text('Kattla')),
                      DataCell(Text('5')),
                      DataCell(Text('50')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('1')),
                      DataCell(Text('Kattla')),
                      DataCell(Text('12')),
                      DataCell(Text('120')),
                    ],
                  ),
                  DataRow(
                    cells: <DataCell>[
                      DataCell(Text('')),
                      DataCell(Text('')),
                      DataCell(Text('ಒಟ್ಟು')),
                      DataCell(Text('120')),
                    ],
                  ),
                ],
              ),

              // SizedBox(
              //   height: 3,
              // ),
              // Divider(
              //   color: Colors.black,
              // ),
              // Row(
              //   mainAxisAlignment: MainAxisAlignment.start,
              //   children: [
              //     SizedBox(
              //       width: 270,
              //     ),
              //     Text("ಒಟ್ಟು:120") //Total
              //   ],
              // ),
              // SizedBox(
              //   height: 3,
              // ),
              Divider(
                color: Colors.black,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(
                    width: 5,
                  ),
                  Text("ಒಟ್ಟು ಮೊತ್ತ Rs :"), //Amunt in words

                ],
              ),
              // RaisedButton(
              //   child:Text("print Normal"),
              //   onPressed: () async {
              //     try{
              //
              //       await NgPrint.showDevices();
              //       await NgPrint.printText();
              //     }catch(ex){
              //       showDialog(context: context,builder: (context)=>AlertDialog(
              //         title: Text("Print"),
              //         content: Text(ex.toString()),
              //       ));
              //     }
              //
              //   },
              // ),
              // RaisedButton(
              //   child:Text("print Kannada"),
              //   onPressed: () async {
              //     try{
              //
              //       await NgPrint.showDevices();
              //       await NgPrint.printUnicode();
              //     }catch(ex){
              //       // print(ex.toString());
              //       showDialog(context: context,builder: (context)=>AlertDialog(
              //         title: Text("Print"),
              //         content: Text(ex.toString()),
              //       ));
              //     }
              //
              //   },
              // ),
              // RaisedButton(
              //   child:Text("print Kannada1"),
              //   onPressed: () async {
              //     try{
              //
              //       await NgPrint.showDevices();
              //       await NgPrint.printUnicode1();
              //     }catch(ex){
              //       // print(ex.toString());
              //       showDialog(context: context,builder: (context)=>AlertDialog(
              //         title: Text("Print"),
              //         content: Text(ex.toString()),
              //       ));
              //     }
              //
              //   },
              // ),
              // RaisedButton(
              //   child:Text("print Kannada2"),
              //   onPressed: () async {
              //     try{
              //
              //       await NgPrint.showDevices();
              //       await NgPrint.printUnicode2();
              //     }catch(ex){
              //       // print(ex.toString());
              //       showDialog(context: context,builder: (context)=>AlertDialog(
              //         title: Text("Print"),
              //         content: Text(ex.toString()),
              //       ));
              //     }
              //
              //   },
              // ),
              RaisedButton(
                child:Text("print Image"),
                onPressed: () async {
                  try{

                    printTicket();
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

                    await NgPrint.showDevices();
                    await NgPrint.printText("asdf");
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
        ),
      ):Center(child:CircularProgressIndicator() ,),
    );
  }

  Future<void> printTicket() async {
      await NgPrint.showDevices();
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
