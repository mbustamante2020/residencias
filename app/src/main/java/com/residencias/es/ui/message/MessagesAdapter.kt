package com.residencias.es.ui.message

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.residencias.es.data.message.Message
import com.residencias.es.databinding.ItemMessagesBinding
import com.residencias.es.viewmodel.MessagesViewModel

class MessagesAdapter(private val viewModel: MessagesViewModel) :
        ListAdapter<Message, MessagesAdapter.MessagesViewHolder>(messagesDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMessagesBinding.inflate(inflater)
        return MessagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.bindTo(getItem(position), viewModel)
    }

    class MessagesViewHolder(private val binding: ItemMessagesBinding) : RecyclerView.ViewHolder(binding.itemMessages) {

        fun bindTo(message: Message, viewModel: MessagesViewModel) {
            with(binding) {
                //evento que permite acceder 
                itemMessages.setOnClickListener {
                   viewModel.messageClicked(message)
                }
                username.text = message.name
                email.text = message.email.plus('\n').plus(message.phone)
            }
        }
    }

    companion object {
        private val messagesDiffCallback = object : DiffUtil.ItemCallback<Message>() {

            override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
                return oldItem == newItem
            }
        }
    }
}