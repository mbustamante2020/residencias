package com.residencias.es.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.ActivityLoginBinding
import com.residencias.es.ui.MainActivity
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel

import android.util.Log
import android.view.View
import android.widget.Button
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*




class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: ActivityLoginBinding

    //google
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int=123
    private lateinit var firebaseAuth: FirebaseAuth
    private var signInButton: SignInButton? = null

    //facebook
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /////////////////////////////////////////////////////////////////
        observeLogin()

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        //////////////////////////////////////////////////////////

        //google
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)

        firebaseAuth= FirebaseAuth.getInstance()


        signInButton = findViewById(R.id.signInButton);

        signInButton?.setOnClickListener{ view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }



        //facebook
        callbackManager = CallbackManager.Factory.create()


        val loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions(listOf("public_profile", "email"))
        // If you are using in a fragment, call loginButton.setFragment(this)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(loginResult: LoginResult?) {
                Log.d("TAG", "Success Login")
                // Get User's Info
            }

            override fun onCancel() {
                Toast.makeText(this@LoginActivity, "Login Cancelled", Toast.LENGTH_LONG).show()
            }

            override fun onError(exception: FacebookException) {
                Toast.makeText(this@LoginActivity, exception.message, Toast.LENGTH_LONG).show()
            }
        })












    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }

    private fun login() {
        lifecycleScope.launch {
            try {
                //loginViewModel.login("mbustama1@uoc.edu", "clave2021*")
                if( binding.email.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty() ) {
                    Toast.makeText(this@LoginActivity, getString(R.string.error_login_empty_fields), Toast.LENGTH_SHORT).show()
                } else {
                    loginViewModel.login("${binding.email.text}", "${binding.password.text}")
                }
            } catch (t: UnauthorizedException) {
                Toast.makeText(this@LoginActivity, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLogin() {
        loginViewModel.getToken.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }





















    private  fun signInGoogle(){

        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        } else {
            //facebook
            callbackManager.onActivityResult(requestCode, resultCode, data)
            super.onActivityResult(requestCode, resultCode, data)
        }
    }













    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task->
            if(task.isSuccessful) {
                // SavedPreference.setEmail(this,account.email.toString())
                // SavedPreference.setUsername(this,account.displayName.toString())
                //val intent = Intent(this, MainActivity::class.java)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            //startActivity(Intent(this, MainActivity::class.java))
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

}