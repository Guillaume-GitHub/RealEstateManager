package com.openclassrooms.realestatemanager.model.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agent")
class Agent(@PrimaryKey(autoGenerate = true) var uid: Long,
            @ColumnInfo var name: String,
            @ColumnInfo var surname: String,
            @ColumnInfo var image: Uri?) {
}