package com.ozgurberat.newsprojectkotlin.view.main.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.view.login.LoginActivity
import kotlinx.android.synthetic.main.activity_edit_profile.*
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private val TAG = "EditProfileActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        setSupportActionBar(edit_profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setInformationsAboutUser()


        //Change Password
        edit_profile_change_password.setOnClickListener {

            val inflater = LayoutInflater.from(it.context)
            val dialogView = inflater.inflate(R.layout.new_password_dialog, null)

            val currentPassword = dialogView.findViewById<EditText>(R.id.current_password_edittext)
            val newPassword = dialogView.findViewById<EditText>(R.id.new_password_edittext)
            val confirmPassword = dialogView.findViewById<EditText>(R.id.confirm_password_edittext)
            val buttonCancel = dialogView.findViewById<Button>(R.id.new_password_cancel_button)
            val buttonChange = dialogView.findViewById<Button>(R.id.new_password_change_button)

            val builder = AlertDialog.Builder(it.context)
            val alertDialog = builder.create()
            alertDialog.setView(dialogView)
            alertDialog.show()

            buttonChange?.setOnClickListener {
                if (currentPassword.text.isEmpty() || newPassword.text.isEmpty() || confirmPassword.text.isEmpty()) {
                    if (currentPassword.text.isEmpty()) {
                        currentPassword.error = "This field cannot be empty"

                    }
                    if (newPassword.text.isEmpty()) {
                        newPassword.error = "This field cannot be empty"

                    }
                    if (confirmPassword.text.isEmpty()) {
                        confirmPassword.error = "This field cannot be empty"
                    }
                } else {
                    val sharedPreferences =
                        getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
                    val currentPw = sharedPreferences!!.getString("password", "")
                    println(currentPw)
                    println(currentPassword.text)
                    if (currentPassword.text.toString() != currentPw) {
                        currentPassword.error = "Your current password is different than that."
                        return@setOnClickListener
                    }
                    if (currentPassword.text.toString() == newPassword.text.toString()) {
                        newPassword.error = "Please enter a different password."
                        return@setOnClickListener
                    }
                    if (confirmPassword.text.toString() != newPassword.text.toString()) {
                        newPassword.error = "New Password and Confirm Password must match."
                        return@setOnClickListener
                    }

                    mAuth.currentUser!!.updatePassword(newPassword.text.toString())
                        .addOnCompleteListener {
                            Toast.makeText(
                                this,
                                "SUCCESS! Your password is changed.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    sharedPreferences.edit().putString("password", newPassword.text.toString())
                        .apply()
                    val n = sharedPreferences.getString("password", "")
                    println("new: $n")

                    alertDialog.dismiss()
                }
            }

            buttonCancel.setOnClickListener {
                alertDialog.dismiss()
            }

        }

        edit_profile_save_button.setOnClickListener {
            val name = edit_profile_name_edittext.text.toString()
            val surname = edit_profile_surname_edittext.text.toString()

            if (name.isEmpty()) {
                edit_profile_name_edittext.error = "Cannot save empty name"
                return@setOnClickListener
            }
            if (surname.isEmpty()) {
                edit_profile_surname_edittext.error = "Cannot save empty surname"
                return@setOnClickListener
            }

            val sharedPreferences =
                getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("name", name).apply()
            sharedPreferences.edit().putString("surname", surname).apply()

            val documentReference = mFirestore.collection("users").document(mAuth.currentUser!!.uid)
            documentReference.update("name", name)
                .addOnFailureListener {
                    Log.d(TAG, "onCreate: ${it.message}")
                }
            documentReference.update("surname", surname)
                .addOnSuccessListener {
                    Toast.makeText(this, "Saved successfully!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(TAG, "onCreate: ${it.message}")
                    Toast.makeText(this, "Error! Try Again Later.", Toast.LENGTH_SHORT).show()
                }

        }

        edit_profile_delete_account.setOnClickListener {

            val inflater = LayoutInflater.from(it.context)
            val dialogView = inflater.inflate(R.layout.delete_account_dialog, null)

            val deleteAccountEditText =
                dialogView.findViewById<EditText>(R.id.delete_account_edittext)
            val buttonCancel = dialogView.findViewById<Button>(R.id.delete_account_cancel_button)
            val buttonDelete = dialogView.findViewById<Button>(R.id.delete_account_delete_button)

            val builder = AlertDialog.Builder(it.context)
            val alertDialog = builder.create()
            alertDialog.setView(dialogView)
            alertDialog.show()



            buttonDelete.setOnClickListener {

                if (deleteAccountEditText.text.toString() != "delete") {
                    deleteAccountEditText.error = "Please type 'delete' to confirm."
                }
                else {
                    val sharedPreferences =
                        getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
                    val email = sharedPreferences!!.getString("email", "")
                    val password = sharedPreferences.getString("password", "")
                    println(email + " ," + password)
                    val credential = EmailAuthProvider.getCredential(email!!, password!!)
                    mAuth.currentUser!!.reauthenticate(credential).addOnCompleteListener {
                        if (it.isComplete && it.isSuccessful) {
                            mAuth.currentUser!!.delete().addOnCompleteListener {
                                if (it.isSuccessful) {
                                    Toast.makeText(this.applicationContext, "User deleted!", Toast.LENGTH_LONG).show()
                                    Log.d(TAG, "onCreate: ${it.result} ${it}")
                                    val intent = Intent(this@EditProfileActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else {
                                    Log.d(TAG, "onCreate: ${it.exception?.message}")
                                    Toast.makeText(
                                        this.applicationContext,
                                        "${it.exception?.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            buttonCancel.setOnClickListener {
                                alertDialog.dismiss()
                            }
                        }
                        else {
                            Toast.makeText(
                                this.applicationContext,
                                "${it.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }

        }
    }

    private fun setInformationsAboutUser() {
        val sharedPreferences = getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
        val name = sharedPreferences!!.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")

        edit_profile_name_edittext.setText(Html.fromHtml("<i>${name!!.capitalize(Locale.getDefault())}</i>"))
        edit_profile_surname_edittext.setText(Html.fromHtml("<i>${surname!!.capitalize(Locale.getDefault())}</i>"))

    }
}










