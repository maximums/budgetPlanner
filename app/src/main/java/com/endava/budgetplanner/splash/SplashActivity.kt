package com.endava.budgetplanner.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.endava.budgetplanner.MainActivity
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.ext.provideAppComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SplashActivity"

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: SplashViewModel.Factory
    private val viewModel: SplashViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        provideAppComponent().splashComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            viewModel.splashState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { viewState ->
                    when (viewState) {
                        SplashState.Loading -> {
                        }
                        is SplashState.LoadComplete -> {
                            startActivity(
                                MainActivity.newIntent(
                                    this@SplashActivity,
                                    viewState.navigateToLogin
                                )
                            )
                            finish()
                        }
                    }
                }
        }
    }
}

