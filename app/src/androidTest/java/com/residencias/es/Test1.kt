package com.residencias.es

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.residencias.es.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class Test1 : ResidencesTest() {

    @Test
    fun retrievesResidences() {
        //se prueba que el listado de residencias no este vacio
        runBlocking {
            val residences = apiService.getResidences()
            assert(!residences?.second.isNullOrEmpty()) {
                "El listado de residencias no debe estar vacío"
            }
        }
    }

    @Test
    fun listDisplaysResidencesProperties() {
        //se verifica que la primera residencia del listado tenga un nombre
        val scenario = ActivityScenario.launch(MainActivity::class.java)

          val residences = runBlocking {
              apiService.getResidences()?.second.orEmpty()
          }

          Thread.sleep(TestData.networkWaitingMillis)

          Espresso.onView(ViewMatchers.withText(residences.first().name ?: ""))
              .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        scenario.close()
    }

}