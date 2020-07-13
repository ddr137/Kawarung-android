package com.nahltech.kawarung.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.HomeProductAdapter
import com.nahltech.kawarung.adapters.MySliderAdapter
import com.nahltech.kawarung.data.models.Banner
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.ui.search.SearchActivity
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    var viewPager: ViewPager2? = null
    var mySliderLists: List<Banner>? = null
    var adapter: MySliderAdapter? = null
    private var indicatorLay: LinearLayout? = null
    private var api = ApiClient.instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        setupViewModel()
        setOnClick()
        name_of_user.text = context?.let { Constants.getNameUser(it) }
    }

    private fun setOnClick() {
        more_trend.setOnClickListener {
            val moveIntent = Intent(context, ListProductActivity::class.java).apply {
                putExtra("product_category_name", "best-seller")
                putExtra("product_category_title", "Produk Terlaris")
            }
            startActivity(moveIntent)
        }
        more_new.setOnClickListener {
            val moveIntent = Intent(context, ListProductActivity::class.java).apply {
                putExtra("product_category_name", "new")
                putExtra("product_category_title", "Produk Terbaru")
            }
            startActivity(moveIntent)
        }
        search_product_home.setOnClickListener {
            val moveIntent = Intent(context, SearchActivity::class.java)
            startActivity(moveIntent)
        }
    }

    private fun setupViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getProductBestSelling().observe(viewLifecycleOwner, Observer {
            rv_trend_categories.adapter?.let { adapter ->
                if (adapter is HomeProductAdapter) {
                    adapter.setListHomeProduct(it)
                }
            }
        })

        homeViewModel.getProductNew().observe(viewLifecycleOwner, Observer {
            rv_new_categories.adapter?.let { adapter ->
                if (adapter is HomeProductAdapter) {
                    adapter.setListHomeProduct(it)
                }
            }
        })

        homeViewModel.getState().observe(viewLifecycleOwner, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fetchProductTrend("best-seller".toString())
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
            rv_trend_categories.visibility = View.GONE
            sh_product_trend.visibility = View.VISIBLE
            sh_product_trend.startShimmerAnimation()

            rv_new_categories.visibility = View.GONE
            sh_product_new.visibility = View.VISIBLE
            sh_product_new.startShimmerAnimation()
        } else {
            rv_trend_categories.visibility = View.VISIBLE
            sh_product_trend.visibility = View.GONE
            sh_product_trend.stopShimmerAnimation()

            rv_new_categories.visibility = View.VISIBLE
            sh_product_new.visibility = View.GONE
            sh_product_new.stopShimmerAnimation()
        }
    }

    private fun toast(message: String?) = Toast.makeText(context, message, Toast.LENGTH_LONG).show()

    private fun setupRecycler() {
        rv_trend_categories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeProductAdapter(mutableListOf(), context)
        }
        rv_new_categories.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeProductAdapter(mutableListOf(), context)
        }
    }

    private fun getData() {
        api.getBannerSlider().enqueue(object : Callback<List<Banner>> {
            override fun onResponse(call: Call<List<Banner>>, response: Response<List<Banner>>) {
                mySliderLists = response.body()
                adapter = MySliderAdapter(requireContext(), mySliderLists as List<Banner>)
                viewPager!!.adapter = adapter
                setupIndicator()
                setupCurrentIndicator(0)
            }

            override fun onFailure(call: Call<List<Banner>>, t: Throwable) {}
        })
    }

    private fun setupIndicator() {
        val indicator = arrayOfNulls<ImageView>(adapter!!.itemCount)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(4, 0, 4, 0)
        for (i in indicator.indices) {
            indicator[i] = ImageView(requireContext())
            indicator[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.indicator_inactive
                )
            )
            indicator[i]!!.layoutParams = layoutParams
            indicatorLay!!.addView(indicator[i])
        }
    }

    private fun setupCurrentIndicator(index: Int) {
        val itemCount = indicatorLay!!.childCount
        for (i in 0 until itemCount) {
            val imageView = indicatorLay!!.getChildAt(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.indicator_active
                    )
                })
            } else {
                imageView.setImageDrawable(activity?.let {
                    ContextCompat.getDrawable(
                        it,
                        R.drawable.indicator_inactive
                    )
                })
            }
        }
    }

    companion object {
        private var currentPage = 0
        private const val NUM_PAGES = 3

        fun newInstance(): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}