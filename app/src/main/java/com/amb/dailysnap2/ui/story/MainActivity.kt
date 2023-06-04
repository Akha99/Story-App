package com.amb.dailysnap2.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.amb.dailysnap2.databinding.ActivityMainBinding
import com.amb.dailysnap2.ui.adapter.LoadingStateAdapter
import com.amb.dailysnap2.ui.adapter.StoryAdapter
import com.amb.dailysnap2.ui.maps.MapsActivity
import com.amb.dailysnap2.ui.user.LoginActivity
import com.amb.dailysnap2.ui.user.UserViewModel
import com.amb.dailysnap2.util.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userViewModel: UserViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: MainViewModel by viewModels { factory }

        supportActionBar?.hide()

        setupMVModel()
        setupView()
        onClick()
    }

    //OnClick{
    private fun onClick() {
        binding.fabLogOut.setOnClickListener(::onClickLogOut)
        binding.fabAddStory.setOnClickListener(::onClickAddStory)
        binding.fabSetting.setOnClickListener(::onClickSetting)
        binding.fabMaps.setOnClickListener(::onClickMaps)

    }

    private fun onClickLogOut(view: View) {
        userViewModel.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun onClickAddStory(view: View) {
        startActivity(Intent(this, AddStoryActivity::class.java))
    }

    private fun onClickSetting(view: View) {
        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
    }
    private fun onClickMaps(view: View){
        startActivity(Intent(this, MapsActivity::class.java))
    }
    //OnClick}


    private fun setupView() {
        storyAdapter = StoryAdapter()
        mainViewModel.getUser().observe(this@MainActivity){ user ->
            if (user.isLogin){
                setStory(user.token)
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        with(binding.rvStory) {
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    storyAdapter.retry()
                })
        }

    }

    private fun setStory(token: String) {
        mainViewModel.getStory(token).observe(this@MainActivity) {
            storyAdapter.submitData(lifecycle, it)
            showProgressIndicator(false)
        }
    }

    private fun setupMVModel() {
        val Factory = ViewModelFactory.getInstance(this)

        mainViewModel = ViewModelProvider(this, Factory).get(MainViewModel::class.java)
        userViewModel = ViewModelProvider(this, Factory).get(UserViewModel::class.java)
    }

    private fun showProgressIndicator(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}