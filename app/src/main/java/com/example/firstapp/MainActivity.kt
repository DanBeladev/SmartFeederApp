package com.example.firstapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var fbAuth = FirebaseAuth.getInstance()

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)
         setSupportActionBar(toolbar)

         val editTextEmail = findViewById<EditText>(R.id.emailId)
         val editTextPass = findViewById<EditText>(R.id.passwordId)
         val signupButton = findViewById<Button>(R.id.signUpBtn)
         val nameText = findViewById<EditText>(R.id.nameId)
         val phoneNumber= findViewById<EditText>(R.id.phoneId)
         val alreadyExistUser = findViewById<TextView>(R.id.signupLabel)

         alreadyExistUser.setOnClickListener { view ->
             var intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)
         }

         signupButton.setOnClickListener {view ->
             checkValidation(editTextEmail, editTextPass,nameText,phoneNumber,view)
         }


     }

     private fun checkValidation(
         email: EditText?,
         password: EditText?,
         name: EditText?,
         phone: EditText?,
         view: View
     ) {
         var isEmailOK =true
         var isPasswordOk =true
         var isNameOk =true
         var isPhoneOk =true
         if (email != null) {
             if (email.text.isEmpty()) {
                 email.setError("please insert an email")
                 email.requestFocus()
                 isEmailOK=false
             } else if (password != null) {

                 if (password.text.isEmpty()) {
                     password.setError("please insert an password")
                     password.requestFocus()
                     isPasswordOk=false
                 } else if (password.text.length < 5) {
                     password.setError("password must be 5 characters at least")
                     password.requestFocus()
                     isPasswordOk=false
                 }
             }

             if(name!=null)
             {
                 if(name.text.isEmpty())
                 {
                     name.setError("please insert full name")
                     name.requestFocus()
                     isNameOk=false
                 }
             }
             else{
                 isNameOk=false
             }

             if(phone!=null)
             {
                 if(phone.text.isEmpty() || phone.text.length<10)
                 {
                     phone.setError("invalid phone number")
                     phone.requestFocus()
                     isPhoneOk=false
                 }
             }
             else{
                 isPhoneOk=false
             }

             if(isEmailOK && isPasswordOk && isNameOk && isPhoneOk)
             {
                 fbAuth.createUserWithEmailAndPassword(email.text.toString(), password?.text.toString()).addOnCompleteListener(this
                 ) { task ->
                     if(task.isSuccessful){
                         Toast.makeText(this,"allow to logged in ",Toast.LENGTH_SHORT).show()
                      val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("id", fbAuth.currentUser?.email)
                        startActivity(intent)

                     }else{
                         showMessage(view,"Error: ${task.exception?.message}")
                     }
                 }
             }
         } else {
             Toast.makeText(this,"something happened",Toast.LENGTH_SHORT).show()
         }


     }
    fun showMessage(view: View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

 }
