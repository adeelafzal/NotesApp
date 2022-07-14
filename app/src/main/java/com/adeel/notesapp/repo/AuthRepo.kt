package com.adeel.notesapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adeel.notesapp.api.UserApi
import com.adeel.notesapp.model.UserRequest
import com.adeel.notesapp.model.UserResponse
import com.adeel.notesapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class AuthRepo @Inject constructor(private val userApi: UserApi) {

    private val _userLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userLiveData: LiveData<NetworkResult<UserResponse>> get() = _userLiveData

    suspend fun registerUser(userRequest: UserRequest) {
        _userLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signup(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userLiveData.postValue(NetworkResult.Loading())
        val response = userApi.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        when {
            response.isSuccessful -> {
                _userLiveData.postValue(NetworkResult.Success(response.body()!!))
            }
            response.errorBody() != null -> {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
                _userLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            }
            else -> {
                _userLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        }
    }

}