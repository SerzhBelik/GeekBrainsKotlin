package com.example.myapplication

import timber.log.Timber

class Production: Timber.DebugTree() {

    override fun log(priority: Int, message: String?, vararg args: Any?) {
        super.log(priority, message, *args)

    }


}