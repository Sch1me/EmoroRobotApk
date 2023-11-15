package com.example.emororobotapk


import android.Manifest
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.emororobotapk.databinding.ActivityPocetnaBinding
import java.io.IOException
import java.util.UUID

class pocetnaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPocetnaBinding
    companion object{
        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var m_bluetoothSocket: BluetoothSocket? = null
        lateinit var m_progress: ProgressDialog
        lateinit var m_bluetoothAdapter: BluetoothAdapter
        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPocetnaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        m_address = intent.getStringExtra(MainActivity.EXTRA_ADRESS).toString()

        ConnectToDevice(this).execute()

//kontrole za autic, jos prilagodit po arduino kodu
      //  binding.controlLedOn.setOnClickListener {  sendCommand("") }
      // binding.controlLedOff.setOnClickListener {  sendCommand("") }
        binding.forwardBtn.setOnClickListener { sendCommand("F") }
        binding.reverseBtn.setOnClickListener { sendCommand("B") }
        binding.rightBtn.setOnClickListener { sendCommand("R") }
        binding.leftBtn.setOnClickListener { sendCommand("L") }
        binding.upLeftBtn.setOnClickListener { sendCommand("UL") }
        binding.upRightBtn.setOnClickListener { sendCommand("UR") }
        binding.downLeftBtn.setOnClickListener { sendCommand("DL") }
        binding.downRightBtn.setOnClickListener { sendCommand("DR") }
        binding.stopBtn.setOnClickListener { sendCommand("") }
        binding.disconectBtn.setOnClickListener {  disconnect() }

    }

    private fun sendCommand(input: String){
        if(m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }
    private fun disconnect(){
        if(m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.close()
                m_bluetoothSocket = null
                m_isConnected = false
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        finish()
    }
    private class ConnectToDevice(c:Context): AsyncTask<Void, Void, String>(){
        private var connectSuccess: Boolean = true
        private val context: Context
        init {
            this.context = c
        }

        override fun onPreExecute() {
            super.onPreExecute()
            m_progress = ProgressDialog.show(context,"Connecting...","plase wait")
        }

        override fun doInBackground(vararg params: Void?): String? {
            try {
                if(m_bluetoothSocket == null || !m_isConnected){
                    m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                    val device:BluetoothDevice = m_bluetoothAdapter.getRemoteDevice(m_address)

                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) { return "" }

                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery()
                    m_bluetoothSocket!!.connect()
                }
            }catch (e: IOException){
                connectSuccess = false
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if(!connectSuccess){
                Log.i("data","couldn't connect")
            }else{
                m_isConnected = true
            }
            m_progress.dismiss()
        }
    }
}