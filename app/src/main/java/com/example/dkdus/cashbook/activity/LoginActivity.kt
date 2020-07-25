package com.example.dkdus.cashbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dkdus.cashbook.R
import com.example.dkdus.cashbook.activity.LoginActivity
import com.example.dkdus.cashbook.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private lateinit var mFirebaseAuth : FirebaseAuth
    private val mAuthListener: AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mFirebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        binding.signInGoogleBtn.setOnClickListener {
            val singIntent = mGoogleSignInClient.getSignInIntent()
            startActivityForResult(singIntent, RC_SIGN_IN)
        }
        binding.registBtn.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistActivity::class.java)
            startActivity(intent)
        }
        binding.loginBtn.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            mFirebaseAuth!!.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        } else {
                            // Toast.makeText(LoginActivity.this,"Error",Toast.LENGTH_SHORT).show();
                            Toast.makeText(this@LoginActivity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Log.e(TAG, "Google Sign-In failed")
            }
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "Connected fail")
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account!!.id)
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    Log.d(TAG, "signInWit" +
                            "hCredential : onComplete" + task.isSuccessful)
                    if (!task.isSuccessful) {
                        Log.w(TAG, "signInWithCredential", task.exception)
                        Toast.makeText(this@LoginActivity, "Authentication failed",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                }
    }

    companion object {
        private const val RC_SIGN_IN = 1004
        private val TAG = LoginActivity::class.java.simpleName
    }
}