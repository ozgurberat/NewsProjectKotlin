package com.ozgurberat.newsprojectkotlin.view.main.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.ozgurberat.newsprojectkotlin.R
import com.ozgurberat.newsprojectkotlin.view.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.*
import kotlin.collections.HashSet

class ProfileFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mAuth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
        val name = sharedPreferences!!.getString("name", "")
        val surname = sharedPreferences.getString("surname", "")
        val email = sharedPreferences.getString("email", "")
        val topicsSet = sharedPreferences.getStringSet("topics", hashSetOf()) as HashSet

        profile_name_text.setText(Html.fromHtml("<b>Name: </b> ${name!!.capitalize(Locale.getDefault())}"))
        profile_surname_text.setText(Html.fromHtml("<b>Surname: </b> ${surname!!.capitalize(Locale.getDefault())}"))
        profile_email_text.setText(Html.fromHtml("<b>Email: </b> ${email!!}"))
        profile_interests_header.text = Html.fromHtml("<b>Interests </b>")

        //Get user's currently selected interests and set it to a textView
        var interestsText = ""
        val iterator = topicsSet.iterator()
        while (iterator.hasNext()) {
            interestsText += iterator.next() + ", "
        }

        val charArray = interestsText.toCharArray()
        val charList = charArray.dropLast(2)
        interestsText = ""
        for (char in charList) {
            interestsText += char
        }
        profile_interests.text = interestsText.capitalize(Locale.getDefault()) + "."


        edit_profile_button.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        fab.setOnClickListener {
            val intent = Intent(activity, EditInterestsActivity::class.java)
            startActivity(intent)
        }

        profile_log_out.setOnClickListener {
            AuthUI.getInstance()
                .signOut(this.requireContext())
                .addOnCompleteListener {
                    val intent = Intent(activity, LoginActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }
        }

    }

}