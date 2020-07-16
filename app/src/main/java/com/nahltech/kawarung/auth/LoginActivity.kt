package com.nahltech.kawarung.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.nahltech.kawarung.MainActivity
import com.nahltech.kawarung.R
import com.nahltech.kawarung.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authViewModel: AuthViewModel
    private var callbackManager: CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        setContentView(R.layout.activity_login)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // [END initialize_auth]

        // Firebase sign out
        auth.signOut()

        // Google revoke access
        googleSignInClient.revokeAccess().addOnCompleteListener(this) {
            googleLoginResult(null)
        }

        google_sign.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }


        callbackManager = CallbackManager.Factory.create()
        hashKey()
        loginFacebook()
        clickIntent()
        toolbarUI()
        setupViewModel()
        doLogin()
    }

    private fun googleLoginResult(user: FirebaseUser?) {
        if (user != null) {
            authViewModel.fetchFacebook("google", user.uid, user.displayName.toString(),
                user.email.toString()
            )
            Log.d("SocmedLogin", ""+user.uid+" "+user.displayName.toString()+" "+
                    user.email.toString())
        } else {
            Log.d("SocmedLogin", ""+user)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("SocmedLogin", "Google sign in failed", e)
                // ...
            }
        } else {
            callbackManager!!.onActivityResult(requestCode, resultCode, data)
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    Log.d("SocmedLogin", "signInWithCredential:success")
                    googleLoginResult(user)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SocmedLogin", "signInWithCredential:failure", task.exception)
                }
            }
    }
    // [END auth_with_google]

    private fun loginFacebook() {
        imgbtn_fb_login.setOnClickListener {
            btn_fb_login.performClick()
        }
        LoginManager.getInstance().logOut()
        btn_fb_login.setReadPermissions("email", "public_profile")
        btn_fb_login.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val data_request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { json_object, response ->
                    try {
                        val jsonObject = JSONObject(json_object.toString())
                        val s_id = jsonObject["id"].toString()
                        val s_name = jsonObject["name"].toString()
                        val s_email = jsonObject["email"].toString()
                        //final String s_phone = jsonObject.get("phone").toString();
                        val inputMethodManager =
                            this@LoginActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            this@LoginActivity.window.decorView.rootView
                                .windowToken, 0
                        )

                        Log.d("SocmedLogin", ""+s_id+","+s_name)
                        authViewModel.fetchFacebook("facebook", s_id, s_name, s_email)
                    } catch (e: Exception) {
                        Log.e("SocmedLogin", e.toString())
                        e.printStackTrace()
                    }
                }
                val permission_param = Bundle()
                permission_param.putString("fields", "id,email,name")
                data_request.parameters = permission_param
                data_request.executeAsync()
            }

            override fun onCancel() {
                Log.d("SocmedLogin", "Cancel")
            }

            override fun onError(exception: FacebookException) {
                Log.d("SocmedLogin", "Facebook $exception")
            }
        })
    }

    @SuppressLint("PackageManagerGetSignatures")
    private fun hashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.nahltech.kawarung", PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md =
                    MessageDigest.getInstance("SHA1")
                md.update(signature.toByteArray())
                val sign = Base64
                    .encodeToString(md.digest(), Base64.DEFAULT)
                Log.d("MYKEYHASH:", sign)
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    private fun clickIntent() {
        register_now.setOnClickListener {
            val moveIntent = Intent(this, RegisterActivity::class.java)
            startActivity(moveIntent)
        }
    }

    private fun toolbarUI() {
        setSupportActionBar(toolbar_login)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar_login.setNavigationIcon(R.drawable.ic_back_black)
        toolbar_login.setNavigationOnClickListener { finish() }
    }

    private fun setupViewModel() {
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        authViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
    }

    private fun doLogin() {
        btn_login.setOnClickListener {
            val inputMethodManager =
                this@LoginActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                this@LoginActivity.window.decorView.rootView.windowToken, 0
            )

            val emailPhone = edt_email_phone_login.text.toString().trim()
            val password = edt_password_login.text.toString().trim()
            if (authViewModel.validateLogin(emailPhone, password)) {
                authViewModel.login(emailPhone, password)
            }
        }
    }

    private fun handleUIState(it: UserState)  {
        when (it) {
            is UserState.Reset -> {
                setPasswordError(null)
            }
            is UserState.Error -> {
                isLoading(false)
                toast(it.err)
            }

            is UserState.ShowToast -> toast(it.message)
            is UserState.Failed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Validate -> {
                it.password?.let {
                    setPasswordError(it)
                }
            }
            is UserState.ErrorSocmed -> {
                isLoading(false)
                toast(it.err)
                if(it.status == "204"){
                    startActivity(Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                        putExtra("name", it.name)
                        putExtra("email", it.email)
                    })
                }
            }
            is UserState.Success -> {
                Constants.setToken(this@LoginActivity, it.token)
                Constants.setIdUser(this@LoginActivity, it.id)
                Constants.setNameUser(this@LoginActivity, it.name)
                toast("Selamat datang ${it.name}")
                startActivity(Intent(this@LoginActivity, MainActivity::class.java)).also {
                    finish()
                }
            }
            is UserState.IsLoading -> isLoading(it.state)
        }
    }

    private fun setPasswordError(err: String?) {
        layout_input_password_login.error = err
    }

    private fun isLoading(state: Boolean) {
        if (state) {
            edt_email_phone_login.isEnabled = false
            edt_password_login.isEnabled = false
            register_now.isEnabled = false
            btn_login.isEnabled = false

            btn_login.visibility = View.GONE
            sh_login.visibility = View.VISIBLE
            //loading.isIndeterminate = true
            sh_login.startShimmerAnimation()
        } else {
            sh_login.apply {
                //isIndeterminate = false
                btn_login.visibility = View.VISIBLE
                sh_login.visibility = View.GONE
                sh_login.stopShimmerAnimation()
                //progress = 0
            }
            edt_email_phone_login.isEnabled = true
            edt_password_login.isEnabled = true
            register_now.isEnabled = true
            btn_login.isEnabled = true
        }
    }

    private fun toast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}