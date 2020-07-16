package com.nahltech.kawarung.ui.search

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.DetailProductAdapter
import kotlinx.android.synthetic.main.activity_search_result.*

class SearchResultActivity : AppCompatActivity() {
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setupRecycler()
        setupViewModel()
        toolbarUI()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_search_result)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_search_result.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_search_result.setNavigationOnClickListener { finish() }
        toolbar_search_result.setTitle(intent.getStringExtra("term").toString())
    }

    private fun setupViewModel() {
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.getSearch().observe(this, Observer {
            rv_search_result.adapter?.let { adapter ->
                if (adapter is DetailProductAdapter) {
                    adapter.setListProductDetail(it)
                }
            }
        })

        searchViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        searchViewModel.fetchSearch(intent.getStringExtra("term").toString(), "")
    }

    private fun handleUIState(it: SearchState) {
        when (it) {
//            is SearchState.IsLoading -> isLoading(it.state)
            is SearchState.Error -> {
                toast(it.err)
                //donation_not_found.visibility = View.VISIBLE
//                isLoading(false)
            }
            is SearchState.Failed -> {
                toast(it.message)
            }
            is SearchState.Success -> {
                toast("ayaan brow")
            }

        }
    }

//    private fun isLoading(state: Boolean) {
//        if (state) {
//            rv_search_result.visibility = View.GONE
//        } else {
//            rv_search_result.visibility = View.VISIBLE
//        }
//    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_search_result.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = DetailProductAdapter(mutableListOf(), context)
        }
    }
}