package com.example.dkdus.cashbook;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.dkdus.cashbook.databinding.ActivityAddBinding;
import com.example.dkdus.cashbook.model.ChildList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    DateTimeFormatter Dateformatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    DateTimeFormatter Hourformatter = DateTimeFormatter.ofPattern("HHmmss");
    String today_date;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String time;
    private TextView textDate;
    private boolean update = false;
    private String intentdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityAddBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_add);

        today_date = LocalDate.now().format(Dateformatter);
        binding.inputDate.setText(today_date);
        time = LocalTime.now().format(Hourformatter);
        Intent intent = getIntent();

        if(intent != null && (intent.getStringExtra("date"))!= null){
            update = true;
            String intenttype = intent.getStringExtra("type");
            String intentcontents = intent.getStringExtra("contents");
            String intentmoney = intent.getStringExtra("money");
            intentdate = intent.getStringExtra("date");
            String intentcategory = intent.getStringExtra("category");
            time = intent.getStringExtra("time");

            int id = whatcategory(intentcategory);
            RadioButton radioButton = (RadioButton) findViewById(id);
            radioButton.setChecked(true);
            binding.inputType.setText(intenttype);
            binding.inputContents.setText(intentcontents);
            binding.inputMoney.setText(intentmoney);
            binding.inputDate.setText(intentdate);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        binding.inputDate.setOnClickListener(new View.OnClickListener() {
            LocalDate todayDate;
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                todayDate = LocalDate.parse(binding.inputDate.getText(), Dateformatter);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddActivity.this, dateSetListener,
                        todayDate.getYear(),todayDate.getMonthValue(),todayDate.getDayOfMonth());
                binding.inputDate.setText(today_date);
                datePickerDialog.show();
            }
        });

        final String finalTime = time;
        binding.inputButton.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                String IDdate = binding.inputDate.getText().toString();

                int id = binding.radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(id);
                String category = radioButton.getText().toString();

                String type = binding.inputType.getText().toString();
                String contents = binding.inputContents.getText().toString();
                String money = binding.inputMoney.getText().toString();

                ChildList childList = new ChildList(type, contents, money, IDdate, category, finalTime);

                Map<String, Object> childUpdates = new HashMap<>();

               childUpdates.put("/users"+"/"+mUser.getUid()+"/"+IDdate+"/"+ finalTime, childList);
               databaseReference.updateChildren(childUpdates);

               //날짜를 변경할 경우 처리
               if(update && !IDdate.equals(intentdate)){
                   DatabaseReference reference = firebaseDatabase.getReference().child("users").child(mUser.getUid()).child(intentdate).child(time);
                   reference.removeValue();
               }
                Intent gointent = new Intent(AddActivity.this, MainActivity.class);
               startActivity(gointent);
            }
        });
    }

    private int whatcategory(String intentcategory) {
        if(intentcategory.equals("지출")){
            return R.id.radioButton1;
        } else if(intentcategory.equals("수입")){
            return R.id.radioButton2;
        } else if(intentcategory.equals("저축")){
            return R.id.radioButton3;
        }

        return 0;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            LocalDate today = LocalDate.of(year, month, dayOfMonth);
            today_date = today.format(Dateformatter);
           // textDate.setText(today.format(Dateformatter));
        }
    };
}
