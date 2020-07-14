package com.nahltech.kawarung.ui.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.DetailProductAdapter
import kotlinx.android.synthetic.main.activity_list_product.*

class ListProductActivity : AppCompatActivity() {
    private lateinit var homeViewModel: HomeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)
        setupRecycler()
        setupViewModel()
        toolbarUI()
        toolbar_title_list_product.text = intent.getStringExtra("product_category_title")
    }
    private fun toolbarUI() {
        setSupportActionBar(toolbar_trend_product)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_trend_product.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_trend_product.setNavigationOnClickListener { finish() }
    }
    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getProductBestSelling().observe(this, Observer {
            rv_trend_categories_more.adapter?.let { adapter ->
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
        homeViewModel.fetchProductTrend(intent.getStringExtra("product_category_name")!!.toString())
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
            rv_trend_categories_more.visibility = View.GONE
            sh_product_list_trend.visibility = View.VISIBLE
            sh_product_list_trend.startShimmerAnimation()
        } else {
            rv_trend_categories_more.visibility = View.VISIBLE
            sh_product_list_trend.visibility = View.GONE
            sh_product_list_trend.stopShimmerAnimation()
        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_trend_categories_more.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = DetailProductAdapter(mutableListOf(), context)
        }
    }

}