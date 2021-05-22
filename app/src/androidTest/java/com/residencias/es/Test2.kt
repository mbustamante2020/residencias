package com.residencias.es


import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.residencias.es.ui.MainActivity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class Test2: ResidencesTest() {


    @Test
    fun retrievesNextPage() {
        //se verifica que la paginación funcione correctamente
        runBlocking {
            val firstPage = apiService.getResidences()
            val page = firstPage?.first
            assert(page != null) {
                "El número de la página no puede ser null"
            }
            val nextPage = apiService.getResidences(page)
            assert(firstPage?.first != nextPage?.first) {
                "La siguiente página no puede ser igual a la anterior ${firstPage?.first} ${nextPage?.first}"
            }
        }
    }

    @Test
    fun recyclerViewBottomScrollLoadsMoreResidences() {
        //se verifica que al hacer scroll se cargan más residencia en el recycler_view
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        var previousCount = 0
        var recyclerView: RecyclerView? = null

        Thread.sleep(TestData.networkWaitingMillis)
        scenario.onActivity {
            recyclerView = it.findViewById(R.id.recycler_view_residences)
            assert(recyclerView != null && recyclerView!!.adapter != null) {
                "Recyclerview y Adapter no puede ser nulos"
            }
            previousCount = recyclerView!!.adapter!!.itemCount
            assert(previousCount > 0) {
                "Adapter no puede estar vacío"
            }
        }

        onView(withId(R.id.recycler_view_residences)).perform(
                RecyclerViewActions
                        .scrollToPosition<RecyclerView.ViewHolder>(previousCount - 1)
        )

        Thread.sleep(TestData.networkWaitingMillis)

        val currentCount = recyclerView!!.adapter!!.itemCount
        assert(currentCount > previousCount) {
            "No se agregaron más residencias: Anterior $previousCount  - Actual: $currentCount"
        }

        scenario.close()
    }

}