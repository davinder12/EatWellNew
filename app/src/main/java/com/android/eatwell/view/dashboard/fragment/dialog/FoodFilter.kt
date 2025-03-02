package com.android.eatwell.view.dashboard.fragment.dialog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.eatwell.data.extension.throttleClicks
import com.android.eatwell.data.models.ProductTypeResponse
import com.android.eatwell.utilitiesclasses.baseclass.BaseBottomSheetDialogFragment
import com.android.eatwell.view.dashboard.DashboardActivity
import com.android.eatwell.view.dashboard.adapter.FoodFilterAdapter
import com.android.eatwell.view.dashboard.viewmodel.FoodFilterViewModel
import dagger.hilt.android.AndroidEntryPoint
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.DialogfilterBinding

@AndroidEntryPoint
class FoodFilter : BaseBottomSheetDialogFragment<DialogfilterBinding>() {

    private val viewModel: FoodFilterViewModel by viewModels()

    override val layoutRes: Int
        get() = R.layout.dialogfilter


    companion object {
        const val OPEN_RESTURANT = "1"
        const val CLOSE_RESTURANT = "0"
        const val MIN_TIME = 0f
        const val MAX_TIME = 1440f
        const val MIN_TIME_LABEL = "00:00"
        const val MAX_TIME_LABEL = "24:00"

        lateinit var callBack: () -> Unit

        var listOfProductType: List<ProductTypeResponse.Body>? = null
        fun create(listOfProductType: List<ProductTypeResponse.Body>?, callBackMethod: () -> Unit): FoodFilter {
            this.listOfProductType = listOfProductType
            callBack = callBackMethod
            return FoodFilter()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.seekbar.setMinStartValue(viewModel.minValueInit)
        binding.seekbar.setMaxStartValue(viewModel.maxValueInit)
        binding.seekbar.apply()


        binding.seekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            viewModel.seekBarChangeListener(minValue, maxValue)
        }
        bindNetworkState(viewModel.resource.networkState, loadingIndicator = binding.progressBar3)

        if (listOfProductType.isNullOrEmpty()) viewModel.callApi() else viewModel.updateList(listOfProductType)

        viewModel.updateList.observe(viewLifecycleOwner) {
            (requireActivity() as DashboardActivity).viewModel.foodList = it
        }

        initAdapter(FoodFilterAdapter(), binding.recyclerView, viewModel.dataList) {
            it.isItemSelected = !it.isItemSelected
            binding.recyclerView.adapter?.notifyDataSetChanged()
        }
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        subscribe(binding.apply.throttleClicks()) {
            viewModel.saveFilterRecord()
            callBack.invoke()
            dialog?.dismiss()
        }
        subscribe(binding.cancel.throttleClicks()) {
            dialog?.dismiss()
        }

    }

    override fun onBindView(binding: DialogfilterBinding) {
        binding.vm = viewModel
    }
}