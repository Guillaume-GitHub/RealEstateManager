package com.openclassrooms.realestatemanager.model.entity

import androidx.room.*
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "locality")
class Locality(
        @PrimaryKey(autoGenerate = true) var localityUid: Long,
        @ColumnInfo var formattedAddress: String,
        @ColumnInfo var streetNumber: String?,
        @ColumnInfo var route: String,
        @ColumnInfo var postalCode: String?,
        @ColumnInfo var cities: String,
        @ColumnInfo var country: String,
        @ColumnInfo var latLng: LatLng)

