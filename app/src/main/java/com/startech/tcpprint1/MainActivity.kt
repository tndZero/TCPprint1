package com.startech.tcpprint1

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPrint.setOnClickListener {

            printTcp()
        }

        button_bluetooth_browse.setOnClickListener {
            browseBluetoothDevice()
        }

        button_bluetooth.setOnClickListener {
            printBluetooth()
        }

    }


     fun printTcp() {
        val ipAddress:String = txtIP.text.toString()

        val portAddress = txtPort.text.toString().toInt()


        try {
            Thread(Runnable {
                val tcpConnection = TcpConnection(
                    ipAddress,
                    portAddress
                )
                printIt(tcpConnection)
            }).start()

        } catch (e: NumberFormatException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }


    val PERMISSION_BLUETOOTH = 1

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == 1) {
                this.printBluetooth()
            }
        }
    }

    private var selectedDevice: BluetoothConnection? = null

    fun browseBluetoothDevice() {
        val bluetoothDevicesList = BluetoothPrintersConnections().list
        if (bluetoothDevicesList != null) {
            val items = arrayOfNulls<String>(bluetoothDevicesList.size + 1)
            items[0] = "Default printer"
            var i = 0
            for (device in bluetoothDevicesList) {
                items[++i] = device.device.name
            }
            val alertDialog = AlertDialog.Builder(this@MainActivity)
            alertDialog.setTitle("Bluetooth printer selection")
            alertDialog.setItems(
                items
            ) { dialogInterface, i ->
                val index = i - 1
                selectedDevice = if (index == -1) {
                    null
                } else {
                    bluetoothDevicesList[index]
                }

                button_bluetooth_browse.text = items[i]
            }
            val alert = alertDialog.create()
            alert.setCanceledOnTouchOutside(false)
            alert.show()
        }
    }
    fun printBluetooth() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH),
                1
            )
        } else {
            printIt(selectedDevice)
        }
    }

    @SuppressLint("LongLogTag")
     fun printIt(printerConnection: DeviceConnection?) {
        try {
            val printer = EscPosPrinter(printerConnection, 203, 80f, 48,
                EscPosCharsetEncoding(txtIP.text.toString(), txtPort.text.toString().toInt()))
            printer
                .printFormattedText(
//                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
//                        printer,
//                        this.applicationContext.resources
//                            .getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)
//                    ).toString() + "</img>\n" +
//                            "[L]\n" +
//                            "[C]<u><font size='big'>RB FOOD</font></u>\n" +
//                            "[L]\n" +
//                            "[C]========================================\n" +
//                            "[L]\n" +
                            "[L]<b>TEST สินค้าทดสอบ</b>[L]9.99\n" //+
//                            "[L]  + Size : S\n" +
//                            "[L]\n" +
//                            "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
//                            "[L]  + Size : 57/58\n" +
//                            "[L]\n" //+
//                            "[C]--------------------------------\n" +
//                            "[R]TOTAL PRICE :[R]34.98e\n" +
//                            "[R]TAX :[R]4.23e\n" +
//                            "[L]\n" +
//                            "[C]================================\n" +
//                            "[L]\n" +
//                            "[L]<font size='tall'>Customer :</font>\n" +
//                            "[L]Raymond DUPONT\n" +
//                            "[L]5 rue des girafes\n" +
//                            "[L]31547 PERPETES\n" +
//                            "[L]Tel : +33801201456\n" //+
                            //"[L]\n" //+
                            //"[C]<barcode type='ean13' height='10'>831254784551</barcode>\n" +
                            //"[C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>"
                )

            printer.disconnectPrinter()
        } catch (e: EscPosConnectionException) {
            e.printStackTrace()
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: EscPosParserException) {
            e.printStackTrace()
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: EscPosEncodingException) {
            e.printStackTrace()
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: EscPosBarcodeException) {
            e.printStackTrace()
            //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }
    }


}
