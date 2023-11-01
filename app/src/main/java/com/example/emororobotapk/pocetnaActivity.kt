package com.example.emororobotapk

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothAdapter.ACTION_STATE_CHANGED
import android.bluetooth.BluetoothManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.emororobotapk.databinding.ActivityPocetnaBinding

class pocetnaActivity : AppCompatActivity() {
    lateinit var binding: ActivityPocetnaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPocetnaBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//bluetooth
        val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
           // startActivityForResult(enableBtIntent,)
        }
//nakon sto se spojio na bluetooth

        val stoji : Boolean
        stoji = false

        binding.forwardBtn.setOnClickListener {

            //Auto go napred

        }
        binding.reverseBtn.setOnClickListener {

            //auto go nazad

        }
        binding.stopBtn.setOnClickListener {

            //auto stop

        }
        binding.leftBtn.setOnClickListener {

            //auto go lijevo

        }
        binding.rightBtn.setOnClickListener {

            //auto go desno

        }






    }
}