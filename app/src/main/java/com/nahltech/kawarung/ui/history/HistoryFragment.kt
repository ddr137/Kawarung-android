package com.nahltech.kawarung.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.HistoryPurchaseAdapter
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
        //toolbarUI()
        //setupRecycler()
        //setupViewModel()
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

        historyViewModel.getHistoryPurchase().observe(viewLifecycleOwner, Observer {
            //fill(it)
        })

        historyViewModel.getState().observe(viewLifecycleOwner, Observer {
            //handleUIState(it)
        })
    }

}