package com.openclassrooms.realestatemanager.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estate")
class Estate(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        @ColumnInfo var category: String,
        @ColumnInfo var title: String,
        @ColumnInfo var address: String,
        @ColumnInfo var price: Long)
