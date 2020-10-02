package com.ozgurberat.newsprojectkotlin.view.login

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_create_account_part2.*

class CreateAccountPart2Fragment : Fragment() {

    private val TAG = "CreateAccountPart2Fragment"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_account_part2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        val name = requireArguments().getString("name")
        val surname = requireArguments().getString("surname")
        val email = requireArguments().getString("email")
        val password = requireArguments().getString("password")

        val topicsList: ArrayList<String> = arrayListOf()




        create_account_done_button.setOnClickListener {

            val checkedChipIds = chip_group.checkedChipIds
            for (id in checkedChipIds) {
                val chip = view.findViewById<Chip>(id)
                topicsList.add(chip.text.toString())
            }

            if (topicsList.size == 0) {
                val snackbar: Snackbar = Snackbar.make(view, "You need to select atleast one topic!", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("Got it", object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        snackbar.dismiss()
                    }
                })
                snackbar.setTextColor(Color.WHITE)
                snackbar.setActionTextColor(resources.getColor(R.color.PINK))
                snackbar.show()
                return@setOnClickListener
            }

            create_account_part2_progress_bar.visibility = View.VISIBLE


            if (email != null && password != null) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val sharedPreferences = activity?.getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
                        sharedPreferences!!.edit().putString("name", name).apply()
                        sharedPreferences.edit().putString("surname", surname).apply()
                        sharedPreferences.edit().putString("email", email).apply()
                        sharedPreferences.edit().putString("password", password).apply()
                        val stringSet: HashSet<String> = hashSetOf()
                        for (topic in topicsList) {
                            stringSet.add(topic)
                        }
                        sharedPreferences.edit().putStringSet("topics", stringSet).apply()


                        val documentReference = mFirestore.collection("users").document(mAuth.currentUser!!.uid)
                        val userMap: HashMap<String, Any?> = hashMapOf(
                            "name" to name,
                            "surname" to surname,
                            "email" to email,
                            "password" to password,
                            "topics" to topicsList
                        )
                        documentReference.set(userMap)
                            .addOnSuccessListener {
                                Log.d(TAG, "onViewCreated: CreateAccountPart2Fragment: Saved in database")
                            }
                            .addOnFailureListener {
                                Log.d(TAG, "onViewCreated: CreateAccountPart2Fragment: failure: "+it.localizedMessage)
                            }

                        val currentUser = mAuth.currentUser
                        currentUser!!.sendEmailVerification()
                            .addOnSuccessListener {
                                Log.d(TAG, "onViewCreated: verification is sent")
                                Toast.makeText(fragment.context,
                                    "Verification link will be sent to your email in a few minutes.", Toast.LENGTH_LONG).show()
                                val action = CreateAccountPart2FragmentDirections.actionCreateAccountPart2FragmentToCreateAccountPart3Fragment()
                                Navigation.findNavController(view).navigate(action)
                            }
                            .addOnFailureListener { exception ->
                                Log.d(TAG, "onViewCreated: "+exception.localizedMessage)
                            }

                    }
                    else {
                        Toast.makeText(requireContext(), task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    create_account_part2_progress_bar.visibility = View.GONE
                }
            }

        }

    }
}






