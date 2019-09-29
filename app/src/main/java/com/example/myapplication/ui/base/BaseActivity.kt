package com.example.myapplication.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.os.PersistableBundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.data.errors.NoAuthException
import com.firebase.ui.auth.AuthUI
import com.github.ajalt.timberkt.i
import java.util.*

abstract class BaseActivity<T, S: BaseViewState<T>> : AppCompatActivity(){
    companion object {
        private const val RC_SIGN_IN = 42424
    }
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {
            setContentView(it)
        }

        viewModel.getViewState().observe(this, Observer<S> {
            if (it == null) return@Observer
            if (it.error != null){
                renderError(it.error!!)
                return@Observer
            }

            renderData(it.data)
        })
    }

    abstract fun renderData(data: T)

    protected fun renderError(error: Throwable){
        when(error){
            is NoAuthException -> startLogin()
            else ->  error.message?.let { showError(it) }
        }
    }
    protected fun showError(message:String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startLogin(){
        val providers = listOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setLogo(R.drawable.android_robot)
                        .setTheme(R.style.LoginStyle)
                        .setAvailableProviders(providers)
                        .build()
                , RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}