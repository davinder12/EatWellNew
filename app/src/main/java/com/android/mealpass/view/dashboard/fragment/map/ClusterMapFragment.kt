package com.android.mealpass.view.dashboard.fragment.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.android.mealpass.data.models.FoodData
import com.android.mealpass.view.units.isResturantOpen
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import mealpass.com.mealpass.R

class ClusterMapFragment(
    private val context: Context,
    map: GoogleMap?,
    mClusterManager: ClusterManager<FoodData.Body>?
) :
    DefaultClusterRenderer<FoodData.Body>(context, map, mClusterManager) {


    override fun onBeforeClusterItemRendered(
        item: FoodData.Body,
        markerOptions: MarkerOptions
    ) {
        markerOptions.snippet(null)

        lateinit var icon: Bitmap

        val condition = isResturantOpen(
                item.itemleft,
                item.is_open,
                item.opening_time,
                item.closing_time,
                item.before_pickup_time,
                item.shop_open_time,
                item.is_active
        )
        icon = when {
            condition -> BitmapFactory.decodeResource(context.resources, R.drawable.green_ywaste)
            else -> BitmapFactory.decodeResource(context.resources, R.drawable.red_ywaste)
        }
        getResizedBitmap(icon, 100, 100)?.let {
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(it))
        }
        super.onBeforeClusterItemRendered(item, markerOptions)
    }

    private fun getResizedBitmap(bm: Bitmap, widthSize: Int, heightSize: Int): Bitmap? {
        val width = bm.width
        val height = bm.height
        val scaleWidth = widthSize.toFloat() / width
        val scaleHeight = heightSize.toFloat() / height
        // CREATE A MATRIX FOR THE MANIPULATION
        val matrix = Matrix()
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight)
        // "RECREATE" THE NEW BITMAP
        val resizedBitmap = Bitmap.createBitmap(
            bm, 0, 0, width, height, matrix, false
        )
        bm.recycle()
        return resizedBitmap
    }

}