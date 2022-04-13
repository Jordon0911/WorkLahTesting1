package com.example.worklah.newFragment

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.worklah.remainedFile.MainActivity
import com.example.worklah.R
import com.example.worklah.databinding.FragmentAccountOverviewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AccountOverviewFragment : Fragment() {
    private var _binding: FragmentAccountOverviewBinding? = null
    private val binding get() = _binding!!

    //firebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    //realtime database
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountOverviewBinding.inflate(inflater, container, false)

        //init firebase auth
        firebaseAuth= FirebaseAuth.getInstance()
        checkUser()

        //handle click, logout
        binding.logoutButton.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        //TO JY: i add the below 2 line for u to link to job history
        //TO JY: and the fun viewJobHistory below is commented out
        binding.userJobHistory.setOnClickListener{
            this.findNavController().navigate(R.id.action_profileFragment2_to_jobHistoryFragment)
        }

        return binding.root
    }

    private fun checkUser() {

        //check user is logged in or not
        val firebaseUser=firebaseAuth.currentUser

        //user not null, user is logged in
        if(firebaseUser!=null){

            //get data from database
            databaseReference = Firebase.database.getReference("Users").child(firebaseUser.uid)

            databaseReference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    //update user profile
                    binding.userFirstName.text=dataSnapshot.child("fname")
                        .getValue<String>()
                    binding.userLastName.text=dataSnapshot.child("lname").getValue<String>()
                    binding.userEmail.text=dataSnapshot.child("email").getValue<String>()
                    binding.userContactNo.text=dataSnapshot.child("phone").getValue<String>()
                    binding.userIC.text=dataSnapshot.child("ic").getValue<String>()
                    binding.userDOB.text=dataSnapshot.child("dob").getValue<String>()
                    binding.userAddress.text=dataSnapshot.child("address").getValue<String>()
                    binding.userBank.text=dataSnapshot.child("bankAcc").getValue<String>()

                    val gender=dataSnapshot.child("gender").getValue<String>()
                    if(binding.userMale.text==gender){
                        binding.userMale.isChecked=true
                        binding.userMale.setTextColor(Color.parseColor("#FFFFFF"))
                    }else{
                        binding.userFemale.isChecked=true
                        binding.userFemale.setTextColor(Color.parseColor("#FFFFFF"))
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }
            })

        }else{
            //user is null, user is not logged in, goto login activity
            //startActivity(Intent(this, MainActivity::class.java))
            startActivity(Intent(activity, MainActivity::class.java))
            //finish()
            getActivity()?.finish()
        }

    }

    /*
    fun viewJobHistory(view: android.view.View) {
        val intent = Intent(activity, JobHistoryActivity::class.java)
        startActivity(intent)
    }
     */
}

