package com.android.eatwell.view.dashboard.fragment.receipt

import android.os.Bundle
import android.view.View
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.dashboard.adapter.ReceiptTabAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentReceiptBinding

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
        binding.receiptTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.receiptViewPager.currentItem = tab.position
                pagerAdapter.getCurrentFragment()?.let {
                    (it as UsedReceiptFragment).updateList()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun initTabViewAndAdapter() {
        binding.receiptTabLayout.addTab(binding.receiptTabLayout.newTab().setText(ACTIVE_RECEIPTS))
        binding.receiptTabLayout.addTab(binding.receiptTabLayout.newTab().setText(USED_RECEIPTS))
        pagerAdapter = ReceiptTabAdapter(requireActivity().supportFragmentManager, binding.receiptTabLayout.tabCount)
        binding.receiptViewPager.adapter = pagerAdapter
        // Unable to load fragment on Second Time So Add this line
        binding.receiptViewPager.isSaveFromParentEnabled = false;
        binding.receiptViewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.receiptTabLayout))
    }


}

