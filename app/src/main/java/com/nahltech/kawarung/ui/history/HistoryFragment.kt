package com.nahltech.kawarung.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.history.HistoryPurchaseAdapter
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment() {

    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupViewModel()
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.fetchHistoryPurchase(Constants.getIdUser(requireContext()), Constants.getToken(requireContext()))
    }

    private fun setupViewModel() {
        historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
        historyViewModel.getHistoryPurchase().observe(viewLifecycleOwner, Observer {
            rv_history_purchase.adapter?.let { adapter ->
                if (adapter is HistoryPurchaseAdapter) {
                    adapter.setListHistoryPurchase(it)
                }
            }
        })

        historyViewModel.getState().observe(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
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
            rv_history_purchase.visibility = View.GONE
            sh_history_purchase.visibility = View.VISIBLE
            sh_history_purchase.startShimmerAnimation()
        } else {
            rv_history_purchase.visibility = View.VISIBLE
            sh_history_purchase.visibility = View.GONE
            sh_history_purchase.stopShimmerAnimation()
        }
    }

    private fun toast(message: String?) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_history_purchase.apply {
            layoutManager = LinearLayoutManager(context)
            adapter =
                HistoryPurchaseAdapter(
                    mutableListOf(),
                    context
                )
        }
    }



}