package com.example.android.politicalpreparedness.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    val apiService: CivicsApiService = CivicsApi.retrofitService

    suspend fun refreshElections(){
        withContext(Dispatchers.IO) {
            val electionsList = apiService.getElections()
            database.electionDao.insertAllElections(electionsList.elections)
        }
    }

    val upcomingElections: LiveData<List<Election>> = database.electionDao.getAllElections()


}