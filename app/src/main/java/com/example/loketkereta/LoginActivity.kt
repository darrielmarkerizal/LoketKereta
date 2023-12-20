package com.example.loketkereta

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.loketkereta.databinding.ActivityLoginBinding
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
    }

    private fun setupViewPager() {
        val adapter = FragmentPagerAdapter(this)

        adapter.addFragment(LoginFragment(), "Login")
        adapter.addFragment(RegisterFragment(), "Register")

        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }
}