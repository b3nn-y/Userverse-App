package com.bennysamuel.userverse.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bennysamuel.userverse.TempFragment
import com.bennysamuel.userverse.fragments.HomeScreenFragment
import com.bennysamuel.userverse.fragments.StaggerdHomeFragment
import com.bennysamuel.userverse.fragments.UserDetailsFragment

//class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
//    FragmentStateAdapter(fragmentManager, lifecycle) {
//
//    override fun getItemCount(): Int {
//        return 2 // Number of fragments
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return when (position) {
//            0 -> HomeScreenFragment()
//            1 -> StaggerdHomeFragment()
//            else -> throw IllegalArgumentException("Invalid position")
//        }
//    }
//}