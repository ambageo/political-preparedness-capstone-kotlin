package com.example.android.politicalpreparedness.network.models

import androidx.room.*
import com.squareup.moshi.*
import java.util.*

@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
)

/*
* Since all data we need for an Election is already stored in election_table, we can just save the
* id of an Election we want to follow */
@Entity(tableName = "saved_election_table")
data class SavedElection(
        @PrimaryKey val id: Int
)

// TODO: Add extension function to convert from Database Object to Domain Object