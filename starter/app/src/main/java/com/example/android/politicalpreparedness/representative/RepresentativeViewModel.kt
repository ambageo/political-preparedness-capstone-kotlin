package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import java.util.*

class RepresentativeViewModel: ViewModel() {

    //TODO: Establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
    get() = _address
    //val address: Address = Address("", "", "", "", "")

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    val apiService: CivicsApiService = CivicsApi.retrofitService

    init {
        _address.value = Address("" ,"", "", "", "")
    }
    //TODO: Create function to fetch representatives from API from a provided address

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :

    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }

    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives

     */
    fun getRepresentatives() {
       Log.d("ggg", "address: ${_address.value?.toFormattedString()}")
        Log.d("ggg", "state: ${_address.value?.state}")
        viewModelScope.launch {
            _address.value?.let {
                val response = apiService.getRepresentatives(it.toFormattedString())
                val offices = response.offices
                val officials = response.officials

                val representativesList = mutableListOf<Representative>()

                offices.forEach { office ->
                    representativesList.addAll(office.getRepresentatives(officials))
                }
                _representatives.value = representativesList
                Log.d("ggg", "representatives: ${representatives.value?.size}")
            }
        }
    }

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields

}
