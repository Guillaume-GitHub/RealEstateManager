package com.openclassrooms.realestatemanager.database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.ArrayList


class DataTransformer {

    // FOR DATE
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return if (date == null) null else date.time
    }

    @TypeConverter
    fun toDate(millisSinceEpoch: Long?): Date? {
        return if (millisSinceEpoch == null) null else Date(millisSinceEpoch)
    }

    // FOR LatLng Object
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return if  (latLng == null) null else String.format("%f,%f", latLng.latitude, latLng.longitude)
    }

    @TypeConverter
    fun toLatLng(latLng: String?): LatLng? {
        if (latLng == null) {
            return null
        }
        else {
            val splitArray = latLng.split(",")
            val latitude = splitArray[0].toDouble()
            val longitude = splitArray[1].toDouble()

            return LatLng(latitude,longitude)
        }
    }

    // FOR URI ARRAYLIST
    @TypeConverter
    fun fromUriList(uriList: ArrayList<Uri>?): String? {
        if (uriList == null) {
            return null
        }
        else {
            val list = uriList.toList()
            var result = ""

            list.forEachIndexed { index, uri ->
                result += uri.toString()
                if (index != list.size -1) result += ","
            }
            return result
        }
    }

    @TypeConverter
    fun toUriList(uriString: String?): ArrayList<Uri>? {
        if (uriString == null) {
            return null
        }
        else {
            val array = uriString.split(",")
            val arrayList = ArrayList<Uri>()

            for (element in array){
                arrayList.add(Uri.parse(element))
            }
            return arrayList
        }
    }

    // FOR URI
    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        else {
            return uri.toString()
        }
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        if (uriString == null) {
            return null
        }
        else {
            return Uri.parse(uriString)
        }
    }

    // FOR STRING ARRAYLIST
    @TypeConverter
    fun fromStringArrayList(stringList: ArrayList<String>?): String? {
        if (stringList == null) {
            return null
        }
        else {
            val list = stringList.toList()
            var result = ""

            list.forEachIndexed { index, str ->
                result += str
                if (index != list.size -1) result += ","
            }
            return result
        }
    }

    @TypeConverter
    fun toStringArrayList(string: String?): ArrayList<String>? {
        if (string == null) {
            return null
        }
        else {
            val array = string.split(",")
            val arrayList = ArrayList<String>()

            for (element in array){
                arrayList.add(element)
            }
            return arrayList
        }
    }

}