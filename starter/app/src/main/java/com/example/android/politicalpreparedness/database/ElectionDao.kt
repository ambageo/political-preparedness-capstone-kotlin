package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.SavedElection

@Dao
interface ElectionDao {

    //DONE: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(elections: List<Election>)

    //TODO: Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    //DONE: Add clear query
    @Query("delete from election_table")
    suspend fun deleteAll()

    @Query("insert into saved_election_table (id) values(:electionId)")
    suspend fun saveElection(electionId: Int)

    @Query("select exists(select * from saved_election_table where id = :electionId)")
    suspend fun isElectionSaved(electionId: Int): Boolean


    //DONE: Add select single election query
    @Query("select * from election_table where id = :id")
    suspend fun getElectionById(id: Int) : Election?

    //DONE: Add delete query
    @Query("delete from election_table where id = :id")
    suspend fun deleteElectionById(id: Int)


}