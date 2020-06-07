package com.subranil.covid19traker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.subranil.covid19traker.adapter.DistrictAdapter
import com.subranil.covid19traker.adapter.TotalAdapter
import com.subranil.covid19traker.databinding.ActivityStateDetailsBinding
import com.subranil.covid19traker.models.Details
import com.subranil.covid19traker.utils.State
import com.subranil.covid19traker.utils.getPeriod
import com.subranil.covid19traker.utils.toDateFormat
import com.subranil.covid19traker.viewmodel.StateDetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class StateDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStateDetailsBinding

    private val mStateTotalAdapter = TotalAdapter()
    private val mDistrictAdapter = DistrictAdapter()

    private val adapter = MergeAdapter(mStateTotalAdapter, mDistrictAdapter)

    private val viewModel: StateDetailsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initData()
    }

    private fun initViews() {
        setSupportActionBar(binding.appBarlayout.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.recyclerState.adapter = adapter

        val details: Details? = getStateDetails()

        details?.let {
            mStateTotalAdapter.submitList(listOf(it))
            supportActionBar?.title = it.state
            supportActionBar?.subtitle = getString(
                R.string.text_last_updated,
                getPeriod(
                    it.lastUpdatedTime.toDateFormat()
                )
            )
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData(getStateDetails())
        }
    }

    private fun initData() {
        viewModel.stateLiveData.observe(this, Observer { it ->
            when (it) {
                is State.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is State.Success -> {
                    val list = it.data.districtData
                    list.sortedByDescending { it.confirmed }.let {
                        mDistrictAdapter.submitList(it)
                    }
                    binding.swipeRefreshLayout.isRefreshing = false
                }

                is State.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        if (viewModel.stateLiveData.value !is State.Success) {
            loadData(getStateDetails())
        }
    }
    private fun loadData(details: Details?) {
        details?.state?.let {
            viewModel.getDistrictData(it)
        }
    }

    private fun getStateDetails(): Details? = intent.getParcelableExtra(KEY_STATE_DETAILS)


    companion object {
        const val KEY_STATE_DETAILS = "key_state_details"
    }
}