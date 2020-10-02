package com.ozgurberat.newsprojectkotlin.view.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.fragment_create_account_part3.*

class CreateAccountPart3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account_part3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        back_to_the_login_screen.setOnClickListener {
            val action = CreateAccountPart3FragmentDirections.actionCreateAccountPart3FragmentToSignInFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }
}