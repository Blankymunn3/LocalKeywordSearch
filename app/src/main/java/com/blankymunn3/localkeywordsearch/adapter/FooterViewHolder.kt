package com.blankymunn3.localkeywordsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.databinding.RecyclerItemFooterBinding
import com.blankymunn3.localkeywordsearch.model.State

class FooterViewHolder(private val binding: RecyclerItemFooterBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(status: State?) {
        binding.progressBar.visibility = if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
            return FooterViewHolder(RecyclerItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
}