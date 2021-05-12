package com.residencias.es.ui.message

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.residencias.es.R
import com.residencias.es.data.message.Message
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.ActivityMessageBinding
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.MessageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel


class MessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMessageBinding
    private val messageViewModel: MessageViewModel by viewModel()
    private lateinit var message: Message

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        message = intent.getParcelableExtra("message")!!
        Log.i("Message", "${message?.id}")
        Log.i("Message", "${message?.idUserReceiver}")




        binding?.recyclerViewMessage?.apply {

            val layoutManager: RecyclerView.LayoutManager? = LinearLayoutManager(context)

            val adapter: MessageAdapter? = MessageAdapter(messageViewModel)

            binding.recyclerViewMessage.layoutManager = layoutManager
            binding.recyclerViewMessage.adapter = adapter

        /*    binding.swipeRefreshLayout.setOnRefreshListener {
                getMessages()
            }*/
            // Get Residences
            getMessages()

            observeMessages(adapter)

        }

        lifecycleScope.launch {
            delay(1000L)
            getMessages()
        }

        binding.btnSend.setOnClickListener {
            sendMessage()
        }
    }

    private fun sendMessage() {
        lifecycleScope.launch {
            try {
                if( binding.textMessage.text.isNullOrEmpty() ) {
                    Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
                } else {
                    message?.message = binding.textMessage.text.toString()
                    messageViewModel.sendMessageClicked(message)
                    getMessages()
                }
            } catch (t: UnauthorizedException) {
                Toast.makeText(applicationContext, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
        }
    }






    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////


    private fun getMessages() {
        // Show Loading
      //  binding.swipeRefreshLayout.isRefreshing = true

        // Get Residences
        lifecycleScope.launch {
            try {
                var message = Message(0, null, null, 2, 4)
                messageViewModel.getMyMessages(message)

                // Hide Loading
              //  binding.swipeRefreshLayout.isRefreshing = false

            } catch (t: UnauthorizedException) {
                // Clear local access token
                messageViewModel.onUnauthorized()
                // User was logged out, close screen and open login
                //finish()
                //startActivity(Intent(this@ResidencesActivity, LoginActivity::class.java))
            }
        }
    }

    private fun observeMessages(adapter: MessageAdapter?) {
        messageViewModel.messages.observe(this, {
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
}