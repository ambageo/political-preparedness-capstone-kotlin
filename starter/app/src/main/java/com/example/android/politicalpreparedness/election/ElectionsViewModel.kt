package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)

    private val _navigateToVoterInfo = MutableLiveData<Election>()
    val navigateToVoterInfo: LiveData<Election>
    get() = _navigateToVoterInfo

    //DONE: Create live data val for upcoming elections
    //DONE: Create live data val for saved elections
    //DONE: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    val upcomingElections = repository.upcomingElections
    val savedElections = repository.followedElections

    init {
        viewModelScope.launch {
            repository.refreshElections()
        }

    }

    //TODO: Create functions to navigate to saved or upcoming election voter info
    fun navigateToVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun navigateToVoterInfoDone(){
        _navigateToVoterInfo.value = null
    }

}