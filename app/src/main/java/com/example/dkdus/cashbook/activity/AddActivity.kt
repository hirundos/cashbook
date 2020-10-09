package com.example.dkdus.cashbook.activity

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dkdus.cashbook.R
import com.example.dkdus.cashbook.databinding.ActivityAddBinding
import com.example.dkdus.cashbook.model.ChildList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(api = Build.VERSION_CODES.O)
class AddActivity : AppCompatActivity() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.reference
    var Dateformatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
    var Hourformatter = DateTimeFormatter.ofPattern("HHmmss")
    var todayDate: String? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUser: FirebaseUser
    private lateinit var time: String
    private lateinit var date : String
    private var update = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
//        val binding: ActivityAddBinding = DataBindingUtil.setContentView(this, R.layout.activity_add)
        todayDate = LocalDate.now().format(Dateformatter)
        input_date.setText(todayDate.toString())
        time = LocalTime.now().format(Hourformatter)

        if (intent is Intent){
            var childList : ChildList? = intent.getParcelableExtra("list")
            if(childList is ChildList){

                update = true
                time = childList.time!!
                date = childList.date!!

                val id = whatCategory(childList.category!!)
                val radioButton = findViewById<View>(id) as RadioButton

                radioButton.isChecked = true
                input_type.setText(childList.type)
                input_contents.setText(childList.contents)
                input_money.setText(childList.money)
                input_date.setText(childList.date)
                }
            }
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser!!
        input_date.setOnClickListener(object : View.OnClickListener {

            @RequiresApi(api = Build.VERSION_CODES.O)
            override fun onClick(v: View) {
                val todayDate = LocalDate.parse(input_date.text, Dateformatter)
                var datePickerDialog = DatePickerDialog(this@AddActivity, dateSetListener,
                        todayDate.getYear(), todayDate.getMonthValue(), todayDate.getDayOfMonth())
                input_date.setText(todayDate.toString())
                datePickerDialog.show()
            }
        })
        val finalTime = time
        input_button.setOnClickListener {
            val IDdate = input_date.text.toString()
            val id = radioGroup.checkedRadioButtonId
            val radioButton = findViewById<View>(id) as RadioButton
            val category = radioButton.text.toString()
            val type = input_type.text.toString()
            val contents = input_contents.text.toString()
            val money = input_money.text.toString()

            val childList = ChildList(type, contents, money, IDdate, category, finalTime)
            val childUpdates: MutableMap<String, Any> = HashMap()
            childUpdates["/users" + "/" + mUser!!.uid + "/" + IDdate + "/" + finalTime] = childList
            databaseReference.updateChildren(childUpdates)

            //날짜를 변경할 경우 처리
            if (update && IDdate != date) {
                val reference = firebaseDatabase.reference.child("users").child(mUser.uid).child(date).child(time)
                reference.removeValue()
            }
            val goIntent = Intent(this@AddActivity, MainActivity::class.java)
            startActivity(goIntent)
        }
    }

    private fun whatCategory(intentCategory: String): Int {
        if (intentCategory == "지출") {
            return R.id.radioButton1
        } else if (intentCategory == "수입") {
            return R.id.radioButton2
        } else if (intentCategory == "저축") {
            return R.id.radioButton3
        }
        return 0
    }

    private val dateSetListener = OnDateSetListener { view, year, month, dayOfMonth ->
        val today = LocalDate.of(year, month, dayOfMonth)
        todayDate = today.format(Dateformatter)
    }

    companion object {
        private val TAG = AddActivity::class.java.simpleName
    }
}