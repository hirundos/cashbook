package com.example.dkdus.cashbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.dkdus.cashbook.R
import com.example.dkdus.cashbook.activity.AddActivity
import com.example.dkdus.cashbook.activity.DetailActivity
import com.example.dkdus.cashbook.databinding.ActivityDetailBinding
import com.example.dkdus.cashbook.model.ChildList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var mUser: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var childList : ChildList
    private var date: String? = null
    private var time: String? = null
    private val TAG = DetailActivity::class.java.simpleName
    private var reference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mUser = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()

        if (intent is Intent) {
            childList = intent.getParcelableExtra("list")
            date = childList.date
            time = childList.time

            type_detail_text.text = childList.type
            content_detail_text.text = childList.contents
            money_detail_text.text = childList.money
            date_detail_text.text = date
            category_detail_text.text = childList.category
        }
        reference = database.reference.child("users").child(mUser.uid).child(date!!).child(time!!)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_update, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.update_menu -> {
                val sendIntent = Intent(this, AddActivity::class.java)
                sendIntent.putExtra("list",childList)
                startActivity(sendIntent)
                true
            }
            R.id.delete_menu -> {
                reference!!.removeValue().addOnSuccessListener {
                    Toast.makeText(this@DetailActivity, "삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }.addOnFailureListener { Log.d(TAG, "실패하였습니다.") }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}