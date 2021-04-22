package com.blankymunn3.localkeywordsearch.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blankymunn3.localkeywordsearch.databinding.RecyclerItemSearchBinding
import com.blankymunn3.localkeywordsearch.model.Place
import com.blankymunn3.localkeywordsearch.util.OnClickItemListener

class ViewHolder(private val binding: RecyclerItemSearchBinding): RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(place: Place, onClickItemListener: OnClickItemListener) {
        binding.tvPlaceName.text = "${absoluteAdapterPosition + 1}. ${place.placeName}"
        binding.tvAddressName.text = place.addressName
        binding.tvDistance.text = "${place.distance}m"
        binding.ivArrUrl.visibility =
            if (place.placeUrl.isNotEmpty())  View.VISIBLE
            else View.INVISIBLE
        binding.layoutSearchItem.setOnClickListener {
            onClickItemListener.onClickItem(absoluteAdapterPosition)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ViewHolder {
            return ViewHolder(RecyclerItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }
}