package com.adeel.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.adeel.notesapp.R
import com.adeel.notesapp.databinding.FragmentRegisterBinding
import com.adeel.notesapp.model.UserRequest
import com.adeel.notesapp.utils.Helper
import com.adeel.notesapp.utils.NetworkResult
import com.adeel.notesapp.utils.TokenManager
import com.adeel.notesapp.vm.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null
    private val viewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindListeners()
        bindObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindListeners() {
        binding!!.btnLogin.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding!!.btnSignUp.setOnClickListener {
            Helper.hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                viewModel.registerUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }
    }

    private fun getUserRequest(): UserRequest {
        return binding!!.run {
            UserRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                txtUsername.text.toString(),
            )
        }
    }

    private fun showValidationErrors(error: String) {
        binding!!.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = binding!!.txtEmail.text.toString()
        val password = binding!!.txtPassword.text.toString()
        val userName = binding!!.txtUsername.text.toString()
        return Helper.validateCredentials(emailAddress, userName, password, false)
    }

    private fun bindObserver() {
        viewModel.userData.observe(viewLifecycleOwner, Observer {
            binding!!.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    showValidationErrors(it.message!!)
                }
                is NetworkResult.Loading -> {
                    binding!!.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

}