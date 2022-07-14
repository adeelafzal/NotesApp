package com.adeel.notesapp.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager

class Helper {
    companion object {
        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun hideKeyboard(view: View) {
            try {
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (e: Exception) {

            }
        }

        fun validateCredentials(emailAddress: String, userName: String, password: String,
                                isLogin: Boolean) : Pair<Boolean, String> {

            var result = Pair(true, "")
            if(TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(password)){
                result = Pair(false, "Please provide the credentials")
            }
            else if(!Helper.isValidEmail(emailAddress)){
                result = Pair(false, "Email is invalid")
            }
            else if(!TextUtils.isEmpty(password) && password.length <= 5){
                result = Pair(false, "Password length should be greater than 5")
            }
            return result
        }

    }

}