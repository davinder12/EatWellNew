package com.android.mealpass.view.dashboard.fragment

import android.os.Bundle
import android.view.View
import com.android.mealpass.utilitiesclasses.baseclass.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import mealpass.com.mealpass.R
import mealpass.com.mealpass.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    override val layoutRes: Int
        get() = R.layout.fragment_setting

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}