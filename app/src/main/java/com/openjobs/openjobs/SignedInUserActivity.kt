package com.openjobs.openjobs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import kotlinx.android.synthetic.main.activity_signed_in_user.*

class SignedInUserActivity : AppCompatActivity() {

    val TAG = "SignedInUserActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signed_in_user)
        setUpToolBar()
        val navFragment = supportFragmentManager.findFragmentById(fragment_container_view_tag.id) as NavHostFragment
        bottomNavigation?.setupWithNavController(navFragment.navController)
    }

    private fun setUpToolBar(){
        toolbar.title =getString(R.string.app_name)
        with(toolbar){
            inflateMenu(R.menu.menu_signed_in_user)
            setOnMenuItemClickListener{
                if(it.itemId == R.id.signOutItem){
                    signOutUser()
                }
                true
            }
        }
    }

    private fun signOutUser(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        this.finishAfterTransition()
    }
}