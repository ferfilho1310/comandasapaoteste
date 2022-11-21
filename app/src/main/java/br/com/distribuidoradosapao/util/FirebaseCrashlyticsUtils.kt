package br.com.distribuidoradosapao.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseCrashlyticsUtils {

    fun log(messageError: String){
        FirebaseCrashlytics.getInstance().recordException(Throwable(messageError))
    }
}