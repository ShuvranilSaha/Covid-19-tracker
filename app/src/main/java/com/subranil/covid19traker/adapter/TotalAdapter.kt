package com.subranil.covid19traker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.subranil.covid19traker.databinding.ItemTotalBinding
import com.subranil.covid19traker.models.Details

class TotalAdapter : ListAdapter<Details, TotalAdapter.TotalViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Details>() {
            override fun areItemsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem.state == newItem.state
            }

            override fun areContentsTheSame(oldItem: Details, newItem: Details): Boolean {
                return oldItem.state == newItem.state
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TotalViewHolder(
        ItemTotalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    class TotalViewHolder(private val binding: ItemTotalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(details: Details) {
            binding.totalConfirmedCases.text = details.confirmed
            binding.totalActiveCases.text = details.active
            binding.totalRecovered.text = details.recovered
            binding.totalDeath.text = details.deaths

            // new confirmed cases
            details.deltaConfirmed.let {
                if (it == "0") {
                    binding.groupNewConfirmed.visibility = View.GONE
                } else {
                    binding.groupNewConfirmed.visibility = View.VISIBLE
                    binding.newConfirmedCases.text = details.deltaConfirmed
                }
            }

            // new Recovered cases
            details.deltaRecovered.let {
                if (it == "0") {
                    binding.groupNewRecovered.visibility = View.GONE
                } else {
                    binding.groupNewRecovered.visibility = View.VISIBLE
                    binding.newRecovered.text = details.deltaRecovered
                }
            }

            // new Deaths
            details.deltaDeaths.let {
                if (it == "0") {
                    binding.groupNewDeaths.visibility = View.GONE
                } else {
                    binding.groupNewDeaths.visibility = View.VISIBLE
                    binding.totalNewDeaths.text = details.deltaDeaths
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TotalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}