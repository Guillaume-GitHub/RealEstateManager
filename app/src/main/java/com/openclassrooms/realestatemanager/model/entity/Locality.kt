package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "locality")
class Locality(
        @PrimaryKey(autoGenerate = true) var localityUid: Long,
        @ColumnInfo var formattedAddress: String,
        @ColumnInfo var streetNumber: String?,
        @ColumnInfo var route: String,
        @ColumnInfo var postalCode: String?,
        @ColumnInfo var cities: String,
        @ColumnInfo var country: String)
