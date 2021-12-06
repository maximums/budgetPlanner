package com.endava.budgetplanner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.endava.budgetplanner.authentication.ui.views.RegisterFragmentDirections
import com.endava.budgetplanner.databinding.ActivityMainBinding

private const val NAVIGATION_KEY = "navigation_key"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHost =
            supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        navController = navHost.navController
        val navigateToLogin = intent.getBooleanExtra(NAVIGATION_KEY, false)
        if (navigateToLogin && navController?.currentDestination?.id == R.id.registerFragment)
            navController?.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        setContentView(binding.root)

        navController?.addOnDestinationChangedListener { _, destination, _ ->
            binding.appBar.isVisible = when (destination.id) {
                R.id.registerFragment, R.id.loginFragment, R.id.dashboardFragment, R.id.welcomeFragment -> false
                else -> true
            }
        }
        navController?.let {
            binding.toolbar.setupWithNavController(it, AppBarConfiguration(it.graph))
        }
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp() =
        (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    override fun onDestroy() {
        super.onDestroy()
        navController = null
    }

    companion object {
        @JvmStatic
        fun newIntent(context: Context, navigateToLogin: Boolean) =
            Intent(context, MainActivity::class.java).apply {
                putExtra(NAVIGATION_KEY, navigateToLogin)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
    }
}