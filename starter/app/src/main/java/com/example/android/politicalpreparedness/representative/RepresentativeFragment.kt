package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale

class RepresentativeFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        //DONE: Add Constant for Location request
        private const val FINE_LOCATION_ACCESS_REQUEST_CODE = 1
    }

    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    //DONE: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRepresentativeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_representative, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        //DONE: Establish bindings
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        //DONE: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            viewModel.getRepresentatives()
        }

        binding.buttonLocation.setOnClickListener {
            if(checkLocationPermissions()){
                getLocation()
            } else {
                requestLocationPermission()
            }
        }

        //DONE: Define and assign Representative adapter
        val representativesAdapter = RepresentativeListAdapter()
        binding.representativesList.adapter = representativesAdapter

        //DONE: Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            representativesAdapter.submitList(it)
        })

        /*
        * Get the selected item of the spinner and update the address accordingly
        * */
        binding.state.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                viewModel.address.value?.state = binding.state.selectedItem as String
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.address.value?.state = binding.state.selectedItem as String
            }
        }
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //DONE: Handle location permission result to get location on permission granted
        when (requestCode) {
            FINE_LOCATION_ACCESS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                } else {
                    Toast.makeText(
                        context,
                        "Location permission has not been granted.",
                        Toast.LENGTH_LONG
                    ).show()

                }

            }

        }

    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //DONE: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat
            .checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation() {
        //DONE: Get location from LocationServices
        //DONE: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
        if(isPermissionGranted()){
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val userAddress = geoCodeLocation(location)
                        viewModel.updateAddressFromGeoLocation(userAddress)
                    }
                }
        } else {
            requestLocationPermission()
        }

    }

    private fun requestLocationPermission() {
        requestPermissions( arrayOf(ACCESS_FINE_LOCATION), FINE_LOCATION_ACCESS_REQUEST_CODE)
    }

    private fun geoCodeLocation(location: Location): Address {
        // Test location in the US, to check whether "Use My Location" works
        /*location.latitude = 43.041510
        location.longitude = -87.91740*/
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(
                    address.thoroughfare,
                    address.subThoroughfare,
                    address.locality,
                    address.adminArea,
                    address.postalCode
                )
            }
            .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}