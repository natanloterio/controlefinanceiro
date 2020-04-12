package me.loterio.controlefinanceiro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.main_activity.*
import me.loterio.controlefinanceiro.ui.costumer.detail.CostumerDertailFragment
import me.loterio.controlefinanceiro.ui.costumer.list.CostumerListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var controller: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)


        // Handle nav drawer item clicks
//        navigationView.setNavigationItemSelectedListener { menuItem ->
//            menuItem.isChecked = true
//            drawerLayout.closeDrawers()
//            true
//        }

        // Tie nav graph to items in nav drawer
        setupWithNavController(navigationView, navController)
    }
}
