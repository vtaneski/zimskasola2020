package com.example.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val PERMISSION_ID = 1
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }

    /**
     *  The method uses the API and returns the last recorded location information of the device.
     *  Also this method will check first if our permission is granted or not and if the location setting is turned on.
     */
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        when (checkPermissions()) {
            true -> {
                if (isLocationEnabled()) {

                    mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                        val location: Location? = task.result
                        if (location == null) {
                            requestNewLocationData()
                        } else {
                            latTextView.text = location.latitude.toString()
                            lonTextView.text = location.longitude.toString()
                        }
                    }
                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }
            false -> {
                requestPermissions()
            }
        }
    }

    /**
     * for cases when the location is null
     * - when we turn the location off, then on
     * - when we access the last location with our app for the first time
     * it also sets the location update interval
     */
    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0            //10000 ms
            fastestInterval = 0     //5000 ms
            numUpdates = 1
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    /**
     * update callback from the requestNewLocationData()
     */
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latTextView.text = mLastLocation.latitude.toString()
            lonTextView.text = mLastLocation.longitude.toString()
        }
    }

    /**
     * This will check if the user has turned on location from the setting.
     * Cause user may grant the app to user location but if the location setting is off then it'll be of no use
     */
    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /**
     * Checks whether or not the user grant us to access ACCESS_COARSE_LOCATION and ACCESS_FINE_LOCATION.
     */
    private fun checkPermissions(): Boolean {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * This method will request our necessary permissions to the user if they are not already granted.
     */
    private fun requestPermissions() {
        /*if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            Toast.makeText(
                this,
                "Location permission is needed to show the current location",
                Toast.LENGTH_SHORT
            ).show()
        }*/
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )

    }

    /**
     * This method is called when a user Allow or Deny our requested permissions.
     * So it will help us to move forward if the permissions are granted.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Granted. Start getting the location information
                    getLastLocation()
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        Toast.makeText(
                            this,
                            "Location permission is needed to show the current location",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        //permission from popup was denied
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
