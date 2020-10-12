package com.openjobs.openjobs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() , View.OnClickListener {

    private val RC_SIGN_IN = 0;
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkAndRespondToSignedInState(false)
        signInButton.setOnClickListener(this)
    }

    private fun checkAndRespondToSignedInState(shouldRedirect : Boolean) {
        if (FirebaseAuth.getInstance().currentUser == null) {
            if(shouldRedirect){
                // Choose authentication providers
                val providers = arrayListOf(
                    AuthUI.IdpConfig.PhoneBuilder().build()
                )

                // Create and launch sign-in intent
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
        } else {
            gotoSignedInUserActivity()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in

                val user : FirebaseUser? = FirebaseAuth.getInstance().currentUser
                gotoSignedInUserActivity()
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.e(TAG,"sign in failed")
            }
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == signInButton.id){
            checkAndRespondToSignedInState(true)
        }
    }

    private fun gotoSignedInUserActivity(){
        val intent = Intent(this,SignedInUserActivity::class.java)
        startActivity(intent)
        this.finishAfterTransition()
    }
}