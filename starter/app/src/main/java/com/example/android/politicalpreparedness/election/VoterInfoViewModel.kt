package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavArgsLazy
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.repository.ElectionsRepository
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class VoterInfoViewModel(private val args: NavArgsLazy<VoterInfoFragmentArgs>, application: Application) : AndroidViewModel(application) {

    private val database = ElectionDatabase.getInstance(application)
    private val repository = ElectionsRepository(database)
    private val electionId = args.value.argElectionId

    //DONE: Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
    get() = _voterInfo

    private var _hasVoterInfo = MutableLiveData<Boolean>()
    val hasVoterInfo: LiveData<Boolean>
    get() = _hasVoterInfo

    //DONE: Add var and methods to support loading URLs
    private var _votingLocationUrl = MutableLiveData<String>()
    val votingLocationUrl: LiveData<String>
        get()= _votingLocationUrl

    private var _ballotInformationUrl = MutableLiveData<String>()
    val ballotInformationUrl: LiveData<String>
        get() = _ballotInformationUrl

    private var _isElectionFollowed = MutableLiveData<Boolean>()
    val isElectionFollowed: LiveData<Boolean>
    get() = _isElectionFollowed

     fun loadVotingLocationUrl(){
        _votingLocationUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.votingLocationFinderUrl
    }

    fun loadBallotInformationUrl(){
        _ballotInformationUrl.value = _voterInfo.value?.state?.get(0)?.electionAdministrationBody?.ballotInfoUrl
    }

    //DONE: Add var and methods to populate voter info
    init {
        viewModelScope.launch {
            getVoterInfo()
            checkIfFollowingElection()
        }
    }

    private fun getVoterInfo() {
        val country = args.value.argDivision.country
        val state = args.value.argDivision.state

        if (state.isNotEmpty()) {
            val address = "$country,$state"
            viewModelScope.launch {
                repository.getVoterInfo(address, electionId)
                _voterInfo.value = repository.voterInfo
            }
            _hasVoterInfo.value = true
        } else {
           _hasVoterInfo.value = false
        }
    }

    //DONE: Populate initial state of save button to reflect proper action based on election saved status
    private fun checkIfFollowingElection() {
        val electionId =args.value.argElectionId
        viewModelScope.launch {
            repository.isElectionFollowed(electionId)
            _isElectionFollowed.value = repository.isElectionFollowed
        }
    }

    fun votingLocationCompleted() {
       _votingLocationUrl.value = null
    }

    fun ballotInformationCompleted() {
       _ballotInformationUrl.value = null
    }


    //DONE: Add var and methods to save and remove elections to local database
    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun toggleFollowElection() = viewModelScope.launch {
        if(isElectionFollowed.value == true){
           repository.deleteElection(electionId)
            _isElectionFollowed.value = false
        } else {
            repository.saveElection(electionId)
            repository.getElection(electionId)
            _isElectionFollowed.value = true
        }

    }

}