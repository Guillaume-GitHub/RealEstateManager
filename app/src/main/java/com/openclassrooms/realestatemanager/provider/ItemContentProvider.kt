package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.AppDatabase
import com.openclassrooms.realestatemanager.model.Estate


class ItemContentProvider: ContentProvider() {

    // FOR DATA
    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"
    val TABLE_NAME = Estate::class.java.simpleName
    val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    override fun onCreate(): Boolean {
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        if (context != null){
            val nbOfEstates = ContentUris.parseId(uri)
            val cursor = AppDatabase.getInstance(context)?.estateDao()?.getEstatesWithCursor(nbOfEstates)
            cursor?.setNotificationUri(context.contentResolver, uri)
            return cursor
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
       throw IllegalAccessException("Impossible to update, update are not allow")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        throw IllegalAccessException("Impossible to delete, delete are not allow")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }
}