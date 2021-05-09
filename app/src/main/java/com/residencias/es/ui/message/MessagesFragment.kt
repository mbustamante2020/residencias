package com.residencias.es.ui.message

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
import com.residencias.es.data.message.Message
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.FragmentMessagesBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.MessagesViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class MessagesFragment : Fragment(R.layout.fragment_messages) {

    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private val messagesViewModel: MessagesViewModel by viewModel()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)

        _binding?.recyclerViewMessages?.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            val layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(activity)
            // set the custom adapter to the RecyclerView
            val adapter: MessagesAdapter? = MessagesAdapter(messagesViewModel)
            Log.i("mensajes", "53 adapter count ${adapter?.itemCount}")


            binding.recyclerViewMessages.layoutManager = layoutManager
            // Set Adapter
            Log.i("mensajes", "81 adapter count ${adapter?.itemCount}")
            binding.recyclerViewMessages.adapter = adapter



            // Swipe to Refresh Listener
            binding.swipeRefreshLayout.setOnRefreshListener {
                getMessages()
            }
            // Get Residences
            getMessages()

            observeMessages(adapter)
            observeClickMessage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private fun getMessages() {
        // Show Loading
        binding.swipeRefreshLayout.isRefreshing = true

        // Get Residences
        lifecycleScope.launch {
            try {
                var message = Message(0,null,null,2,4)
                messagesViewModel.getMessages(message)

                // Hide Loading
                binding.swipeRefreshLayout.isRefreshing = false

            } catch (t: UnauthorizedException) {
                // Clear local access token
                messagesViewModel.onUnauthorized()
                // User was logged out, close screen and open login
                //finish()
                //startActivity(Intent(this@ResidencesActivity, LoginActivity::class.java))
            }
        }
    }

    private fun observeMessages(adapter: MessagesAdapter?) {
        messagesViewModel.messages.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                       val messages = it.data
/*
                       Log.i("mensajes", "${messages?.size}")
                       Log.i("mensajes", "$messages")

                       if (it.data != null) {
                           // We are adding more items to the list
                          // adapter?.submitList(adapter!!.currentList.plus(messages))
                       } else {
                           // It's the first n items, no pagination yet
                           adapter?.submitList(messages)
                       }*/

                   // val messages:List<Message> = listOf(Message(1,"mmn", "202020", 2,4, "mm"), Message(2,"mmn", "202020", 2,4, "mm"))

                    Log.i("mensajes", "$messages")
                    Log.i("mensajes", " adapter count ${adapter?.itemCount}")
                    adapter?.submitList(messages)

                    Log.i("mensajes", " adapter count ${adapter?.itemCount}")

                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Log.i("mensajes", "error")
                }
            }
        })
    }

    private fun observeClickMessage() {
        messagesViewModel.viewMessage.observe(viewLifecycleOwner, { message ->
            if (message) {
                val intent = Intent(activity, MessageActivity::class.java)
                intent.putExtra("message", messagesViewModel.message.value)

                //val bundle = Bundle()
                //bundle.putParcelable("message", messagesViewModel.message.value)
                //intent.putExtra("Bundle", bundle)
               Log.i("mensaje 143",  "${messagesViewModel.message.value?.id}")


                startActivity(intent)
            }
        })
    }
}