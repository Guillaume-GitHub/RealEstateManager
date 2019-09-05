package com.openclassrooms.realestatemanager.Utils

import android.content.Intent
import android.util.Log

class QueryBuilder {

    companion object{
        private const val BASE_SQL_QUERY = "SELECT * FROM ESTATE"
        private const val ORDER_BY_QUERY = "ORDER BY publishedDate DESC"

        fun getCategoryQuery(categoryName: String): String{
            return "$BASE_SQL_QUERY WHERE category LIKE '$categoryName' $ORDER_BY_QUERY"
        }
    }

    private var containWhereCondition = false
    private var stringQuery = BASE_SQL_QUERY

    fun getQuery(data: Intent): String {

        if (data.extras != null){

            val extras = data.extras!!

            // "query" filter (text in searchview)
            if (extras.get("query") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " (title LIKE '%${extras.get("query")}%' OR description LIKE '%${extras.get("query")}%')"
            }

            // Price VALUES (range bar values)
            if (extras.get("min_price") != null || extras.get("max_price") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " (price BETWEEN ${extras.get("min_price")} AND ${extras.get("max_price")})"
            }

            // Surface VALUES (range bar values)
            if (extras.get("min_surface") != null || extras.get("max_surface") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " (surface BETWEEN ${extras.get("min_surface")} AND ${extras.get("max_surface")})"
            }

            // Room VALUES (range bar values)
            if (extras.get("min_room") != null || extras.get("max_room") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " (nbRoom BETWEEN ${extras.get("min_room")} AND ${extras.get("max_room")})"
            }

            // filters nearby chip tags
            if (extras.get("filter_array") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val tagList = extras.getStringArrayList("filter_array")
                var stringFilters = String()

                tagList?.forEachIndexed { index, string ->
                    if (index != tagList.size -1) stringFilters += " filters LIKE '$string' OR "
                    else stringFilters += "filters LIKE '$string'"
                }
                this.stringQuery += " ($stringFilters)"
            }

            // Category filters
            if (extras.get("category_array") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val catList = extras.getStringArrayList("category_array")
                var stringCat = String()

                catList?.forEachIndexed { index, string ->
                    if (index != catList.size -1) stringCat += "category LIKE '$string' OR "
                    else stringCat += "category LIKE '$string'"
                }
                this.stringQuery += " ($stringCat)"
            }

            // Location filters
            if (extras.get("location_array") != null) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val locationList = extras.getStringArrayList("location_array")
                var stringLocation = String()

                locationList?.forEachIndexed { index, locationString ->
                    if (index != locationList.size -1) stringLocation += " cities LIKE '%$locationString%' OR"
                    else stringLocation += " cities LIKE '%$locationString%'"
                }
                    this.stringQuery += " ($stringLocation)"
            }
        }

        this.stringQuery += " $ORDER_BY_QUERY ;"

        Log.d(this.javaClass.simpleName , this.stringQuery)
        return this.stringQuery
    }
}