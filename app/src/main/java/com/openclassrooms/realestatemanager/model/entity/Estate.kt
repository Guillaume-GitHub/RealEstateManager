package com.openclassrooms.realestatemanager.model.entity

import android.content.ContentValues
import android.net.Uri
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "estate")
class Estate(
        @PrimaryKey(autoGenerate = true) var estateUid: Long,
        @ColumnInfo var category: String,
        @ColumnInfo var title: String,
        @ColumnInfo var description: String,
        @ColumnInfo var surface: Int,
        @ColumnInfo var nbRoom: Int,
        @ColumnInfo var price: Long,
        @ColumnInfo var publishedDate: Date,
        @ColumnInfo var saleDate: Date?,
        @ColumnInfo var filters: ArrayList<String>?,
        @ColumnInfo var images: ArrayList<Uri>,
        @Embedded var locality: Locality,
        @Embedded var agent: Agent){

    // FOR CONTENT PROVIDER
    companion object{

        lateinit var estate: Estate

        fun fromContentValues(values: ContentValues): Estate {
            if (values.containsKey("estateUid")) estate.estateUid = values.getAsLong("estateUid")
            if (values.containsKey("category")) estate.category = values.getAsString("category")
            if (values.containsKey("title")) estate.title = values.getAsString("title")
            if (values.containsKey("address")) estate.locality.formattedAddress = values.getAsString("address")
            if (values.containsKey("price")) estate.price = values.getAsLong("price")

            return estate
        }
    }
}
