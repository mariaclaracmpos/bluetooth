package com.example.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.bluetooth.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //viewBinding
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    //variáveis para habilitar e deixar disponível o Bluetooth
    private val REQUEST_CODE_ENABLE_BT = 1;
    private val REQUEST_CODE_DISCOVERABLE_BT = 2;

    //adaptador bluetooth
    private lateinit var bluetoothAdapter: BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //inicializar o adaptador do bluetooth
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            binding.bluetoothStatus.text = "Não disponível"
        } else {
            binding.bluetoothStatus.text = "Disponível"
        }

        //status bluetooth ON/OFF com imagem
        if (bluetoothAdapter.isEnabled) {
            binding.bluetoothImg.setImageResource(R.drawable.ic_bluetooth_on)
        } else {
            binding.bluetoothImg.setImageResource(R.drawable.ic_bluetooth_off)
        }

        //ação de clique para habilitar bluetooth
        binding.btnLigarB.setOnClickListener {
            if (bluetoothAdapter.isEnabled) {
                Toast.makeText(this, "Ligado", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@setOnClickListener
                }
                startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
            }

            //ação de clique para desabilitar o bluetooth
            binding.btnDesligarB.setOnClickListener {
                if (!bluetoothAdapter.isEnabled) {
                    Toast.makeText(this, "Ligado", Toast.LENGTH_SHORT).show()
                } else {
                    bluetoothAdapter.disable()
                    binding.bluetoothImg.setImageResource(R.drawable.ic_bluetooth_off)
                    Toast.makeText(this, "Bluetooth desligado", Toast.LENGTH_SHORT).show()
                }
            }

            //ação para deixar meu bluetooth visível
            binding.btnMeuB.setOnClickListener {
                if (!bluetoothAdapter.isDiscovering) {
                    Toast.makeText(this, "Este é meu dispositivo", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                    startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
                }
            }

            //listagem dos dispositivos que se conectaram ao meu
            binding.btnAparelhosC.setOnClickListener {
                if (bluetoothAdapter.isEnabled) {
                    binding.statusConectados.text = "Dispositivos conectados:"
                    val devices = bluetoothAdapter.bondedDevices
                    for (device in devices) {
                        val deviceName = device.name
                        val deviceAdress = device
                        binding.statusConectados.append(
                            "\n Dispositivos:" +
                                    "$deviceName $device")
                    }
                    }else {
                        Toast.makeText(this, "Favor ligar o bluetooth", Toast.LENGTH_SHORT).show()
                    }
                 }
            }
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
           when (requestCode) {
               REQUEST_CODE_ENABLE_BT ->
                   if (resultCode == Activity.RESULT_OK) {
                       binding.bluetoothImg.setImageResource(R.drawable.ic_bluetooth_on)
                       Toast.makeText(this, "Bluetooth ligado", Toast.LENGTH_SHORT).show()
                   } else{
                       Toast.makeText(this, "Bluetooth desligado", Toast.LENGTH_SHORT).show()
                   }
           }
            super.onActivityResult(requestCode, resultCode, data)
        }
        }
    }