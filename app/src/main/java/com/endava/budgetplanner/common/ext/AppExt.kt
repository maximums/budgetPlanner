package com.endava.budgetplanner.common.ext

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.endava.budgetplanner.common.network.NetworkMonitor
import com.endava.budgetplanner.common.network.Resource

fun hideKeyboardFrom(view: View) {
    val imm = view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.RESULT_UNCHANGED_SHOWN)
}

fun <T> lazyUnsafe(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE) { initializer() }

suspend fun <T> safeNetworkRequest(
    networkMonitor: NetworkMonitor,
    networkRequest: suspend () -> Resource<T>
): Resource<T> {
    return if (networkMonitor.hasInternetConnection()) {
        networkRequest()
    } else Resource.ConnectionError
}