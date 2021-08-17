package com.android.mealpass.utilitiesclasses.baseclass

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import com.android.mealpass.data.network.NetworkState
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import mealpass.com.mealpass.R

abstract class BaseFragment<TBinding : ViewDataBinding> : BaseBackStack() {


    lateinit var activityContext: Context

    /**
     * The layout resource ID for the fragment. This is inflated automatically.
     */
    abstract val layoutRes: Int
        @LayoutRes get

    protected open lateinit var binding: TBinding


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
            Log.e("ifnormation", "sdf" + it.message)
            //  Timber.e(it)
        }
    }

    /**
     * Container for RxJava subscriptions.
     */
    val subscriptions = CompositeDisposable()


    override fun onDestroy() {
        super.onDestroy()
    }

    fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }


    fun showSnackMessage(message: String?) {
        message?.let {
            val snackBar = Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT)
            snackBar.show()
        }
    }

    fun locationSnackMessage() {
        Snackbar.make(requireView(), R.string.GpsValidationMsg, Snackbar.LENGTH_LONG)
            .setAction(R.string.SettingsTab) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }.show()
    }


    protected fun bindNetworkState(
        networkState: LiveData<NetworkState>,
        dialog: AlertDialog? = null,
        @StringRes success: Int? = null,
        @StringRes error: Int? = null,
        loadingIndicator: View? = null,
        onError: (() -> Unit)? = null,
        onSuccess: (() -> Unit)? = null
    ) {
        networkState.observe(viewLifecycleOwner, {
            when (it.status) {
                NetworkState.Status.RUNNING -> {
                    loadingIndicator?.visibility = View.VISIBLE
                    dialog?.show()
                }
                NetworkState.Status.FAILED -> {
                    showMessage(it.msg)
                    loadingIndicator?.visibility = View.GONE
                    dialog?.dismiss()
                    onError?.invoke()
                }
                NetworkState.Status.SUCCESS -> {
                    success?.let { showMessage(resources.getString(success)) }
                    loadingIndicator?.visibility = View.GONE
                    dialog?.dismiss()
                    onSuccess?.invoke()
                }
            }
        })
    }


}
