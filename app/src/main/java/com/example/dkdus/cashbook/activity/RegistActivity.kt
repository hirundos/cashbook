package com.example.dkdus.cashbook.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dkdus.cashbook.R
import com.example.dkdus.cashbook.activity.LoginActivity
import com.example.dkdus.cashbook.activity.RegistActivity
import com.example.dkdus.cashbook.databinding.ActivityRegistBinding
import com.google.firebase.auth.FirebaseAuth

class RegistActivity : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth? = null
    private val TAG = RegistActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityRegistBinding = DataBindingUtil.setContentView(this, R.layout.activity_regist)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.registerBtn.setOnClickListener {
            val email = binding.username.text.toString()
            val pwd = binding.password.text.toString()
            firebaseAuth!!.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this@RegistActivity) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this@RegistActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (pwd.length < 6) {
                            Toast.makeText(this@RegistActivity, task.exception!!.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegistActivity, "이미 아이디가 존재합니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
        }
    }
}