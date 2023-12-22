package com.example.loketkereta

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.loketkereta.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding  = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.btnToDashboard.setOnClickListener {
            val email = binding.emailInput.text.toString()
            val password = binding.passwordInput.text.toString()
            loginUser(email, password)
        }
    }

    private fun loginUser(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d(TAG, "signInWithEmail:success")

                    val sharedPreferences = requireActivity().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()

                    editor.putBoolean("isLoggedIn", true)

                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("users").child(user!!.uid)

                    myRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            val role = dataSnapshot.child("role").getValue(String::class.java)
                            editor.putString("role", role)
                            editor.apply()

                            if (role == "admin") {
                                val intent = Intent(activity, AdminActivity::class.java)
                                startActivity(intent)
                            } else {
                                val intent = Intent(activity, MainActivity::class.java)
                                startActivity(intent)
                            }
                            activity?.finish()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                        }
                    })
                } else {
                    Log.d(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }


}