package com.nahltech.kawarung.ui.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nahltech.kawarung.R
import com.nahltech.kawarung.adapters.CheckoutProductAdapter
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.DecimalFormat
import java.util.*

class CheckoutActivity : AppCompatActivity() {

    private lateinit var checkoutViewModel: CheckoutViewModel
    private var disposable: Disposable? = null
    private var api = ApiClient.instance()

    private var provinces = mutableListOf<String>()
    var provincesId = mutableListOf<Long>()

    private var cities = mutableListOf<String>()
    var citiesId = mutableListOf<Long>()

    private var districts = mutableListOf<String>()
    var districtsId = mutableListOf<Long>()

    private var villages = mutableListOf<String>()
    var villagesId = mutableListOf<Long>()

    var idProvince: String = ""
    var idCity: String = ""
    var idDistrict: String = ""
    var idVillage: String = ""

    var paymentMethod: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        toolbarUI()
        getProvince()
        setSpinner()
        fillText()
        setupViewModel()
        setupRecycler()

        if (cod_method.isClickable) {
            paymentMethod = "Cash On Delivery"
        } else if (bank_method.isClickable) {
            paymentMethod = "Bank Transfer"
        }
    }

    private fun setupViewModel() {
        checkoutViewModel = ViewModelProvider(this).get(CheckoutViewModel::class.java)
        doUpdate()

        checkoutViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })

        checkoutViewModel.getProductCheckout().observe(this, Observer {
            rv_checkout.adapter?.let { adapter ->
                if (adapter is CheckoutProductAdapter) {
                    adapter.setListProductCheckout(it)
                }
            }
        })
        checkoutViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    override fun onResume() {
        super.onResume()
        checkoutViewModel.fetchProductCheckout(Constants.getIdUser(this), Constants.getToken(this))
    }

    private fun setupRecycler() {
        rv_checkout.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity)
            adapter = CheckoutProductAdapter(mutableListOf(), context)
        }
    }

    private fun fillText() {
        val localeID = Locale("in", "ID")
        val formatRupiah = DecimalFormat.getCurrencyInstance(localeID)

        qty_checkout.text = intent.getStringExtra("qty")
        price_piece.text = formatRupiah.format(intent.getStringExtra("total_discount").toString().toDouble())
        sub_total_checkout.text = formatRupiah.format(intent.getStringExtra("sub_total").toString().toDouble())

    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_checkout)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_checkout.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_checkout.setNavigationOnClickListener { finish() }
    }

    private fun setSpinner() {
        val adapterProvinsi = ArrayAdapter(
            this,
            R.layout.spinner_single_simple, resources.getStringArray(R.array.provinsi)
        )
        adapterProvinsi.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        spinner_province_address_checkout.adapter = adapterProvinsi

        val adapterKota = ArrayAdapter(
            this,
            R.layout.spinner_single_simple, resources.getStringArray(R.array.kota)
        )
        adapterKota.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        spinner_city_address_checkout.adapter = adapterKota
        spinner_city_address_checkout.isEnabled = false

        val adapterKecamatan = ArrayAdapter(
            this,
            R.layout.spinner_single_simple, resources.getStringArray(R.array.kecamatan)
        )
        adapterKecamatan.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        spinner_district_address_checkout.adapter = adapterKecamatan
        spinner_district_address_checkout.isEnabled = false

        val adapterDesa = ArrayAdapter(
            this,
            R.layout.spinner_single_simple, resources.getStringArray(R.array.desa)
        )
        adapterDesa.setDropDownViewResource(R.layout.spinner_dropdown_simple)
        spinner_village_address_checkout.adapter = adapterDesa
        spinner_village_address_checkout.isEnabled = false
    }

    private fun getProvince() {
        provinceLoad(true)
        disposable = api.provinces()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    provinces.clear()
                    provincesId.clear()

                    provinces.add("Pilih Provinsi")
                    provincesId.add(0)

                    result.data!!.forEach {
                        provinces.add(it.provinceName)
                        provincesId.add(it.id)
                    }

                    val adapterProvinsi = ArrayAdapter(
                        this,
                        R.layout.spinner_single_simple, provinces
                    )
                    adapterProvinsi.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    spinner_province_address_checkout.adapter = adapterProvinsi

                    provinceLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    provinceLoad(false)
                }
            )

        spinner_province_address_checkout.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (provincesId.count() > 0 && position > 0) {
                        idProvince = provinces[position]
                        getCity(provincesId[position]).toString()
                    }
                }
            }


    }

    private fun provinceLoad(status: Boolean) {
        spinner_province_address_checkout.isEnabled = !status
        spinner_city_address_checkout.isEnabled = false
        spinner_district_address_checkout.isEnabled = false
        spinner_village_address_checkout.isEnabled = false
    }

    private fun getCity(id: Long) {
        cityLoad(true)
        disposable = api.cities(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    cities.clear()
                    citiesId.clear()

                    cities.add("Pilih Kota/Kab")
                    citiesId.add(0)

                    result.data!!.forEach {
                        cities.add(it.cityName)
                        citiesId.add(it.id)
                    }

                    val adapterCity = ArrayAdapter(
                        this,
                        R.layout.spinner_single_simple, cities
                    )
                    adapterCity.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    spinner_city_address_checkout.adapter = adapterCity

                    cityLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    cityLoad(false)
                }
            )

        spinner_city_address_checkout.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (citiesId.count() > 0 && position > 0) {
                    idCity = cities[position]
                    getDistrict(citiesId[position]).toString()
                }
            }
        }
    }

    private fun cityLoad(status: Boolean) {
        spinner_province_address_checkout.isEnabled = !status
        spinner_city_address_checkout.isEnabled = !status
        spinner_district_address_checkout.isEnabled = false
        spinner_village_address_checkout.isEnabled = false
    }

    private fun getDistrict(id: Long) {
        districtLoad(true)
        disposable = api.districts(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->

                    districts.clear()
                    districtsId.clear()

                    districts.add("Pilih Kecamatan")
                    districtsId.add(0)

                    result.data!!.forEach {
                        districts.add(it.districtName)
                        districtsId.add(it.id)
                    }

                    val adapterDistrict = ArrayAdapter(
                        this,
                        R.layout.spinner_single_simple, districts
                    )
                    adapterDistrict.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    spinner_district_address_checkout.adapter = adapterDistrict

                    districtLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    districtLoad(false)
                }
            )



        spinner_district_address_checkout.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (districtsId.count() > 0 && position > 0) {
                        idDistrict = districts[position]
                        getVillage(districtsId[position]).toString()
                    }
                }
            }
    }

    private fun districtLoad(status: Boolean) {
        spinner_province_address_checkout.isEnabled = !status
        spinner_city_address_checkout.isEnabled = !status
        spinner_district_address_checkout.isEnabled = !status
        spinner_village_address_checkout.isEnabled = false
    }

    private fun getVillage(id: Long) {
        villageLoad(true)
        disposable = api.villages(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    villages.clear()
                    villagesId.clear()

                    villages.add("Pilih Kelurahan/Desa")
                    villagesId.add(0)

                    result.data!!.forEach {
                        villages.add(it.villageName)
                        villagesId.add(it.id)
                    }

                    val adapterVillage = ArrayAdapter(
                        this,
                        R.layout.spinner_single_simple, villages
                    )
                    adapterVillage.setDropDownViewResource(R.layout.spinner_dropdown_simple)
                    spinner_village_address_checkout.adapter = adapterVillage

                    villageLoad(false)

                },
                { error ->
                    Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    villageLoad(false)
                }
            )

        spinner_village_address_checkout.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (villagesId.count() > 0 && position > 0) {
                        idVillage = villages[position]
                        villagesId[position].toString()
                    }
                }
            }
    }

    private fun villageLoad(status: Boolean) {
        spinner_province_address_checkout.isEnabled = !status
        spinner_city_address_checkout.isEnabled = !status
        spinner_district_address_checkout.isEnabled = !status
        spinner_village_address_checkout.isEnabled = !status
    }

    private fun handleUIState(it: CheckoutState) {
        when (it) {
            is CheckoutState.IsLoading -> isLoading(it.state)
            is CheckoutState.Error -> {
                toast(it.err)
                isLoading(false)
            }
            is CheckoutState.ShowToast -> toast(it.message)
            is CheckoutState.IsSuccess -> {
                toast("Berhasil dibuat")
                startActivity(Intent(this@CheckoutActivity, CheckoutSuccessActivity::class.java))
            }
        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun isLoading(state: Boolean) {
        if (state) {
            next_checkout.isEnabled = false
            next_checkout.visibility = View.GONE
            sh_button_checkout.visibility = View.VISIBLE
            sh_button_checkout.startShimmerAnimation()
        } else {
            next_checkout.isEnabled = true
            next_checkout.visibility = View.VISIBLE
            sh_button_checkout.visibility = View.GONE
            sh_button_checkout.stopShimmerAnimation()
        }
    }

    private fun doUpdate() {
        next_checkout.setOnClickListener {
            //val choosePayment: RadioButton = findViewById(rg_payment_method.checkedRadioButtonId)

            val id = Constants.getIdUser(this).trim()
            val token = Constants.getToken(this).trim()
            val provinceId = idProvince.trim()
            val cityId = idCity.trim()
            val districtId = idDistrict.trim()
            val villageId = idVillage.trim()
            val completeAddress = complete_address_address_checkout.text.toString().trim()
            val postalCode = postal_code_address_checkout.text.toString().trim()
            val orderId = intent.getStringExtra("order_id").toString().trim()
            val note = note_checkout.text.toString().trim()

            val paymentMethod = paymentMethod.trim()
            val totalDiscount = intent.getStringExtra("total_discount").toString().trim()
            val subtotal = intent.getStringExtra("sub_total").toString().trim()

            checkoutViewModel.checkoutPurchase(
                id,
                token,
                provinceId,
                cityId,
                districtId,
                villageId,
                completeAddress,
                postalCode,
                orderId,
                note,
                paymentMethod,
                totalDiscount,
                subtotal
            )
        }
    }
}