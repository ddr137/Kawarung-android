package com.nahltech.kawarung.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.DetailProductAdapter
import kotlinx.android.synthetic.main.activity_new_product.*

class NewProductActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_product)
        toolbarUI()
        setupRecycler()
        setupViewModel()
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_new_product)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_new_product.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_new_product.setNavigationOnClickListener { finish() }
    }
    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getProductNew().observe(this, Observer {
            rv_new_categories_more.adapter?.let { adapter ->
                if (adapter is DetailProductAdapter) {
                    adapter.setListProductDetail(it)
                }
            }
        })

        homeViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }
    override fun onResume() {
        super.onResume()
        homeViewModel.fetchProductNew()
    }
    private fun handleUIState(it: HomeState) {
        when (it) {
            is HomeState.IsLoading -> isLoading(it.state)
            is HomeState.Error -> {
                toast(it.err)
                //donation_not_found.visibility = View.VISIBLE
                isLoading(false)
            }
            is HomeState.IsSuccess -> {
                toast("ayaan brow")
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
        rv_new_categories_more.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = DetailProductAdapter(mutableListOf(), context)
        }
    }
}