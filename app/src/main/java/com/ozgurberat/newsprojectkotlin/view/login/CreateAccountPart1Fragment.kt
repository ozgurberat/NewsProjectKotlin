package com.ozgurberat.newsprojectkotlin.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_create_account_part1.*
import kotlinx.android.synthetic.main.fragment_create_account_part2.*

class CreateAccountPart1Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account_part1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        next_button.setOnClickListener {

            val name = name_edit_text.text.toString()
            val surname = surname_edit_text.text.toString()
            val email = create_account_email_edit_text.text.toString()
            val password = create_account_password_edit_text.text.toString()
            val confirmPassword = create_account_confirm_password_edit_text.text.toString()

            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                if (name.isEmpty()) {
                    name_edit_text.error = "Name is required."
                }
                if (surname.isEmpty()) {
                    surname_edit_text.error = "Surname is required."
                }
                if (email.isEmpty()) {
                    create_account_email_edit_text.error = "Email is required."
                }
                if (password.isEmpty()) {
                    create_account_password_edit_text.error = "Password is required."
                }
                if (confirmPassword.isEmpty()) {
                    create_account_confirm_password_edit_text.error = "Confirm password is required."
                }
            }
            else {
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(requireActivity().applicationContext, "Password and confirm password must match", Toast.LENGTH_SHORT).show()
                }
                else {
                    val bundle = Bundle()
                    bundle.putString("name", name)
                    bundle.putString("surname", surname)
                    bundle.putString("email", email)
                    bundle.putString("password", password)
                    Navigation.findNavController(it).navigate(R.id.action_createAccountPart1Fragment_to_createAccountPart2Fragment, bundle)
                }
            }
        }
    }
}