package com.nahltech.kawarung.ui.history

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.HistoryStatusAdapter
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_history_status.*

class HistoryStatusActivity : AppCompatActivity() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_status)
        toolbarUI()
        setupViewModel()
        setupRecycler()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_history_status)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_history_status.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_history_status.setNavigationOnClickListener { finish() }
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.getHistoryStatus().observe(this, Observer {
            rv_history_status.adapter?.let { adapter ->
                if (adapter is HistoryStatusAdapter) {
                    adapter.setListHistoryStatus(it)
                }
            }
        })

        historyViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.fetchHistoryStatus(
            Constants.getIdUser(this),
            intent.getStringExtra("id_purchase").toString(),
            Constants.getToken(this)
        )
    }

    private fun handleUIState(it: HistoryState) {
        when (it) {
            is HistoryState.IsLoading -> isLoading(it.state)
            is HistoryState.Error -> {
                toast(it.err)
                isLoading(false)
            }
            is HistoryState.IsSuccess -> {
                //toast(it.what.toString())
            }
        }
    }

    private fun isLoading(state: Boolean) {
        if (state) {

        } else {

        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_history_status.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                HistoryStatusAdapter(
                    mutableListOf(),
                    context
                )
        }
    }
}