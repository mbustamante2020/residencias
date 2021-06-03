package com.residencias.es.ui.residence

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.data.residence.model.Search
import com.residencias.es.databinding.FragmentResidencesBinding
import com.residencias.es.ui.residence.adapter.ResidencesAdapter
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.ResidencesViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class ResidencesFragment ( private var search: Search?): Fragment(R.layout.fragment_residences) {

    private var _binding: FragmentResidencesBinding? = null
    private val binding get() = _binding!!

    private var layoutManager: RecyclerView.LayoutManager? = null
    private val residencesViewModel: ResidencesViewModel by viewModel()
    private var adapter: ResidencesAdapter? = null

    private var nextPage: Int? = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResidencesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        _binding?.recyclerViewResidences.apply {

            // Init RecyclerView
            initRecyclerView()

            // Swipe to Refresh Listener
            binding.swipeRefreshLayout.setOnRefreshListener {
                getResidences(nextPage)
            }
            // Get Residences
            getResidences(nextPage)

            observeResidences()

            observeVerResidence()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////
    private fun initRecyclerView() {

        // set a LinearLayoutManager to handle Android
        // RecyclerView behavior
        layoutManager = LinearLayoutManager(activity)
        // set the custom adapter to the RecyclerView
        adapter = ResidencesAdapter(residencesViewModel)
        Log.i("ResidencesFragment", "76")

        // Set Layout Manager
        binding.recyclerViewResidences.layoutManager = layoutManager
        // Set Adapter
        binding.recyclerViewResidences.adapter = adapter
        // Set Pagination Listener
        binding.recyclerViewResidences.addOnScrollListener(object : PaginationScrollListener(layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                getResidences(nextPage)
            }

            override fun isLastPage(): Boolean {
                return nextPage == null
            }

            override fun isLoading(): Boolean {
                return binding.swipeRefreshLayout.isRefreshing
            }
        })
    }



    private fun getResidences(nextPage: Int? = null) {
        // Show Loading
        binding.swipeRefreshLayout.isRefreshing = true

        // Get Residences
        lifecycleScope.launch {
            try {
                residencesViewModel.getResidences(nextPage, search)

                // Hide Loading
                binding.swipeRefreshLayout.isRefreshing = false

            } catch (t: UnauthorizedException) {
                // Clear local access token
                // User was logged out, close screen and open login
                //finish()
                //startActivity(Intent(this@ResidencesActivity, LoginActivity::class.java))
            }
        }
    }

    private fun observeResidences() {
        residencesViewModel.residences.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val residences = it.data?.second.orEmpty()
                    nextPage = it.data?.first

                    // Update UI with Residences
                    if (it.data?.first != null) {
                        // We are adding more items to the list
                        adapter?.submitList(adapter!!.currentList.plus(residences))
                    } else {
                        // It's the first n items, no pagination yet
                        adapter?.submitList(residences)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {

                }
            }
        })
    }


    private fun observeVerResidence() {
        residencesViewModel.verResidence.observe(viewLifecycleOwner, { verResidence ->
            if (verResidence) {
                val intent = Intent(activity, ResidenceActivity::class.java)
                intent.putExtra("residence", residencesViewModel.residence.value)
                startActivity(intent)
            }
        })
    }
}