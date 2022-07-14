package com.adeel.notesapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.adeel.notesapp.R
import com.adeel.notesapp.databinding.FragmentLoginBinding
import com.adeel.notesapp.model.UserRequest
import com.adeel.notesapp.utils.Helper
import com.adeel.notesapp.utils.Helper.Companion.validateCredentials
import com.adeel.notesapp.utils.NetworkResult
import com.adeel.notesapp.utils.TokenManager
import com.adeel.notesapp.vm.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val viewModel by activityViewModels<AuthViewModel>()
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
        }
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindListeners()
        bindObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bindListeners() {
        _binding!!.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        _binding!!.btnLogin.setOnClickListener {
            Helper.hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                viewModel.loginUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }
    }

    private fun getUserRequest(): UserRequest {
        return _binding!!.run {
            UserRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                ""
            )
        }
    }

    private fun showValidationErrors(error: String) {
        _binding!!.txtError.text =
            String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = _binding!!.txtEmail.text.toString()
        val password = _binding!!.txtPassword.text.toString()
        return validateCredentials(emailAddress, "", password, true)
    }

    private fun bindObserver() {
        viewModel.userData.observe(viewLifecycleOwner, Observer {
            _binding!!.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
                is NetworkResult.Error -> {
                    showValidationErrors(it.message!!)
                }
                is NetworkResult.Loading -> {
                    _binding!!.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroy() {
        _binding = null;
        super.onDestroy()
    }

}