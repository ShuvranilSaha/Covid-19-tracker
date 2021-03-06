package com.subranil.covid19traker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.subranil.covid19traker.R
import com.subranil.covid19traker.databinding.ItemStateBinding
import com.subranil.covid19traker.models.Details
import com.subranil.covid19traker.utils.getPeriod
import com.subranil.covid19traker.utils.toDateFormat

class ItemAdapter(val clickListener: (stateDetails: Details) -> Unit = {}) :
    ListAdapter<Details, ItemAdapter.StateViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StateViewHolder(
        ItemStateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class StateViewHolder(private val binding: ItemStateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(details: Details) {
            binding.textState.text = details.state
            binding.textLastUpdatedView.text = itemView.context.getString(
                R.string.text_last_updated,
                getPeriod(
                    details.lastUpdatedTime.toDateFormat()
                )
            )

            binding.textConfirmed.text = details.confirmed
            binding.textActive.text = details.active
            binding.textRecovered.text = details.recovered
            binding.textDeath.text = details.deaths

            // New Confirmed
            details.deltaConfirmed.let {
                if (it == "0") {
                    binding.groupStateNewConfirm.visibility = View.GONE
                } else {
                    binding.groupStateNewConfirm.visibility = View.VISIBLE
                    binding.textStateNewConfirm.text = it
                }
            }

            // New Recovered
            details.deltaRecovered.let {
                if (it == "0") {
                    binding.groupStateNewRecover.visibility = View.GONE
                } else {
                    binding.groupStateNewRecover.visibility = View.VISIBLE
                    binding.textStateNewRecover.text = it
                }
            }

            // New Deaths
            details.deltaDeaths.let {
                if (it == "0") {
                    binding.groupStateNewDeaths.visibility = View.GONE
                } else {
                    binding.groupStateNewDeaths.visibility = View.VISIBLE
                    binding.textStateNewDeath.text = it
                }
            }

            // Set Click Listener
            binding.root.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) {
                    return@setOnClickListener
                }

                val item = getItem(bindingAdapterPosition)
                item.let {
                    clickListener.invoke(it)
                }
            }
        }

    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Details>() {
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem.state == newItem.state
            }

            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem == newItem
            }
        }
    }
}