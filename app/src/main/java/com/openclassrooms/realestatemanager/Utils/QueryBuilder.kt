package com.openclassrooms.realestatemanager.Utils

import android.content.Intent
import android.util.Log

class QueryBuilder {

    companion object{
        private const val BASE_SQL_QUERY = "SELECT * FROM ESTATE"
        private const val ORDER_BY_QUERY = "ORDER BY publishedDate DESC"
    }
    private lateinit var keyList: ArrayList<String>

    private var containWhereCondition = false
    private var stringQuery = BASE_SQL_QUERY

    fun getQuery(data: Intent): String {

        val extras = data.extras

        if(extras != null) {

            val keys = extras.keySet()
            val iterator: Iterator<String> = keys.iterator()
            keyList = ArrayList()

            while (iterator.hasNext()) {
                val key = iterator.next()
                Log.d("KEY -> ", "[$key = ${extras.get(key)}]")
                keyList.add(key)
            }
        }

        if (keyList.size > 0){

            // "query" filter (text in searchview)
            if (keyList.contains("query")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " title LIKE '%${extras?.get("query")}%' OR description LIKE '%${extras?.get("query")}%'"
            }

            // Price VALUES (range bar values)
            if (keyList.contains("min_price") || keyList.contains("max_price")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " price BETWEEN ${extras?.get("min_price")} AND ${extras?.get("max_price")}"
            }

            // Surface VALUES (range bar values)
            if (keyList.contains("min_surface") || keyList.contains("max_surface")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " surface BETWEEN ${extras?.get("min_surface")} AND ${extras?.get("max_surface")}"
            }

            // Room VALUES (range bar values)
            if (keyList.contains("min_room") || keyList.contains("max_room")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }
                this.stringQuery += " nbRoom BETWEEN ${extras?.get("min_room")} AND ${extras?.get("max_room")}"
            }

            // filters nearby chip tags
            if (keyList.contains("filter_array")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val tagList = extras?.getStringArrayList("filter_array")
                var stringFilters = String()

                tagList?.forEachIndexed { index, string ->
                    if (index != tagList.size -1) stringFilters += "'%$string%' OR"
                    else stringFilters += "'%$string%'"
                }
                this.stringQuery += " filters LIKE $stringFilters"
            }

            // Category filters
            if (keyList.contains("category_array")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val catList = extras?.getStringArrayList("category_array")
                var stringCat = String()

                catList?.forEachIndexed { index, string ->
                    if (index != catList.size -1) stringCat += "'%$string%' OR"
                    else stringCat += "'%$string%'"
                }
                this.stringQuery += " category LIKE $stringCat"
            }

            // Location filters
            if (keyList.contains("location_array")) {
                if (containWhereCondition) {
                    stringQuery += " AND"
                } else {
                    stringQuery += " WHERE"
                    this.containWhereCondition = true
                }

                val locationList = extras?.getStringArrayList("location_array")
                var stringLocation = String()

                locationList?.forEachIndexed { index, locationString ->
                    if (index != locationList.size -1) stringLocation += " cities LIKE '%$locationString%' OR"
                    else stringLocation += " cities LIKE '%$locationString%'"
                }
                    this.stringQuery += stringLocation
            }
        }

        this.stringQuery += " $ORDER_BY_QUERY ;"

        Log.d(this.javaClass.simpleName , this.stringQuery)
        return this.stringQuery
    }
}