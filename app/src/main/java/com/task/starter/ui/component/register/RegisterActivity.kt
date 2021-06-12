package com.task.starter.ui.component.register

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import com.demo.paradoxcatapp.ui.component.register.RegisterViewModel
import com.google.android.material.snackbar.Snackbar
import com.task.databinding.RegisterActivityBinding
import com.task.starter.data.Resource
import com.task.starter.ui.base.BaseActivity
import com.task.starter.ui.component.recipes.RecipesListActivity
import com.task.starter.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : BaseActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    private lateinit var binding: RegisterActivityBinding


    override fun initViewBinding() {
        binding = RegisterActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnRegister.setOnClickListener { doRegister() }
    }

    private fun doRegister() {
        viewModel.doRegister(
            binding.fullname.text.toString(),
            binding.username.text.trim().toString(),
            binding.password.text.toString()
        )
    }


    override fun observeViewModel() {
        observe(viewModel.isRegister, ::handleIsUserRegistration)
        observe(viewModel.loginStatusLiveData, ::updateLoginStatus)
        observeSnackBarMessages(viewModel.showSnackBar)
        observeToast(viewModel.showToast)
    }

    private fun updateLoginStatus(resource: Resource<Boolean>) {
        when(resource){
            is Resource.Success ->{
                resource.data?.let {
                    if(it){
                        navigateToMainScreen()
                    }
                }
            }
        }
    }


    private fun handleIsUserRegistration(isRegister: Resource<Boolean>) {
        when (isRegister) {
            is Resource.Loading -> {
                binding.loaderView.toVisible()
            }
            is Resource.Success -> {
                isRegister.data?.let {
                    binding.loaderView.toGone()
                    if (it){
                        viewModel.updateLoginStatus()
                    }

                }
            }
            is Resource.DataError -> {
                binding.loaderView.toGone()
                isRegister.errorCode?.let { viewModel.showToastMessage(it) }
            }
        }
    }


    private fun navigateToMainScreen() {
        val nextScreenIntent = Intent(this, RecipesListActivity::class.java)
        nextScreenIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(nextScreenIntent)
        finish()
    }

    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

}
