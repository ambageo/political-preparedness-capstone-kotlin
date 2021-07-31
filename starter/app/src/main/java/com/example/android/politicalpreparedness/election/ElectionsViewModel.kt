package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)

    //TODO: Create live data val for upcoming elections
    val upcomingElections = MutableLiveData<Election>()
    val savedElections = MutableLiveData<Election>()

    init {
        viewModelScope.launch {
            repository.refreshElections()
        }
    }
    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

}