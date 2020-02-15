package com.example.firstapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    val mFirebase = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email = findViewById<EditText>(R.id.emailId)
        val password = findViewById<EditText>(R.id.passwordId)
        val signInButton = findViewById<Button>(R.id.LoginBtn)

        signInButton.setOnClickListener { view ->
            checkValidation(view,email,password)
        }
    }

    fun signIn(view: View, email: String, password: String) {
        showMessage(view, "Authenticating...")

        mFirebase.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                if (task.isSuccessful) {
                    var intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("id", mFirebase.currentUser?.email)
                    startActivity(intent)

                } else {
                    showMessage(view, "Error: ${task.exception?.message}")
                }
            })

    }

    private fun checkValidation(view:View,email: EditText?, password: EditText?) {
        var isEmailOK = true
        var isPasswordOk = true
        val emailStr = email?.text.toString()
        val passwordStr = password?.text.toString()
        if (email != null) {
            if (email.text.isEmpty()) {
                email.setError("please insert an email")
                email.requestFocus()
                isEmailOK = false
            } else if (password != null) {

                if (password.text.isEmpty()) {
                    password.setError("please insert an password")
                    password.requestFocus()
                    isPasswordOk = false
                } else if (password.text.length < 5) {
                    password.setError("password must be 5 characters at least")
                    password.requestFocus()
                    isPasswordOk = false
                }
            }

            if (isEmailOK && isPasswordOk) {
                Toast.makeText(this, "allow to logged in ", Toast.LENGTH_SHORT).show()

                signIn(view,emailStr,passwordStr)

            }
        } else {
            Toast.makeText(this, "something happened", Toast.LENGTH_SHORT).show()

        }
    }

    fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null)
            .show()
    }
}
