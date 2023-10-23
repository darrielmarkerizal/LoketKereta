package com.example.loketkereta

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.loketkereta.OnBoardingFragment


class OnBoardingAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                "Selamat Datang di Aplikasi LoketKereta",
                "Mulailah perjalanan petualangan Anda dengan aplikasi kami. Temukan tiket kereta, jadwal perjalanan, dan rencanakan perjalanan kereta Anda dengan mudah",
                R.raw.onboarding1
            )
            1 -> OnBoardingFragment.newInstance(
                "Temukan Tujuan Perjalanan Terbaik",
                "Temukan berbagai destinasi menarik yang dapat Anda jangkau dengan kereta api. Aplikasi kami akan membantu Anda menemukan tujuan perjalanan impian Anda",
                R.raw.onboarding2
            )
            else -> OnBoardingFragment.newInstance(
               "Mudah dan Aman",
                "Kami berkomitmen untuk memberikan pengalaman perjalanan kereta yang nyaman dan aman. Dapatkan tiket Anda dengan cepat, pantau perjalanan Anda, dan nikmati perjalanan tanpa khawatir dengan bantuan aplikasi kami",
                R.raw.onboarding3
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}