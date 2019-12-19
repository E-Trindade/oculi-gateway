package com.oculi

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(this.applicationContext, "Error getting bluetooth adapter", Toast.LENGTH_LONG).show()
            return
        }
        if (!bluetoothAdapter?.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        ConnectThread(bluetoothAdapter).start()

//        val devices = bluetoothAdapter.getBondedDevices()
//        if (devices != null) {
//            for (device in devices!!) {
//                Log.println(Log.INFO, "aaa", device.name + " " + device.address)
//                if(device.name == "lain"){
//                    Log.println(Log.INFO, "aaa", "starting")
//
//                    bluetoothAdapter?.cancelDiscovery()
//
//
//                }
//            }
        }
//        val deviceName = device.name
//        val deviceHardwareAddress = device.address

//    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        // Check which request we're responding to
//        if (requestCode == 1) {
//            // Make sure the request was successful
//            if (resultCode == Activity.RESULT_OK) {
//
//            }
//        }
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        //
//        Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        return when(item.itemId) {
//            R.id.action_settings -> true
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    private inner class ConnectThread(bluetoothAdapter: BluetoothAdapter) : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingRfcommWithServiceRecord("My Service", UUID.fromString("315c4520-e7a0-4426-91e0-2a82f13993f7"))
        }

//        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
//            device.createRfcommSocketToServiceRecord(UUID.fromString("7ce20676-0b4b-11ea-8d71-362b9e155667"))
//        }

        override fun run() {
            Log.println(Log.INFO, "aaa", "Running")
            var shouldLoop = true
            while (shouldLoop) {
                Log.i("aaa", "Waiting for connections")

                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.e("aaa", "Socket's accept() method failed", e)
                    shouldLoop = false
                    null
                }

                socket?.also {
                    Log.i("aaa", "Accepted")

                    val reader = BufferedReader(InputStreamReader(it.inputStream))

                    val sb = StringBuilder()
                    while(true) {

                        val msg = reader.readLine()
                        //Log.println(Log.INFO, "aaa", "received_msg " + msg)
                        if(msg == "END_IMAGE"){
                            break
                        }

                        sb.append(msg)
                    }

//                    Log.println(Log.INFO, "aaa", sb.toString())
                    Log.println(Log.INFO, "aaa", "finished")
                    Log.println(Log.INFO, "aaa", sb.toString().length.toString())

                    mmServerSocket?.close()
                    shouldLoop = false
                }
            }

//                socket.outputStream.write("something\n".toByteArray())
            }
        }

        // Closes the client socket and causes the thread to finish.
//        fun cancel() {
//            try {
//                mmServerSocket?.close()
//            } catch (e: IOException) {
//                Log.e("aaa", "Could not close the client socket", e)
//            }
//        }
    }
//}
