package br.edu.infnet.appexternalstorage

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun callWriteOnExternalStorage(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                callDialog(
                    "É preciso liberar WRITE_EXTERNAL_STORAGE",
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE
                )
            }
        } else {
            createDeleteFile()
        }
    }

    private val REQUEST_PERMISSIONS_CODE = 12800

    private fun callDialog(messagem: String, permissions: Array<String>) {
        var mDialog = AlertDialog.Builder(this)
            .setTitle("Permissão")
            .setMessage(messagem)
            .setPositiveButton("OK") { dialog, id ->
                ActivityCompat.requestPermissions(
                    this@MainActivity, permissions,
                    REQUEST_PERMISSIONS_CODE
                )
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, id ->
                dialog.dismiss()
            }
        mDialog.show()
    }

    private fun createDeleteFile() {
        val file = File(getExternalFilesDir(null), "DemoFile.txt")
        if (file.exists()) {
            file.delete()
        } else {
            try {
                val os: OutputStream = FileOutputStream(file)
                os.write("Pequeno Teste\nCom Quebra de Linha".toByteArray())
                os.close()
            } catch (e: IOException) {
                Log.d("Permissao", "Erro de escrita em arquivo")
            }
        }
    }

    fun callReadFromExternalStorage(view: View) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                callDialog(
                    "É preciso a liberar READ_EXTERNAL_STORAGE",
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_PERMISSIONS_CODE
                )
            }

        } else {
            readFile()
        }

    }

    private fun readFile() {
        val file = File(getExternalFilesDir(null), "DemoFile.txt")
        if (!file.exists()) {
            Toast.makeText(
                this@MainActivity,
                "Arquivo não encontrado",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val text = StringBuilder()

        try {
            val br = BufferedReader(FileReader(file))
            var line: String?

            while (br.readLine().also { line = it } != null) {

                text.append(line)
                text.append('\n')
            }
            br.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(
            this@MainActivity,
            text.toString(),
            Toast.LENGTH_SHORT
        ).show()

    }

    fun callAccessLocation(view: View) {
        val permissionAFL = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val permissionACL = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (permissionAFL != PackageManager.PERMISSION_GRANTED &&
            permissionACL != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                callDialog(
                    "É preciso liberar ACCESS_FINE_LOCATION",
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                )
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSIONS_CODE
                )
            }
        } else {
            readMyCurrentCoordinates()
        }


    }

    private fun readMyCurrentCoordinates() {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val isGPSEnabled = locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

        val isNetworkEnabled = locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )

        if (!isGPSEnabled && !isNetworkEnabled) {
            Toast.makeText(this, "Habilite o GPS e Rede!", Toast.LENGTH_LONG).show()
            Log.d("Permissao", "Ative os serviços necessários")
        } else {
            if (isGPSEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            } else if (isNetworkEnabled) {
                try {
                    locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        2000L, 0f, locationListener
                    )
                } catch (ex: SecurityException) {
                    Log.d("Permissao", "Security Exception")
                }
            }

        }

    }

    private val locationListener =
        object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Toast.makeText(
                    applicationContext,
                    "Lat: $location.latitude | Long: $location.longitude",
                    Toast.LENGTH_SHORT
                ).show()
            }



            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }
}


