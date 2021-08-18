package com.android.mealpass.view.dashboard.fragment.receipt

import android.os.Bundle
import android.view.View
import com.android.mealpass.utilitiesclasses.baseclass.BaseListFragment
import com.android.mealpass.view.dashboard.adapter.ReceiptTabAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_receipt.*
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentReceiptBinding

@AndroidEntryPoint
class ReceiptCategoryFragment : BaseListFragment<FragmentReceiptBinding>() {

    override val layoutRes: Int get() = R.layout.fragment_receipt

    companion object{
        const val ACTIVE_RECEIPTS ="Active Receipts"
        const val USED_RECEIPTS ="Used Receipts"
    }
    lateinit var pagerAdapter: ReceiptTabAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // (requireActivity() as DashboardActivity).viewModel.updateTitle(getString(R.string.receipts))
        initTabViewAndAdapter()
        tabSelectedListener()
    }


    private fun tabSelectedListener() {
        receiptTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                receiptViewPager.currentItem = tab.position
                pagerAdapter.getCurrentFragment()?.let {
                    (it as UsedReceiptFragment).updateList()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun initTabViewAndAdapter() {
        receiptTabLayout.addTab(receiptTabLayout.newTab().setText(ACTIVE_RECEIPTS))
        receiptTabLayout.addTab(receiptTabLayout.newTab().setText(USED_RECEIPTS))
        pagerAdapter = ReceiptTabAdapter(requireActivity().supportFragmentManager, receiptTabLayout.tabCount)
        receiptViewPager.adapter = pagerAdapter
        receiptViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(receiptTabLayout))
    }


}

