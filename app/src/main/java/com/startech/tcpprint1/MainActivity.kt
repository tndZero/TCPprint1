package com.startech.tcpprint1

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dantsu.escposprinter.EscPosCharsetEncoding
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.DeviceConnection
import com.dantsu.escposprinter.connection.tcp.TcpConnection
import com.dantsu.escposprinter.exceptions.EscPosBarcodeException
import com.dantsu.escposprinter.exceptions.EscPosConnectionException
import com.dantsu.escposprinter.exceptions.EscPosEncodingException
import com.dantsu.escposprinter.exceptions.EscPosParserException
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPrint.setOnClickListener {

            printTcp()
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


    @SuppressLint("LongLogTag")
     fun printIt(printerConnection: DeviceConnection?) {
        try {
            val printer = EscPosPrinter(printerConnection, 203, 80f, 48,
                EscPosCharsetEncoding("UTF8", 16))
            printer
                .printFormattedTextAndCut(
//                    "[C]<img>" + PrinterTextParserImg.bitmapToHexadecimalString(
//                        printer,
//                        this.applicationContext.resources
//                            .getDrawableForDensity(R.drawable.logo, DisplayMetrics.DENSITY_MEDIUM)
//                    ).toString() + "</img>\n" +
                            "[L]\n" +
                            "[C]<u><font size='big'>ALW</font></u>\n" +
                            "[L]\n" +
                            "[C]==========================================\n" +
                            "[L]\n" +
                            "[L]<b>TEST สินค้าทดสอบ</b>[R]9.9999e\n"+
                            "[L]  + Size : S\n" +
                            "[L]\n" +
                            "[L]<b>AWESOME HAT</b>[R]24.99e\n" +
                            "[L]  + Size : 57/58\n" +
                            "[L]\n" +
                            "[C]--------------------------------\n" +
                            "[R]TOTAL PRICE :[R]34.98e\n" +
                            "[R]TAX :[R]4.23e\n" +
                            "[L]\n" +
                            "[C]================================\n" +
                            "[L]\n" +
                            "[L]<font size='tall'>Customer :</font>\n" +
                            "[L]Raymond DUPONT\n" +
                            "[L]5 rue des girafes\n" +
                            "[L]31547 PERPETES\n" +
                            "[L]Tel : +33801201456\n" //+
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