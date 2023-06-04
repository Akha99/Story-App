package com.amb.dailysnap2.ui.user

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.amb.dailysnap2.R
import com.amb.dailysnap2.data.model.User
import com.amb.dailysnap2.databinding.ActivityLoginBinding
import com.amb.dailysnap2.ui.story.MainActivity
import com.amb.dailysnap2.util.ViewModelFactory
import com.amb.dailysnap2.data.Result
import com.amb.dailysnap2.data.networking.response.LoginResponse

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupLAVModel()
        setupAction()
        setAnimation()
    }


    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            if (valid()) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPass.text.toString()

                userViewModel.userLogin(email, password).observe(this) { result ->
                    when (result) {
                        is Result.Success -> handleLoginSuccess(result.data)
                        is Result.Loading -> showProgressIndicator(true)
                        is Result.Error -> handleLoginError(result.error)
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.check_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun handleLoginSuccess(data: LoginResponse) {
        showProgressIndicator(false)

        val user = User(
            data.loginResult?.name.toString(),
            data.loginResult?.token.toString(),
            true
        )

        saveUserData(user)

        startActivity(Intent(this, MainActivity::class.java))
        finishAffinity()
    }

    private fun handleLoginError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        showProgressIndicator(false)
    }

    private fun setAnimation() {
        val appIcon = ObjectAnimator.ofFloat(binding.icon, View.ALPHA, 1f).setDuration(700)
        val appName = ObjectAnimator.ofFloat(binding.tvStory, View.ALPHA, 1f).setDuration(700)
        val etEmail = ObjectAnimator.ofFloat(binding.etEmail, View.ALPHA, 1f).setDuration(700)
        val etPass = ObjectAnimator.ofFloat(binding.etPass, View.ALPHA, 1f).setDuration(700)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(700)
        val txtHaveAc = ObjectAnimator.ofFloat(binding.tvDontHaveAcc, View.ALPHA, 1f).setDuration(700)
        val txtSignup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(700)

        val textAnimation = AnimatorSet().apply {
            playTogether(appName, txtSignup, txtHaveAc)
            duration = 700
        }
        val layoutAnimation = AnimatorSet().apply {
            playTogether(etPass, etEmail)
            duration = 700
        }

        AnimatorSet().apply {
            playSequentially(
                appIcon,
                textAnimation,
                layoutAnimation,
                btnLogin
            )
            start()
        }
    }

    private fun saveUserData(user: User) {
        userViewModel.saveUser(user)
    }

    private fun setupLAVModel() {
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun valid() =
        binding.etEmail.error == null && binding.etPass.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPass.text.isNullOrEmpty()

}