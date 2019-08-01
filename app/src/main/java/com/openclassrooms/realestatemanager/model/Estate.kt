package com.openclassrooms.realestatemanager.model

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estate")
class Estate(
        @PrimaryKey(autoGenerate = true) var uid: Long,
        @ColumnInfo var category: String,
        @ColumnInfo var title: String,
        @ColumnInfo var address: String,
        @ColumnInfo var price: Long){

    // FOR CONTENT PROVIDER
    companion object{

        lateinit var estate: Estate

        fun fromContentValues(values: ContentValues): Estate{
            if (values.containsKey("uid")) estate.uid = values.getAsLong("uid")
            if (values.containsKey("category")) estate.category = values.getAsString("category")
            if (values.containsKey("title")) estate.title = values.getAsString("title")
            if (values.containsKey("address")) estate.address = values.getAsString("address")
            if (values.containsKey("price")) estate.price = values.getAsLong("price")

            return estate
        }
    }
}
