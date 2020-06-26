package com.example.dkdus.cashbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dkdus.cashbook.databinding.ActivityDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private String date, time, type, contents, money, category;
    private String TAG = DetailActivity.class.getSimpleName();
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        Intent intent = getIntent();
        if(intent != null) {
            type = intent.getStringExtra("type");
            contents = intent.getStringExtra("contents");
            money = intent.getStringExtra("money");
            date = intent.getStringExtra("date");
            category = intent.getStringExtra("category");
            time = intent.getStringExtra("time");

            binding.typeDetailText.setText(type);
            binding.cotentDetailText.setText(contents);
            binding.moneyDetailText.setText(money);
            binding.dateDetailText.setText(date);
            binding.categoryDetailText.setText(category);
        }
        reference = database.getReference().child("users").child(mUser.getUid()).child(date).child(time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.update_menu:
                Intent sendIntent = new Intent(this, AddActivity.class);
                sendIntent.putExtra("category",category);
                sendIntent.putExtra("money", money);
                sendIntent.putExtra("date",date);
                sendIntent.putExtra("type",type);
                sendIntent.putExtra("contents",contents);
                sendIntent.putExtra("time", time);
                startActivity(sendIntent);
                return true;
            case R.id.delete_menu:
                reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailActivity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "실패하였습니다.");
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
