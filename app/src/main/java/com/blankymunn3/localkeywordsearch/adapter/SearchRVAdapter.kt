package com.blankymunn3.localkeywordsearch.adapter

import android.annotation.SuppressLint
import android.net.wifi.rtt.CivicLocationKeys.STATE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.databinding.RecyclerItemFooterBinding
import com.blankymunn3.localkeywordsearch.databinding.RecyclerItemSearchBinding
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.model.State
import com.blankymunn3.localkeywordsearch.util.DiffCallback
import com.blankymunn3.localkeywordsearch.util.OnClickItemListener

class SearchRVAdapter(private val retry: () -> Unit): PagedListAdapter<Place, RecyclerView.ViewHolder>(DiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = State.LOADING

    private lateinit var onClickItemListener: OnClickItemListener

    fun setOnClickItem(onClickItemListener: OnClickItemListener) {
        this.onClickItemListener = onClickItemListener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) (holder as ViewHolder).bind(getItem(position)!!, onClickItemListener)
        else (holder as FooterViewHolder).bind(state)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == DATA_VIEW_TYPE) ViewHolder.create(parent)
        else  FooterViewHolder.create(retry, parent)

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasFooter()) 1 else 0

    private fun hasFooter(): Boolean = super.getItemCount() != 0 && (state == State.LOADING)

    fun setState(state: State) {
        val prevState = this.state
        val hadExtraRow = hasExtraRow()
        this.state = state
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) notifyItemRemoved(super.getItemCount())
            else notifyItemInserted(super.getItemCount())
        } else if (hasExtraRow && prevState != state) notifyItemChanged(itemCount)
    }

    private fun hasExtraRow() = state != State.DONE

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<Place>() {
            override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean = oldItem == newItem
        }
    }

}