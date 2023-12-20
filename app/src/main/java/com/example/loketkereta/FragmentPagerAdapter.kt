package com.example.loketkereta

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class FragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = ArrayList<Fragment>()
    private val fragmentTitles = ArrayList<String>()
    private var currentFragment: Fragment? = null

    fun addFragment(fragment: Fragment, title: String) {
        fragments.add(fragment)
        fragmentTitles.add(title)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        currentFragment = fragments[position]
        super.onBindViewHolder(holder, position, payloads)
    }

    fun getCurrentFragment(): Fragment? {
        return currentFragment
    }

    fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitles[position]
    }
}