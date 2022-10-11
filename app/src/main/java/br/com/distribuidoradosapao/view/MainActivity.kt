package br.com.distribuidoradosapao.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import br.com.distribuidoradosapao.R
import br.com.distribuidoradosapao.databinding.ActivityMainBinding
import br.com.distribuidoradosapao.view.client.ClientFragment
import br.com.distribuidoradosapao.view.client.InsertClientBottomSheet

class MainActivity : AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    View.OnClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        initializeFragment()
        listener()
        setupDrawerLayout()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_slideshow -> navigateFragment(SlideshowFragment())
            R.id.nav_client -> navigateFragment(ClientFragment())
            R.id.nav_gallery -> navigateFragment(GalleryFragment())
        }

        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }

    private fun navigateFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, fragment)
            .commit()
    }

    private fun initializeFragment() {
        navigateFragment(ClientFragment())
    }

    private fun listener() {
        binding.let {
            it.appBarMain.fab.setOnClickListener(this)
            it.navView.setNavigationItemSelectedListener(this)
        }
    }

    private fun setupDrawerLayout() {
        drawerLayout = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMain.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.fab -> {
                val bottomSheet = InsertClientBottomSheet()
                bottomSheet.show(supportFragmentManager, "TAG")
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.getOnBackPressedDispatcher()
            finish()
        }
    }
}