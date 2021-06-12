package com.demo.paradoxcatapp.ui.component.register

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.task.starter.data.DataRepositorySource
import com.task.starter.data.Resource
import com.task.starter.data.dto.user.User
import com.task.starter.data.error.CHECK_YOUR_FIELDS
import com.task.starter.data.error.PASS_WORD_ERROR
import com.task.starter.data.error.USER_NAME_ERROR
import com.task.starter.ui.base.BaseViewModel
import com.task.starter.utils.SingleEvent
import com.task.starter.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RegisterViewModel @Inject constructor(private val dataRepository: DataRepositorySource) :
    BaseViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val isRegisterPrivate = MutableLiveData<Resource<Boolean>>()
    val isRegister: LiveData<Resource<Boolean>> get() = isRegisterPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val loginStatusPrivate = MutableLiveData<Resource<Boolean>>()
    val loginStatusLiveData: LiveData<Resource<Boolean>> get() = loginStatusPrivate


    fun doRegister(fullname: String, userName: String, passWord: String) {
        val isUsernameValid = userName.trim().length > 4
        val isPassWordValid = passWord.trim().length > 4
        if (isUsernameValid && !isPassWordValid) {
            isRegisterPrivate.value = Resource.DataError(PASS_WORD_ERROR)
        } else if (!isUsernameValid && isPassWordValid) {
            isRegisterPrivate.value = Resource.DataError(USER_NAME_ERROR)
        } else if (!isUsernameValid || !isPassWordValid || fullname.isEmpty()) {
            isRegisterPrivate.value = Resource.DataError(CHECK_YOUR_FIELDS)
        } else {
            val user = User(fullname, userName, passWord)
            viewModelScope.launch {
                isRegisterPrivate.value = Resource.Loading()
                wrapEspressoIdlingResource {
                    dataRepository.registerUser(user).collect { isAdded ->
                        isRegisterPrivate.value = isAdded
                    }

                }
            }
        }
    }

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

    /** Error handling as UI **/

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }
}
