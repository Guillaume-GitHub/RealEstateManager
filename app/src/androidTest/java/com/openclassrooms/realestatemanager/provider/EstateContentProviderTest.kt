package com.openclassrooms.realestatemanager.provider

import org.junit.Before

import org.junit.Assert.*
import android.content.ContentUris
import androidx.test.InstrumentationRegistry
import androidx.room.Room
import android.content.ContentResolver
import com.openclassrooms.realestatemanager.database.AppDatabase
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Test


class EstateContentProviderTest {

    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    // DATA SET FOR TEST
    private val NB_MAX_ESTATES: Long = 10

    private val RESULT: Int = 0

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        mContentResolver = InstrumentationRegistry.getContext().contentResolver
    }

    @Test
    fun getItemsWhenNoItemInserted() {
        val  cursor = mContentResolver?.query(ContentUris.withAppendedId(EstateContentProvider().URI_ITEM, NB_MAX_ESTATES), null, null, null, null)
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, CoreMatchers.`is`(RESULT) )
        cursor.close()
    }
}