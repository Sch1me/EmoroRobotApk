package com.example.emororobotapk

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast

import androidx.core.app.ActivityCompat
import com.example.emororobotapk.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var  m_BluetoothAdapter: BluetoothAdapter? = null
    private lateinit var  m_PairedDevices: Set<BluetoothDevice>
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private lateinit var binding: ActivityMainBinding
    companion object{
        val EXTRA_ADRESS: String = "Device_adress"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_BluetoothAdapter == null){
            Toast.makeText(this, "This device doesnt support blutut", Toast.LENGTH_SHORT).show()
            return
        }
        if(!m_BluetoothAdapter!!.isEnabled){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
        }

        binding.selectDeviceRefresh.setOnClickListener { PairedDeviceList() }

    }
    private fun PairedDeviceList(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        m_PairedDevices = m_BluetoothAdapter!!.bondedDevices
        val list : ArrayList<BluetoothDevice> = ArrayList()
        if(!m_PairedDevices.isEmpty()){
            for(device: BluetoothDevice in m_PairedDevices){
                list.add(device)
                Log.i("device",device.toString())
            }
        }else{
            Toast.makeText(this, "No pared devicec found", Toast.LENGTH_SHORT).show()
        }

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1, list)
        binding.selectDeviceList.adapter = adapter
        binding.selectDeviceList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list[position]
            val address: String = device.address

            val intent = Intent(this, pocetnaActivity::class.java)
            intent.putExtra(EXTRA_ADRESS, address)
            startActivity(intent)
        }



    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data : Intent?){
        super.onActivityResult(requestCode,resultCode,data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_BluetoothAdapter!!.isEnabled){
                    Toast.makeText(this, "Blutut has bin enabled", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "blutut has ben disabled", Toast.LENGTH_SHORT).show()
                }
            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Blutut enabeling has bin canceled", Toast.LENGTH_SHORT).show()
            }
        }

    }
}