package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize // This is so we can pass an Election as argument between fragments
@Entity(tableName = "election_table")
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
): Parcelable

/*
* Since all data we need for an Election is already stored in election_table, we can just save the
* id of an Election we want to follow */
@Entity(tableName = "saved_election_table")
data class FollowedElection(
        @PrimaryKey val id: Int
)
