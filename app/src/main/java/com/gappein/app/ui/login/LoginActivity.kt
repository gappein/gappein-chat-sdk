package com.gappein.app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.gappein.app.R
import com.gappein.app.data.AppPreference
import com.gappein.app.ui.chat.ChatActivity
import com.gappein.sdk.Gappein
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val RC_SIGN_IN = 100
        private const val TAG = "LoginActivity"
        private const val TEST_TOKEN = "Himanshu"
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupOnClickListener()
    }

    private fun checkIfUserIsNotRegistered() {
        val user = AppPreference.getUser()

        if (user != null) {
            setupOnClickListener()
        } else {
            setupGoogleSignIn()
            setupOnClickListener()
        }
    }

    private fun setupOnClickListener() {
        buttonLogin.setOnClickListener {
            val currentUser = User(
                token = "eTCwHFMrfjMoX3vfYxQrrpIINYs2",
                profileImageUrl = "https://lh3.googleusercontent.com/a-/AOh14Ghy8H-FJrgWB2TcxHCOKWxWEQB4AiklYADGTydu4nE=s96-c",
                name = "Himanshu Singh",
                createdAt = Date()
            )
            AppPreference.setUser(currentUser)
            goToNext(currentUser)

        }
    }

    private fun setupGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = FirebaseAuth.getInstance()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { firebaseAuthWithGoogle(it) }
                Log.d("Sddsdssd", account?.idToken.toString())
                Log.d("Sddsdssd", account?.email.toString())
            } catch (ignored: ApiException) {
                Log.d(TAG, "--- Error caught: " + ignored.stackTraceToString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    setupUser(user)
                } else {
                    Log.d(TAG, task.exception.toString())
                }
            }
    }

    private fun setupUser(user: FirebaseUser?) {

        if (user != null) {
            val currentUser = User(
                token = user.uid,
                profileImageUrl = user.photoUrl.toString(),
                name = user.displayName.toString(),
                createdAt = Date()
            )
            AppPreference.setUser(currentUser)
            goToNext(currentUser)

        }
    }

    private fun goToNext(currentUser: User) {
        Gappein.getInstance().setUser(
            currentUser,
            token = currentUser.token, onSuccess = {
                ChatClient.getInstance().openOrCreateChannel(TEST_TOKEN, {
                    startActivity(ChatActivity.buildIntent(this))
                }) {
                    Log.d("Sddsdssd1", currentUser.toString())
                    Log.d("Sddsdssd2", it.message.toString())
                }
            }, onError = {
                Log.d("Sddsdssd3", currentUser.toString())
                Log.d("Sddsdssd4", it.message.toString())
            }
        )
    }
}