package kerim.android.bade

import androidx.test.ext.junit.runners.AndroidJUnit4
import kerim.android.bade.api.DataSource
import kerim.android.bade.data.Tsery
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Before




/**
     * Running tests on our app
     */
    @RunWith(AndroidJUnit4::class)
    class ApplicationTesting {
        private lateinit var dataSource: DataSource

        @Before
        fun createDataSource() { // creates the data source object we wish to test
            dataSource = DataSource()
        }

        @Test
        fun testHavvarselFrost() { // tests if our api calls work
            var list : List<Tsery>?

            // fetch our list of tsery objects from data source
            runBlocking {
                list = dataSource.fetchBadeInfo()
            }

            // check if the list actually got data
            assertNotEquals(list, null)
        }
    }