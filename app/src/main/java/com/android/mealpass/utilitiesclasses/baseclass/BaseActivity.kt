package com.android.mealpass.utilitiesclasses.baseclass

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.android.mealpass.data.models.SaveReceiptRequestModel
import com.android.mealpass.data.network.NetworkState
import com.android.mealpass.utilitiesclasses.PagedListViewModel
import com.android.mealpass.utilitiesclasses.ResourceViewModel
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundAdapterClass
import com.android.mealpass.utilitiesclasses.baseadapter.DataBoundPagedListAdapter
import com.android.mealpass.view.common.NavigationScreen
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign


abstract class BaseActivity : AppCompatActivity() {


    protected fun requirePaymentRequest() = requireObject(NavigationScreen.EXTRA_PAYMENT)

    /**
     * [ViewModelFactory] which uses Dagger2 for dependency injection
     */


    /*This is for viewPagerAdapter Adapter*/

    fun <X : DataBoundAdapterClass<T, *>, T> X.setup(
        viewPager: ViewPager2,
        data: List<T>
    ) {
        viewPager.adapter = this
        submitList(data)
    }


    protected fun <X : DataBoundPagedListAdapter<T, *>, T> initAdapter(
        adapter: X,
        recyclerView: RecyclerView,
        viewModel: PagedListViewModel<T>,
        hasFixedSize: Boolean = true,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recyclerView.setHasFixedSize(hasFixedSize)
        recyclerView.adapter = adapter
        subscriptions += adapter.retryClicks.subscribe(viewModel::retry)
        clickHandler?.let { subscribe(adapter.clicks, it) }
        viewModel.items.observe(
            this,
            Observer { adapter.submitList(it) })
        viewModel.frontLoadingState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        viewModel.endLoadingState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0) recyclerView.scrollToPosition(0)
            }
        })
        return adapter
    }

    protected fun <X : DataBoundAdapterClass<T, *>, T> initAdapter(
        adapter: X,
        recycler: RecyclerView,
        list: LiveData<List<T>?>,
        clickHandler: ((T) -> Unit)? = null
    ): X {
        recycler.adapter = adapter
        list.observe(this, Observer {
            adapter.submitList(it)
        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
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
        list.observe(this, Observer {
            adapter.submitList(it)
        })
        adapter.retryClicks.subscribe(viewModel::retry)
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
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
        data.observe(this, Observer {
            adapter.submitList(it)
        })
        clickHandler?.let { subscribe(adapter.clicks, it) }
        return adapter
    }


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
     * Container for RxJava subscriptions.
     */
    private val subscriptions = CompositeDisposable()


    fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }
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
        networkState.observe(this, Observer {
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

    private fun requireBoolean(tag: String): Boolean {
        return intent.getBooleanExtra(tag, false)
    }

    private fun requireString(tag: String): String {
        var field = ""
        intent.getStringExtra(tag)?.let { field = it }
        return field
    }


    private fun requireObject(tag: String): SaveReceiptRequestModel? {
        var activeReceiptRespose: SaveReceiptRequestModel? = null
        intent.getParcelableExtra<SaveReceiptRequestModel>(tag)?.let {
            activeReceiptRespose = it
        }
        return activeReceiptRespose
    }

}
