package com.blankymunn3.localkeywordsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.databinding.RecyclerItemSearchBinding
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.util.DiffCallback

class SearchRVAdapter(var list: List<Place> = emptyList()): RecyclerView.Adapter<SearchRVAdapter.ViewHolder>() {

    private lateinit var onClickItemListener: OnClickItemListener

    interface OnClickItemListener {
        fun onClickItem(position: Int)
    }

    fun setOnClickItem(onClickItemListener: OnClickItemListener) {
        this.onClickItemListener = onClickItemListener
    }

    fun setData(newData: List<Place>) {
        val diffResult = DiffUtil.calculateDiff(DiffCallback(this.list, newData))
        this.list = newData
        diffResult.dispatchUpdatesTo(this@SearchRVAdapter)
    }

    inner class ViewHolder(private val binding: RecyclerItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(place: Place) {
            binding.tvPlaceName.text = place.placeName
            binding.tvAddressName.text = place.addressName
            binding.tvDistance.text = "${place.distance}m"
            binding.layoutSearchItem.setOnClickListener {
                onClickItemListener.onClickItem(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRVAdapter.ViewHolder =
        ViewHolder(RecyclerItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: SearchRVAdapter.ViewHolder, position: Int) = holder.bind(list[position])

    override fun getItemCount(): Int = list.size
}