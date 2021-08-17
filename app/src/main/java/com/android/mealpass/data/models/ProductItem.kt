package com.android.mealpass.data.models

import com.android.mealpass.data.api.enums.ProductBuyEnum

data class ProductItem(
    var productBuyEnum: ProductBuyEnum,
    var saveReceiptRequestModel: SaveReceiptRequestModel? = null,
    var quantity: Int = 0
)