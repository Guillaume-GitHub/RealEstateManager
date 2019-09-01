package com.openclassrooms.realestatemanager.model.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "draft")
class Draft(@PrimaryKey(autoGenerate = true) var draftUid: Long,
            @ColumnInfo var title: String,
            @ColumnInfo var description: String?,
            @ColumnInfo var type: String?,
            @ColumnInfo var address: String?,
            @ColumnInfo var surface: Int?,
            @ColumnInfo var rooms: Int?,
            @ColumnInfo var price: Long?,
            @ColumnInfo var images: ArrayList<Uri>?,
            @ColumnInfo var filters: ArrayList<String>?,
            @ColumnInfo var lastModification: Date)