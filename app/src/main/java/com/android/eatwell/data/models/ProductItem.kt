package com.android.eatwell.data.models

import com.android.eatwell.data.api.enums.ProductBuyEnum

data class ProductItem(
    var productBuyEnum: ProductBuyEnum,
    var saveReceiptRequestModel: SaveReceiptRequestModel? = null,
    var quantity: Int = 0
)