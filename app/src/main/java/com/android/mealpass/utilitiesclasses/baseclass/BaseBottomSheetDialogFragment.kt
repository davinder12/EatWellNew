package com.android.mealpass.utilitiesclasses.baseclass

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

abstract class BaseBottomSheetDialogFragment<TBinding : ViewDataBinding> :
    BottomSheetDialogFragment() {


    companion object {
        const val INTEREST_KEY = "interest"
        const val PROFILE_KEY = "profile"
        const val FLAG = "flag"
    }


//    /**
//     * The layout resource ID for the fragment. This is inflated automatically.
//     */
//    abstract var popUpDialog: Dialog?

    lateinit var activityContext: Context

    /**
     * The layout resource ID for the fragment. This is inflated automatically.
     */
    abstract val layoutRes: Int
        @LayoutRes get

    protected open lateinit var binding: TBinding


    /**
     * Called during onCreate, immediately after the view has been inflated.
     * Override this to bind values to the view.
     * [ViewDataBinding.executePendingBindings] will be executed after this method.
     */
    protected open fun onBindView(binding: TBinding) {}


    /**
     * Subscribes to a [Observable] and handles disposing.
     */
    fun <T> subscribe(stream: Observable<T>?, handler: (T) -> Unit) {
        if (stream == null) return
        subscriptions += stream.subscribe(handler) {
            //  Timber.e(it)
        }
    }

    /**
     * Creates the [ViewDataBinding] for this view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        activityContext = requireContext()
        onBindView(binding)
        binding.executePendingBindings()
        return binding.root
    }

    /**
     * Container for RxJava subscriptions.
     */
    private val subscriptions = CompositeDisposable()


    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }

    private fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    protected fun bindNetworkState(
        networkState: LiveData<NetworkState>,
        @StringRes success: Int? = null,
        @StringRes error: Int? = null,
        loadingIndicator: View? = null,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        networkState.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    loadingIndicator?.visibility = View.VISIBLE
                }
                NetworkState.Status.FAILED -> {
                    loadingIndicator?.visibility = View.GONE
                    showMessage(it.msg)
                }
                NetworkState.Status.SUCCESS -> {
                    loadingIndicator?.visibility = View.GONE
                    success?.let {
                        showMessage(resources.getString(it))
                    }
                    onSuccess?.invoke()
                }
            }
        })
    }

    // TODO Need to refactor retryClick  funtionality
    @SuppressLint("CheckResult")
    protected fun <X : DataBoundAdapterClass<T, *>, T, R> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        viewModel: ResourceViewModel<R>,
        list: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        adapter.retryClicks.subscribe(viewModel::retry)
        viewModel.networkState.observe(viewLifecycleOwner, adapter::setNetworkState)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


    @SuppressLint("CheckResult")
    protected fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        list: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it?.toList())
        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


    fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        viewPager: ViewPager2,
        data: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        viewPager.adapter = adapter
        data.observe(this, adapter::submitList)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }
}