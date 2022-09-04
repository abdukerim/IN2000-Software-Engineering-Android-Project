package kerim.android.bade

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITest {
    @Test
    fun ListFragmentTest() {
        launchFragmentInContainer<ListFragment>()
        onView(withId(R.id.my_spinner)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.SistOppdatert)).check(matches(withText("Sist oppdatert")))
    }
    @Test
    fun useAppContext() {

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("gruppe39.android.in2000.prosjekt", appContext.packageName)
    }
    @Test
    fun navigationTest() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.btn_list)).perform(click())
        onView(withId(R.id.main)).check(matches(ViewMatchers.isDisplayed()))

        onView(withId(R.id.btn_map)).perform(click())
        onView(withId(R.id.map)).check(matches(ViewMatchers.isDisplayed()))

    }
}