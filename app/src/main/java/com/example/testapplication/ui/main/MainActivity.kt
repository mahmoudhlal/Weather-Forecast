package com.example.testapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.testapplication.R
import com.example.testapplication.core.BaseActivity
import com.example.testapplication.databinding.ActivityMainBinding
import com.example.testapplication.model.Location
import com.example.testapplication.utils.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    private val locationSettingsScreen =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            getCurrentLocation()
        }
    private var mBinding : ActivityMainBinding ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        getCurrentLocationWithPermissionCheck()

        setupObserver()
    }

    private fun setupObserver() {
        mainViewModel.forecast.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    mBinding?.item = it.data
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION)
    fun getCurrentLocation() {
        if (isLocationEnabled()) {
            getLastKnownLocation {
                //call weather api to get restaurants
                val currentLatLng = Location(it.latitude ,it.longitude)
                mainViewModel.getWeather("",currentLatLng)
            }
        } else {
            MaterialAlertDialogBuilder(this).apply {
                    setCancelable(false)
                    setTitle(getString(R.string.location_not_enabled))
                    setMessage(getString(R.string.enable_location))
                    setPositiveButton(getString(R.string.enable)) { dialog, _ ->
                        // open settings screen
                        openSettingsScreen()
                        dialog.dismiss()
                    }
                    setNegativeButton(getString(R.string.deny)) { dialog, _ ->
                        mainViewModel.getWeather()
                        dialog.dismiss()
                    }
                    show()
                }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        this.onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun openSettingsScreen() {
        locationSettingsScreen.launch(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

}