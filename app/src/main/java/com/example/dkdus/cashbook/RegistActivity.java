package com.example.dkdus.cashbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dkdus.cashbook.databinding.ActivityRegistBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    private String TAG = RegistActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityRegistBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_regist);

        firebaseAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.username.getText().toString();
                final String pwd = binding.password.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(RegistActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(RegistActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else if(pwd.length() < 6){
                                    Toast.makeText(RegistActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegistActivity.this,"이미 아이디가 존재합니다.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}
