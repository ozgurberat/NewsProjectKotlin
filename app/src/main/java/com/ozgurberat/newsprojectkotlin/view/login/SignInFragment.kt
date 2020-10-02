package com.ozgurberat.newsprojectkotlin.view.login

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_sign_in.*

class SignInFragment : Fragment() {

    private val TAG = "SignInFragment"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            val sharedPreferences = activity?.getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
            val isLoggedInBefore = sharedPreferences!!.getBoolean("login", false)
            if (isLoggedInBefore) {
                val intent = Intent(activity, MainActivity::class.java)
                intent.putExtra("id", mAuth.currentUser!!.uid)
                startActivity(intent)
                activity?.finish()
            }
        }

        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Create account
        create_account_text.setOnClickListener {
            val action = SignInFragmentDirections.actionSignInFragmentToCreateAccountPart1Fragment()
            Navigation.findNavController(it).navigate(action)
        }

        login_button.setOnClickListener {
            val email = sign_in_email_edit_text.text.toString()
            val password = sign_in_password_edit_text.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    sign_in_email_edit_text.error = "Email should not be empty"
                }
                if (password.isEmpty()) {
                    sign_in_password_edit_text.error = "Password should not be empty"
                }
            }
            else {
                var currentUser: FirebaseUser
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser = mAuth.currentUser!!
                        if (currentUser.isEmailVerified) {
                            val intent = Intent(activity, MainActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }
                        else {
                            val alertDialogBuilder = AlertDialog.Builder(view.context)
                            alertDialogBuilder.setTitle("Verification Failed")
                            alertDialogBuilder.setMessage("Please verify your email first")
                            alertDialogBuilder.setPositiveButton(
                                "Send Link",
                                object : DialogInterface.OnClickListener {
                                    override fun onClick(dialog: DialogInterface?, which: Int) {
                                        currentUser.sendEmailVerification()
                                            .addOnSuccessListener {
                                                Log.d(TAG, "onViewCreated: verification is sent")
                                                Toast.makeText(
                                                    fragment.context,
                                                    "Verification link will be sent to your email in a few minutes.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                            .addOnFailureListener { exception ->
                                                Log.d(TAG, "onViewCreated: " + exception.localizedMessage)
                                                Toast.makeText(fragment.context, exception.message.toString(), Toast.LENGTH_LONG).show()
                                            }
                                    }
                                })
                            alertDialogBuilder.setCancelable(true)
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                            val buttonPositive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                            buttonPositive.setTextColor(
                                ContextCompat.getColor(
                                    fragment.requireContext(),
                                    R.color.RED
                                )
                            )
                            currentUser.reload()
                        }
                    }
                    else {
                        Toast.makeText(this.context, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }

        forgot_password.setOnClickListener {

            val inflater = LayoutInflater.from(it.context)
            val dialogView = inflater.inflate(R.layout.forgot_password_dialog, null)

            val editTextEmail = dialogView.findViewById<EditText>(R.id.forgot_password_email_edittext)
            val buttonSend = dialogView.findViewById<Button>(R.id.forgot_password_send_button)
            val buttonCancel = dialogView.findViewById<Button>(R.id.forgot_password_cancel_button)

            val builder = AlertDialog.Builder(it.context)
            val alertDialog = builder.create()
            alertDialog.setView(dialogView)
            alertDialog.show()

            buttonSend?.setOnClickListener {
                val userEmail = editTextEmail?.text.toString()
                if (userEmail.isEmpty()) {
                    editTextEmail?.error = "Email cannot be empty"
                }
                else {
                    mAuth.sendPasswordResetEmail(userEmail)
                        .addOnSuccessListener {
                            Toast.makeText(fragment.context,
                                "Reset link will be sent to your email in a few minutes.", Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(fragment.context, exception.message.toString(), Toast.LENGTH_LONG).show()
                        }
                }

            }


            buttonCancel.setOnClickListener {
                alertDialog.dismiss()
            }

        }
    }



}




