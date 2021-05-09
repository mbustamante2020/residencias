package com.residencias.es.ui.message

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.residencias.es.R
import com.residencias.es.data.message.Message
import com.residencias.es.databinding.ItemMessageBinding
import com.residencias.es.viewmodel.MessageViewModel
import io.ktor.client.engine.*


class MessageAdapter(private val viewModel: MessageViewModel) :
        ListAdapter<Message, MessageAdapter.MessageViewHolder>(messageDiffCallback) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMessageBinding.inflate(inflater)
            return MessageViewHolder(binding)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            holder.bindTo(getItem(position), viewModel)
        }

        class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.itemMessage) {

            fun bindTo(message: Message, viewModel: MessageViewModel) {


                with(binding) {


                    var params1: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, // This will define text view width
                            LinearLayout.LayoutParams.WRAP_CONTENT // This will define text view height
                    )

                    // Add margin to the text view
                    params1.setMargins(10, 10, 10, 10)

                    // Now, specify the text view width and height (dimension)




                    itemMessage.layoutParams = params1







                    //itemMessage.explicitStyle = R.style.EditText


                    if(message.id == message.idUserEmitter) {
                        //itemMessage.setBackgroundColor(Color.parseColor(R.color.black.toString()))
                        itemMessage.setBackgroundColor(Color.YELLOW)
                    } else {
                        //itemMessage.setBackgroundColor(Color.parseColor("#000000"))//actually you should set to the normal text color
                        itemMessage.setBackgroundColor(Color.parseColor("#f6f7f2"))
                    }




                    textMessage.text = message.message.plus("${message.id} ${message.idUserEmitter} ${message.idUserReceiver} ")
                    date.text = message.date




                }
            }
        }

        companion object {
            private val messageDiffCallback = object : DiffUtil.ItemCallback<Message>() {

                override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                    return oldItem.name == newItem.name
                }

                override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
