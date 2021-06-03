package com.residencias.es.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
import com.residencias.es.R
import com.residencias.es.data.network.UnauthorizedException
import com.residencias.es.databinding.ActivityLoginBinding
import com.residencias.es.ui.MainActivity
import com.residencias.es.utils.Status
import com.residencias.es.viewmodel.LoginViewModel
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    private lateinit var binding: ActivityLoginBinding

    // Login con google
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private val reqCode: Int = 123
    private lateinit var firebaseAuth: FirebaseAuth
    private var signInButton: SignInButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ///////////////////////// Login con clave y correo //////////////////////////
        observeLogin()

        binding.btnLogin.setOnClickListener {
            login()
        }

        binding.btnRegister.setOnClickListener {
            register()
        }

        ///////////////////////// Login con google /////////////////////////////////
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso)

        firebaseAuth= FirebaseAuth.getInstance()

        signInButton = findViewById(R.id.signInButton)

        signInButton?.setOnClickListener{
            signInGoogle()
        }
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
        loginViewModel.getOAuthTokens.observe(this, {
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


    private fun loginGoogle(account: GoogleSignInAccount) {
        lifecycleScope.launch {
            try {
                loginViewModel.loginGoogle(account.email ?: "", account.displayName ?: "")
            } catch (t: UnauthorizedException) {
                Toast.makeText(this@LoginActivity, getString(R.string.error_login), Toast.LENGTH_SHORT).show()
            }
        }
    }


















    private  fun signInGoogle(){
        val signInIntent: Intent =mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, reqCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==reqCode){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
//            firebaseAuthWithGoogle(account!!)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                updateUI(account)
            }
        } catch (e: ApiException){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task->
            if(task.isSuccessful) {
                Log.i("google", account.email ?: "")
                Log.i("google", account.displayName ?: "")
                loginGoogle(account)
                // SavedPreference.setEmail(this,account.email.toString())
                // SavedPreference.setUsername(this,account.displayName.toString())
                //val intent = Intent(this, MainActivity::class.java)
                //val intent = Intent(this, MainActivity::class.java)
                //startActivity(intent)
                //finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(this)!=null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}