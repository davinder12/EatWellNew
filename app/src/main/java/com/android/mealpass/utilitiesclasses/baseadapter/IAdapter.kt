package com.android.mealpass.utilitiesclasses.baseadapter

import io.reactivex.Observable


interface IAdapter<T> {
    val clicks: Observable<T>

}