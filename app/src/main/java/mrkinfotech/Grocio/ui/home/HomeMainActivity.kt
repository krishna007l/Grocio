package mrkinfotech.Grocio.ui.home

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivityMainBinding


class HomeMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportFragmentManager.executePendingTransactions()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home_content_main) as? NavHostFragment
                ?: run {
                    finish()
                    return
                }
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.HomeFragment,
                R.id.SearchFragment,
                R.id.CartFragment,
                R.id.ProfileFragment
            ),
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        configureBottomNavigation()
        configureBottomNavigationGlass()
    }

    fun bottomNavigationViewGone(){
        binding.bottomNavGlassCard.visibility = View.GONE
        binding.viewBottomNavGlow.visibility = View.GONE
    }

    fun bottomNavigationViewVisible() {
        binding.bottomNavGlassCard.visibility = View.VISIBLE
        binding.viewBottomNavGlow.visibility = View.VISIBLE
    }

    fun openCartScreen() {
        navigateToBottomDestination(R.id.CartFragment)
    }

    fun openHomeScreen() {
        navigateToBottomDestination(R.id.HomeFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (::navController.isInitialized) {
            navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        } else {
            super.onSupportNavigateUp()
        }
    }

    private fun configureBottomNavigationGlass() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            binding.viewBottomNavGlow.setRenderEffect(
                RenderEffect.createBlurEffect(56f, 56f, Shader.TileMode.CLAMP)
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavGlassCard) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val baseMargin = resources.getDimensionPixelSize(R.dimen.margin_12)
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
            val desiredBottomMargin = baseMargin + systemBars.bottom

            if (layoutParams.bottomMargin != desiredBottomMargin) {
                layoutParams.bottomMargin = desiredBottomMargin
                view.layoutParams = layoutParams
            }

            insets
        }
        ViewCompat.requestApplyInsets(binding.bottomNavGlassCard)
    }

    private fun configureBottomNavigation() {
        syncBottomNavigationSelection(navController.currentDestination?.id)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            navigateToBottomDestination(item.itemId)
        }

        binding.bottomNavigationView.setOnItemReselectedListener {
            // Keep the current tab visible without rebuilding it.
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            syncBottomNavigationSelection(destination.id)
        }
    }

    private fun navigateToBottomDestination(destinationId: Int): Boolean {
        val targetDestinationId = resolveBottomNavDestination(destinationId) ?: return false

        if (navController.currentDestination?.id != targetDestinationId) {
            val options = navOptions {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
            }

            navController.navigate(targetDestinationId, null, options)
        }

        syncBottomNavigationSelection(targetDestinationId)
        return true
    }

    private fun syncBottomNavigationSelection(destinationId: Int?) {
        val checkedDestination = resolveBottomNavDestination(destinationId) ?: return
        binding.bottomNavigationView.menu.findItem(checkedDestination)?.isChecked = true
    }

    private fun resolveBottomNavDestination(destinationId: Int?): Int? {
        return when (destinationId) {
            R.id.HomeFragment,
            R.id.SearchFragment,
            R.id.CartFragment,
            R.id.ProfileFragment -> destinationId
            R.id.AccountFragment,
            R.id.SettingsFragment,
            R.id.OrdersFragment -> R.id.ProfileFragment
            R.id.OrderSummaryFragment -> R.id.CartFragment
            R.id.ProductDetailFragment -> R.id.HomeFragment
            else -> null
        }
    }
}
