package com.nahltech.kawarung.ui.confirmPayment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.nahltech.kawarung.MainActivity
import com.nahltech.kawarung.R
import com.nahltech.kawarung.data.models.MessageOnly
import com.nahltech.kawarung.data.network.ApiClient
import com.nahltech.kawarung.utils.Constants
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_confirm_payment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ConfirmPaymentActivity : AppCompatActivity() {

    private lateinit var imgFile: File
    private var api = ApiClient.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_payment)
        toolbarUI()
        btn_send_confirm_payment.setOnClickListener {
            val bank  = bank_name.text.toString().trim()
            val noRek = no_account_bank.text.toString().trim()
            val nameRek = name_owner_bank.text.toString().trim()

            if (bank.isEmpty() || noRek.isEmpty() || nameRek.isEmpty() ) {
                Toast.makeText(applicationContext, "Mohon isi semua form", Toast.LENGTH_SHORT)
                    .show()
            } else {
                dataDikirimSekarang()
            }
        }

        pick_proof.setOnClickListener {
            EasyImage.openChooserWithGallery(
                this@ConfirmPaymentActivity,
                "Pilih",
                1
            )
        }
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_confirm_payment)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_confirm_payment.setNavigationIcon(R.drawable.ic_back_white)
        toolbar_confirm_payment.setNavigationOnClickListener { finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {

            override fun onImagePicked(imageFile: File, source: EasyImage.ImageSource, type: Int) {
                CropImage.activity(Uri.fromFile(imageFile))
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setMinCropWindowSize(1000, 2000)
                    .setAllowRotation(false)
                    .setAllowFlipping(false)
                    .setCropMenuCropButtonTitle("klik disini")
                    .setFixAspectRatio(false)
                    .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setOutputCompressQuality(50)
                    .start(this@ConfirmPaymentActivity)
            }

            override fun onImagePickerError(e: Exception, source: EasyImage.ImageSource, type: Int) {
                super.onImagePickerError(e, source, type)
                Toast.makeText(this@ConfirmPaymentActivity, "" + e.message, Toast.LENGTH_SHORT).show()
            }

        })

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            val result = CropImage.getActivityResult(data)

            if (resultCode == Activity.RESULT_OK) {

                val uri: Uri = result.uri

                img_proof.load(File(uri.path))

                imgFile = File(uri.path)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val exception = result.error
                Toast.makeText(this, "" + exception.toString(), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun dataDikirimSekarang() {
        shConfirm.visibility = View.VISIBLE
        shConfirm.startShimmerAnimation()
        btn_send_confirm_payment.visibility = View.GONE

        val token = Constants.getToken(this).toString().trim()
        val userId = Constants.getIdUser(this).toString().trim()
        val orderId : RequestBody = RequestBody.create(MediaType.parse("text/plain"), intent.getStringExtra("id_purchase").toString().trim())
        val accountNumber : RequestBody = RequestBody.create(MediaType.parse("text/plain"), no_account_bank.text.toString().trim())
        val ownerName : RequestBody = RequestBody.create(MediaType.parse("text/plain"), name_owner_bank.text.toString().trim())
        val bankName : RequestBody = RequestBody.create(MediaType.parse("text/plain"), bank_name.text.toString().trim())
        val total : RequestBody = RequestBody.create(MediaType.parse("text/plain"), intent.getStringExtra("total").toString().trim())
        val requestFile = RequestBody.create(MediaType.parse("multipart/from-data"), imgFile)
        val image = MultipartBody.Part.createFormData("image", imgFile.name, requestFile)

        api.confirmPayment(token, userId, orderId, accountNumber, ownerName, bankName, total, image  )
            .enqueue(object : Callback<MessageOnly> {
                override fun onFailure(call: Call<MessageOnly>, t: Throwable) {
                    Toast.makeText(this@ConfirmPaymentActivity, t.message, Toast.LENGTH_SHORT).show()
                    shConfirm.visibility = View.GONE
                    btn_send_confirm_payment.visibility = View.VISIBLE
                    shConfirm.stopShimmerAnimation()
                }

                override fun onResponse(
                    call: Call<MessageOnly>,
                    response: Response<MessageOnly>
                ) {
                    if(response.isSuccessful){
                        if(response.code() == 201){
                            Toast.makeText(this@ConfirmPaymentActivity, "Terimakasih", Toast.LENGTH_SHORT).show()
                            shConfirm.visibility = View.GONE
                            shConfirm.stopShimmerAnimation()
                            btn_send_confirm_payment.visibility = View.VISIBLE
                            val moveIntent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(moveIntent)
                            finish()
                        } else if (response.code() == 422) {
                            val body = response.body() as MessageOnly
                            Toast.makeText(this@ConfirmPaymentActivity, "${body.message}", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                        Log.d("rekening", "rekening error")
                        Toast.makeText(this@ConfirmPaymentActivity, response.body().toString(), Toast.LENGTH_SHORT).show()
                        shConfirm.visibility = View.GONE
                        shConfirm.stopShimmerAnimation()
                        btn_send_confirm_payment.visibility = View.VISIBLE
                    }
                }

            })

    }
}