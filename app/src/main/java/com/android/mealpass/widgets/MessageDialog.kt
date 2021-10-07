package com.android.mealpass.widgets

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AlertDialog
import mealpass.com.mealpass.R
import java.util.*


fun dateDialog(context: Context, response: (String) -> Unit): DatePickerDialog {
    val c: Calendar = Calendar.getInstance()
    val mYear = c.get(Calendar.YEAR)
    val mMonth = c.get(Calendar.MONTH)
    val mDay = c.get(Calendar.DAY_OF_MONTH)
    val datepicker = DatePickerDialog(
        context,
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            val date = "" + (monthOfYear + 1) + "." + dayOfMonth.toString() + "." + year
            response(date)
        },
        mYear,
        mMonth,
        mDay
    )
    datepicker.datePicker.minDate = System.currentTimeMillis() - 1000

    return datepicker
}


fun Context.messageDialog(message: String, isCancelable: Boolean = true, callBack: (() -> Unit)? = null) {
    val alertDialogBuilder = AlertDialog.Builder(this).also {
        it.setMessage(message)
        it.setTitle(R.string.app_name)
        it.setCancelable(isCancelable)
    }
    alertDialogBuilder.setPositiveButton(getString(R.string.OK)) { dialog, which ->
        dialog.dismiss()
        callBack?.invoke()
    }
    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}


fun Context.campignDialog(message: String, successResponse: () -> Unit) {
    val alertDialogBuilder = AlertDialog.Builder(this).also {
        it.setTitle(getString(R.string.AcceptTitle))
        it.setMessage(message)
    }
    alertDialogBuilder.setPositiveButton(getString(R.string.Continue)) { dialog, _ ->
        dialog.dismiss()
        successResponse()
    }
    alertDialogBuilder.setNegativeButton(getString(R.string.Cancel)) { dialog, which ->
        dialog.dismiss()
    }

    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}

fun Context.alertDialog(
        title: String,
        message: String,
        postitiveBtnMsg: String,
        negativebtnMsg: String,
        successResponse: (() -> Unit)? = null,
        errorResponse: (() -> Unit)? = null,

        ) {
    val alertDialogBuilder = AlertDialog.Builder(this).also {
        it.setTitle(title)
        it.setMessage(message)

    }
    alertDialogBuilder.setPositiveButton(postitiveBtnMsg) { dialog, _ ->
        dialog.dismiss()
        successResponse?.invoke()
    }
    alertDialogBuilder.setNegativeButton(negativebtnMsg) { dialog, which ->
        dialog.dismiss()
        errorResponse?.invoke()
    }
    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}












