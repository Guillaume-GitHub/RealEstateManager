package com.openclassrooms.realestatemanager.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.runner.RunWith
import androidx.test.InstrumentationRegistry
import androidx.room.Room
import com.openclassrooms.realestatemanager.model.Estate
import org.junit.After
import org.junit.Rule
import junit.framework.Assert.assertTrue


@RunWith(AndroidJUnit4::class)
class TestDaoEstate {


    // FOR DATA
    private var database: AppDatabase? = null
    // DATA SET FOR TEST
    private val ESTATE_ID: Long = 1
    private val ESTATE_DEMO = Estate(ESTATE_ID, "House", "Awesome House","4746  SW 13th Place, Deerfield, Florida 33442",999999)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    @Throws(Exception::class)
    fun initDb() {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    @Throws(Exception::class)
    fun closeDb() {
        this.database!!.close()
    }

    @org.junit.Test
    @Throws(InterruptedException::class)
    fun insertAndGetUser() {
        // BEFORE : Adding a new user
        database!!.estateDao().insertEstate(ESTATE_DEMO)
        // TEST
        val estate = LiveDataTestUtil.getValue(this.database!!.estateDao().getEstate(ESTATE_ID))
        assertTrue(estate.uid == ESTATE_DEMO.uid && estate.address == ESTATE_DEMO.address)
    }
}