package com.example.dkdus.cashbook.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ExpandableListView
import androidx.appcompat.app.AppCompatActivity
import com.example.dkdus.cashbook.ExpandAdapter
import com.example.dkdus.cashbook.R
import com.example.dkdus.cashbook.model.ChildList
import com.example.dkdus.cashbook.model.ParentList
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mAuth: FirebaseAuth
    private var mUser: FirebaseUser? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val TAG = this.javaClass.toString()
    private var adapter: ExpandAdapter? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build()
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        if (mUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddActivity::class.java)
            startActivity(intent)
        }
        val database = FirebaseDatabase.getInstance()
        val parentReference = database.reference.child("users").child(mUser!!.uid)
        parentReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val Parent: MutableList<ParentList> = ArrayList()
                for (snapshot in dataSnapshot.children) {
                    val ParentKey = snapshot.key.toString()
                    val childReference = parentReference.child(ParentKey)
                    childReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot1: DataSnapshot) {
                            val Child: MutableList<ChildList> = ArrayList()
                            for (snapshot1 in dataSnapshot1.children) {
                                val ChildKey = snapshot1.key.toString()
                                val type = snapshot1.child("type").value.toString()
                                val contents = snapshot1.child("contents").value.toString()
                                val money = snapshot1.child("money").value.toString()
                                val category = snapshot1.child("category").value.toString()
                                val MyChildList = ChildList(type, contents, money, ParentKey, category, ChildKey)
                                Child.add(MyChildList)
                            }
                            Parent.add(ParentList(ParentKey, Child))
                            adapter = ExpandAdapter(this@MainActivity, Parent)
                            expanedlist.setAdapter(adapter)
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d(TAG, "Failed to read value." + databaseError.toException().toString())
                        }
                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
        expanedlist.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val childList = adapter!!.getChild(groupPosition, childPosition) as ChildList
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("list",childList)

            startActivity(intent)

            true
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out_menu -> {
                mGoogleSignInClient!!.signOut()
                        .addOnCompleteListener(this) {
                            mAuth.signOut()
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}