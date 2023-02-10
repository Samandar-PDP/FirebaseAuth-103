package com.sdk.firebaseauth.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sdk.firebaseauth.R
import com.sdk.firebaseauth.databinding.FragmentRegisterBinding
import com.sdk.firebaseauth.model.User
import com.sdk.firebaseauth.toast

class RegisterFragment : Fragment() {
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val fireStore by lazy { FirebaseFirestore.getInstance() }
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            binding.pr.isVisible = true
            binding.btnRegister.isEnabled = false
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val fullName = binding.fullname.text.toString().trim()
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotBlank()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        saveToFireStore(email, password, fullName)
                        binding.pr.isVisible = false
                        binding.btnRegister.isEnabled = true
                        toast("Successfully registered")
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    }
                    .addOnFailureListener {
                        toast(it.message.toString())
                    }
            }
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun saveToFireStore(email: String, password: String, fullName: String) {
        fireStore.collection("users")
            .add(User(fullName, email, password))
            .addOnSuccessListener {
                println("success")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}