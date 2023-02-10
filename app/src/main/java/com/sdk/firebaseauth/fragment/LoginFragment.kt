package com.sdk.firebaseauth.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.sdk.firebaseauth.R
import com.sdk.firebaseauth.databinding.FragmentLoginBinding
import com.sdk.firebaseauth.toast


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            binding.btnLogin.isVisible = true
        } else {
            binding.pr.isVisible = false
            binding.btnLogin.isVisible = true
        }
        binding.btnLogin.setOnClickListener {
            binding.pr.isVisible = true
            binding.btnLogin.isEnabled = false
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    binding.pr.isVisible = false
                    binding.btnLogin.isEnabled = true
                    if (it.isSuccessful) {
                        toast("Successfully logged in")
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    } else {
                        toast(it.exception?.message.toString())
                    }
                }
        }
        binding.textReg.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}