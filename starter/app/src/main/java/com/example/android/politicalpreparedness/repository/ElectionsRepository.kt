package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class ElectionsRepository(private val database: ElectionDatabase) {

    val apiService: CivicsApiService = CivicsApi.retrofitService
    lateinit var voterInfo: VoterInfoResponse
    var isElectionFollowed by Delegates.notNull<Boolean>()
    lateinit var election: Election

    val upcomingElections: LiveData<List<Election>> = database.electionDao.getAllElections()
    val followedElections: LiveData<List<FollowedElection>> = database.electionDao.getFollowedElections()

    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            val electionsList = apiService.getElections()
            database.electionDao.insertAllElections(electionsList.elections)
        }
    }

    suspend fun getVoterInfo(address: String, electionId: Int) {
        withContext(Dispatchers.IO) {
            voterInfo = apiService.getVoterInfo(electionId, address)
        }
    }

    suspend fun saveElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.followElection(electionId)
        }
    }

    suspend fun deleteElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            database.electionDao.deleteElection(electionId)
        }
    }

    suspend fun getElection(electionId: Int) {
        withContext(Dispatchers.IO) {
            election = database.electionDao.getElection(electionId)!!
        }
    }

    suspend fun isElectionFollowed(electionId: Int) {
        withContext(Dispatchers.IO) {
            isElectionFollowed = database.electionDao.isElectionFollowed(electionId)
        }
    }


}