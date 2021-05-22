package com.residencias.es

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.residencias.es.CustomAssertions.Companion.hasItemCount
import com.residencias.es.ui.residence.ResidencesSearchActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.containsString
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class Test3 : ResidencesTest() {


    @Test
    fun searchResidences() {
        //se verifica que el buscador funcione, en el ActivitySearch, en el spinner de provincia, se selecciona Almería y en el
        //de municipio, se selecciona Albox, luego se hace click en buscar el cual nos envia al ActivityResidences y se verifica
        //que el recycle_view contenga una residencia

        val scenario = ActivityScenario.launch(ResidencesSearchActivity::class.java)

        Thread.sleep(TestData.networkWaitingMillis)

        Espresso.onView(withId(R.id.province)).perform(click())
        Espresso.onData(CoreMatchers.anything()).atPosition(3).perform(click())

        Thread.sleep(TestData.networkWaitingMillis)
        Espresso.onView(withId(R.id.province)).check(matches(withSpinnerText(containsString("Almería"))))

        Espresso.onView(withId(R.id.town)).perform(click())
        Espresso.onData(CoreMatchers.anything()).atPosition(3).perform(click())

        Thread.sleep(TestData.networkWaitingMillis)
        Espresso.onView(withId(R.id.town)).check(matches(withSpinnerText(containsString("Albox"))))

        Espresso.onView(withId(R.id.btn_search)).perform(click())

        onView(withId(R.id.recycler_view_residences)).check(hasItemCount(1))

        scenario.close()
    }
}

class CustomAssertions {
    companion object {
        fun hasItemCount(count: Int): ViewAssertion {
            return RecyclerViewItemCountAssertion(count)
        }
    }

    private class RecyclerViewItemCountAssertion(private val count: Int) : ViewAssertion {

        override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            if (view !is RecyclerView) {
                throw IllegalStateException("The asserted view is not RecyclerView")
            }

            if (view.adapter == null) {
                throw IllegalStateException("No adapter is assigned to RecyclerView")
            }

            ViewMatchers.assertThat("RecyclerView item count", view.adapter!!.itemCount, CoreMatchers.equalTo(count))
        }
    }
}