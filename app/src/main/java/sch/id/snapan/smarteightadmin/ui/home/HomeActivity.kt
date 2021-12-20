package sch.id.snapan.smarteightadmin.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import sch.id.snapan.smarteightadmin.R
import sch.id.snapan.smarteightadmin.databinding.ActivityHomeBinding
import sch.id.snapan.smarteightadmin.ui.auth.LoginActivity

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment)!!
        val navController = navHostFragment.findNavController()
        val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = setOf(
            R.id.announcementFragment,
            R.id.attendanceFragment,
            R.id.userFragment
        ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavMenu.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.home_nav_host_fragment)
        return navController.navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                Firebase.auth.signOut()
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}