package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.FollowedElection

@Dao
interface ElectionDao {

    //DONE: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(elections: List<Election>)

    //DONE: Add select all election query
    @Query("select * from election_table")
    fun getAllElections(): LiveData<List<Election>>

    //DONE: Add clear query
    @Query("delete from election_table")
    suspend fun deleteAll()

    @Query("select * from election_table where id in (select id from saved_election_table) order by electionDay desc")
    fun getFollowedElections(): LiveData<List<Election>>

    @Query("insert into saved_election_table (id) values(:electionId)")
    suspend fun followElection(electionId: Int)

    @Query("select exists(select * from saved_election_table where id = :electionId)")
    suspend fun isElectionFollowed(electionId: Int): Boolean


    //DONE: Add select single election query
    @Query("select * from election_table where id = :id")
    suspend fun getElection(id: Int) : Election?

    //DONE: Add delete query
    @Query("delete from saved_election_table where id = :id")
    suspend fun deleteElection(id: Int)


}