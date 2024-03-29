package com.swago.seenthemlive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.swago.seenthemlive.api.setlistfm.Setlist
import com.swago.seenthemlive.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_login.sign_in_button
import java.io.Serializable


class LoginActivity : BaseActivity() {

    //Google Login Request Code
    private val RC_SIGN_IN = 7
    //Google Sign In Client
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        sign_in_button.setOnClickListener({
            signIn()
        })

        val logout = intent.getBooleanExtra(INTENT_LOGOUT, false)

        if (logout) {
            val editor = getSharedPreferences(
                getString(
                    R.string.spotify_shared_pref_key
                ),
                MODE_PRIVATE
            ).edit()
            editor.remove("token")
            editor?.apply()
            mAuth.signOut()
            mGoogleSignInClient.signOut()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                account?.let {
                    firebaseAuthWithGoogle(account)
                }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
                // ...
            }

        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInWithCredential:success ${mAuth.currentUser?.displayName}")
                    val user = mAuth.currentUser
                    val username = usernameFromEmail(user!!.email!!)
                    val db = FirebaseFirestore.getInstance()
                    Log.d("CLOUDFIRESTORE", "Getting user")
                    db.collection("users").document(user.uid)
                        .get()
                        .addOnSuccessListener { documentReference ->
                            Log.d("CLOUDFIRESTORE", "Got User: ${documentReference}")
                            val existingUser: User? = documentReference.toObject(LoginActivity.User::class.java)
                            if (existingUser == null) {
                                saveNewUser(user!!)
                            }
                        }
                        .addOnFailureListener { e ->
                            saveNewUser(user!!)
                        }
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInWithCredential:failure", task.exception)
                    Toast.makeText(this,"Auth Failed", Toast.LENGTH_LONG).show()
                    updateUI(null)
                }

                // ...
            }
    }

    fun saveNewUser(user: FirebaseUser) {
        val username = usernameFromEmail(user!!.email!!)
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user.uid)
            .set(User(id = user.uid, username = username, email = user.email, displayName = user.displayName))
            .addOnSuccessListener { documentReference ->
                Log.d("CLOUDFIRESTORE", "Saved User: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("CLOUDFIRESTORE", "Failed to save user", e)
            }
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            //Do your Stuff
//            val db = FirebaseFirestore.getInstance()
//            db.collection("users").document(user.uid)
//                .get()
//                .addOnSuccessListener { documentReference ->
//                    val user: User? = documentReference.toObject(User::class.java)
//                    if (user?.setlists == null) {
//                        user?.setlists = ArrayList()
//                    }
//                    Log.d("CLOUDFIRESTORE", "DocumentSnapshot retrieved with ID: ${user}")
//                    val intent = ViewConcertsActivity.newIntent(this, user!!.id!!)
//                    startActivity(intent)
//                }
//                .addOnFailureListener { e ->
//                    Log.w("CLOUDFIRESTORE", "Error adding document", e)
//                }
            val intent = ViewConcertsActivity.newIntent(this, user.uid)
            startActivity(intent)
            finish()
        }
    }

    private fun usernameFromEmail(email: String): String? {
        return if (email.contains("@")) {
            email.split("@".toRegex()).toTypedArray()[0]
        } else {
            email
        }
    }

    data class User(
        var id: String? = null,
        var username: String? = null,
        var email: String? = null,
        var displayName: String? = null,
        var setlists: List<Setlist>? = null
    ) : Serializable

    companion object {

        private val INTENT_LOGOUT = "logout"

        fun newIntent(context: Context, logout: Boolean): Intent {
            val intent = Intent(context, LoginActivity::class.java)
            intent.putExtra(INTENT_LOGOUT, logout)
            return intent
        }
    }
}
