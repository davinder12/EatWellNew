package com.android.eatwell.view.dashboard.fragment.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.afollestad.assent.Permission
import com.afollestad.assent.isAllGranted
import com.afollestad.assent.runWithPermissions
import com.android.eatwell.data.models.FoodDataMap
import com.android.eatwell.utilitiesclasses.baseclass.BaseListFragment
import com.android.eatwell.view.common.NavigationScreen
import com.android.eatwell.view.dashboard.viewmodel.MapViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.ClusterManager.OnClusterItemInfoWindowClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_map.*
import eatwell.com.eatwell.R
import eatwell.com.eatwell.databinding.FragmentMapBinding
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : BaseListFragment<FragmentMapBinding>(), OnMapReadyCallback,
    OnClusterItemInfoWindowClickListener<FoodDataMap.Body> {

    @Inject
    lateinit var navigationScreen: NavigationScreen

    private val viewModel: MapViewModel by viewModels()

    private var mClusterManager: ClusterManager<FoodDataMap.Body>? = null

    override val layoutRes: Int
        get() = R.layout.fragment_map

    var googleMap: GoogleMap? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFrag?.getMapAsync(this)

        //(requireActivity() as DashboardActivity).viewModel.updateTitle(getString(R.string.map))

        bindNetworkState(viewModel.networkState, loadingIndicator = progressBar)

        viewModel.data.observe(viewLifecycleOwner) {
            mClusterManager?.addItems(it)
            clusterMethod()
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(viewModel.  getLocation(), 7f))

        }
    }


    private fun clusterMethod() {
        mClusterManager?.let { clusterManager ->
            googleMap?.let { googleMap ->
                googleMap.uiSettings.isZoomControlsEnabled = true
                googleMap.setOnCameraIdleListener(clusterManager)
                googleMap.setOnMarkerClickListener(clusterManager)
                googleMap.setOnInfoWindowClickListener(clusterManager)
                clusterManager.renderer = ClusterMapFragment(
                    requireActivity(),
                    googleMap,
                    clusterManager
                )
                clusterManager.setOnClusterItemInfoWindowClickListener(this)
                clusterManager.cluster()
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        when {
            isAllGranted(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) -> callMethod(googleMap)
            else -> runWithPermissions(
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION
            ) { callMethod(googleMap) }
        }
    }

    @SuppressLint("MissingPermission")
    fun callMethod(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        googleMap?.isMyLocationEnabled = true
        mClusterManager = ClusterManager(requireContext(), googleMap)
        viewModel.getResturantList()

    }


    override fun onClusterItemInfoWindowClick(item: FoodDataMap.Body?) {
        item?.let { items ->
            navigationScreen.productDetailScreen(items.id, items.storename)

//            val signOutBottomSheet = MapProductList.create(it as ArrayList<SearchApiResponse.Body.ProductInfo>,item.merchant_info.restaurent_id)
//            signOutBottomSheet.show(childFragmentManager, signOutBottomSheet.tag)
        }
    }
}
