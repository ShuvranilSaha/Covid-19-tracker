package com.subranil.covid19traker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.MergeAdapter
import com.google.android.material.snackbar.Snackbar
import com.subranil.covid19traker.adapter.ItemAdapter
import com.subranil.covid19traker.adapter.TotalAdapter
import com.subranil.covid19traker.databinding.ActivityMainBinding
import com.subranil.covid19traker.models.Details
import com.subranil.covid19traker.utils.State
import com.subranil.covid19traker.viewmodel.MainViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.viewmodel.ext.android.viewModel

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    private val mTotalAdapter = TotalAdapter()

    private val mStateAdapter = ItemAdapter(this::navigateToStateDetailsActivity)
    private val adapter = MergeAdapter(mTotalAdapter, mStateAdapter)

    private var backPressedTime = 0L
    private val backSnackbar by lazy {
        Snackbar.make(binding.root, BACK_PRESSED_MESSAGE, Snackbar.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        initData()
    }

    private fun initViews() {
        setSupportActionBar(binding.appBarlayout.toolbar)

        binding.recyclerView.adapter = adapter

        binding.swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backSnackbar.dismiss()
            super.onBackPressed()
            return
        } else {
            backSnackbar.view.setBackgroundColor(Color.parseColor("#000000"))
            backSnackbar.show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun initData() {
        viewModel.liveData.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> binding.swipeRefreshLayout.isRefreshing = true
                is State.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(applicationContext, state.message, Toast.LENGTH_LONG).show()
                }
                is State.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false

                    val list = state.data.stateWiseDetails
                    mTotalAdapter.submitList(list.subList(0, 1))
                    mStateAdapter.submitList(list.subList(1, list.size - 1))
                }
            }
        })

        if (viewModel.liveData.value !is State.Success) {
            loadData()
        }
    }

    private fun loadData() {
        viewModel.getData()
    }

    private fun navigateToStateDetailsActivity(details: Details) {
        startActivity(Intent(this, StateDetailsActivity::class.java).apply {
            putExtra(StateDetailsActivity.KEY_STATE_DETAILS, details)
        })
    }

    companion object {
//        const val JOB_TAG = "notificationWorkTag"
        const val BACK_PRESSED_MESSAGE = "Press back again to exit"
    }
}