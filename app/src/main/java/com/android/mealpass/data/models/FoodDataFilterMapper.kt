package com.android.mealpass.data.models


class FoodDataFilterMapper {
    object Mapper {
        fun from(
            productList: ProductTypeResponse,
            selectedList: List<String>
        ): ProductTypeResponse {
            productList.body?.map { list ->
                selectedList.map {
                    if (list.id.equals(it, ignoreCase = true)) list.isItemSelected = true
                }
            }
            return productList
        }
    }
}