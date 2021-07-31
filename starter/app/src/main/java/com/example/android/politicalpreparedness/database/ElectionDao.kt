package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.SavedElection

@Dao
interface ElectionDao {

    //DONE: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(vararg elections: Election)

    //DONE: Add select all election query
    @Query("select * from election_table")
    suspend fun getAllElections(): LiveData<List<Election>>

    //DONE: Add clear query
    @Query("delete from election_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveElection(id: SavedElection)


    //DONE: Add select single election query
    @Query("select * from election_table where id = :id")
    suspend fun getElectionById(id: Int) : Election?

    //DONE: Add delete query
    @Query("delete from election_table where id = :id")
    suspend fun deleteElectionById(id: Int)


}