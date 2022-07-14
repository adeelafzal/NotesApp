package com.adeel.notesapp.vm

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adeel.notesapp.model.UserRequest
import com.adeel.notesapp.model.UserResponse
import com.adeel.notesapp.repo.AuthRepo
import com.adeel.notesapp.utils.Helper
import com.adeel.notesapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepo: AuthRepo) : ViewModel() {

    val userData: LiveData<NetworkResult<UserResponse>> get() = userRepo.userLiveData

    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepo.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepo.loginUser(userRequest)
        }
    }

}