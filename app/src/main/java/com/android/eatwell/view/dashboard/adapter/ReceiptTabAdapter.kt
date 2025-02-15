package com.android.eatwell.view.dashboard.adapter

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.android.eatwell.view.dashboard.fragment.receipt.ActiveReceiptFragment
import com.android.eatwell.view.dashboard.fragment.receipt.UsedReceiptFragment

class ReceiptTabAdapter(fm: FragmentManager, private var totalTabs: Int) : FragmentStatePagerAdapter(fm) {

    private var currentFragment: UsedReceiptFragment? = null

    override fun getItem(position: Int): Fragment {
        return when (position) {
            ACTIVE_RECEIPTS_FRAGMENT -> ActiveReceiptFragment() // 0 -> Navigate Active Receipt Fragment
            USED_RECEIPTS_FRAGMENT -> UsedReceiptFragment() // 1 -> Navigage Used Receipt Fragment
            else -> TODO("Invalid tab requested")
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }

    fun getCurrentFragment(): Fragment? {
        return currentFragment
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (getCurrentFragment() !== `object` && `object` is UsedReceiptFragment) {
            currentFragment = `object`
        }
    }

    companion object {
        const val ACTIVE_RECEIPTS_FRAGMENT = 0
        const val USED_RECEIPTS_FRAGMENT = 1
    }

}
