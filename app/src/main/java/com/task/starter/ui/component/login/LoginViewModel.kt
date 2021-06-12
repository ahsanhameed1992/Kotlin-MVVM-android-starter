package com.task.starter.ui.component.login

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.starter.data.DataRepository
import com.task.starter.data.Resource
import com.task.starter.data.dto.login.LoginRequest
import com.task.starter.data.dto.login.LoginResponse
import com.task.starter.data.dto.user.User
import com.task.starter.data.error.CHECK_YOUR_FIELDS
import com.task.starter.data.error.PASS_WORD_ERROR
import com.task.starter.data.error.USER_NAME_ERROR
import com.task.starter.ui.base.BaseViewModel
import com.task.starter.utils.RegexUtils.isValidEmail
import com.task.starter.utils.SingleEvent
import com.task.starter.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository: DataRepository) : BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val loginLiveDataPrivate = MutableLiveData<Resource<Boolean>>()
    val loginLiveData: LiveData<Resource<Boolean>> get() = loginLiveDataPrivate

    /** Error handling as UI **/

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate


    fun doLogin(userName: String, passWord: String) {
        val isUsernameValid = userName.trim().length > 4
        val isPassWordValid = passWord.trim().length > 4
        if (isUsernameValid && !isPassWordValid) {
            loginLiveDataPrivate.value = Resource.DataError(PASS_WORD_ERROR)
        } else if (!isUsernameValid && isPassWordValid) {
            loginLiveDataPrivate.value = Resource.DataError(USER_NAME_ERROR)
        } else if (!isUsernameValid && !isPassWordValid) {
            loginLiveDataPrivate.value = Resource.DataError(CHECK_YOUR_FIELDS)
        } else {
            viewModelScope.launch {
                val user = User(null,username = userName,password = passWord)
                loginLiveDataPrivate.value = Resource.Loading()
                wrapEspressoIdlingResource {
                    dataRepository.doLogin(user).collect {
                        loginLiveDataPrivate.value = it
                    }
                }
            }
        }
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }


    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val loginStatusPrivate = MutableLiveData<Resource<Boolean>>()
    val loginStatusLiveData: LiveData<Resource<Boolean>> get() = loginStatusPrivate

    fun updateLoginStatus() {
        viewModelScope.launch {
            loginStatusPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepository.updateLoginStatus().collect { isAdded ->
                    loginStatusPrivate.value = isAdded
                }

            }
        }

    }
}