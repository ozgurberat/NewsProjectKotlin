package com.ozgurberat.newsprojectkotlin.view.main.profile

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ozgurberat.newsprojectkotlin.R
import kotlinx.android.synthetic.main.activity_edit_interests.*

class EditInterestsActivity : AppCompatActivity() {

    private val TAG = "EditInterestsActivity"

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mFirestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_interests)

        mAuth = FirebaseAuth.getInstance()
        mFirestore = FirebaseFirestore.getInstance()

        setSupportActionBar(edit_interests_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        //Get user's current topics and set checked all of them
        val sharedPreferences = getSharedPreferences(mAuth.currentUser!!.uid, Context.MODE_PRIVATE)
        val topicsSet = sharedPreferences.getStringSet("topics", hashSetOf()) as HashSet
        val iterator = topicsSet.iterator()
        val topicsArrayList = arrayListOf<String>()
        while (iterator.hasNext()) {
            topicsArrayList.add(iterator.next())
        }

        for (i in 0..chip_group.childCount) {
            val chip = chip_group.getChildAt(i) as? Chip
            if (topicsArrayList.contains(chip?.text)) {
                println("Chip: ${chip?.text}")
                chip?.isChecked = true
            }

        }



        //Save button
        edit_interests_save_button.setOnClickListener {

            //Get checked chips and save it in sharedPreferences & firebase database
            val topicsList = arrayListOf<String>()
            val checkedChipIds = chip_group.checkedChipIds
            for (id in checkedChipIds) {
                val chip = findViewById<Chip>(id)
                topicsList.add(chip.text.toString())
            }

            if (topicsList.size == 0) {
                val snackbar: Snackbar = Snackbar.make(it, "You need to select atleast one topic!", Snackbar.LENGTH_INDEFINITE)
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

            val stringSet: HashSet<String> = hashSetOf()
            for (topic in topicsList) {
                stringSet.add(topic)
            }
            sharedPreferences!!.edit().putStringSet("topics", stringSet).apply()


            val documentReference = mFirestore.collection("users").document(mAuth.currentUser!!.uid)
            documentReference.update("topics", topicsList)
                .addOnSuccessListener {
                    Toast.makeText(this, "Your interests are updated!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.d(TAG, "onViewCreated: ${it.message}")
                }
        }

    }
}










