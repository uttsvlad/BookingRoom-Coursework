package com.cdr.courseworkdatabase.main

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
import com.cdr.courseworkdatabase.R
import com.cdr.courseworkdatabase.authentication.AuthenticationActivity
import com.cdr.courseworkdatabase.databinding.ActivityMainBinding
import com.cdr.courseworkdatabase.main.contract.HasCustomIconMain
import com.cdr.courseworkdatabase.main.contract.HasCustomTitleMain
import com.cdr.courseworkdatabase.main.contract.NavigatorMain
import com.cdr.courseworkdatabase.main.home.admin.*
import com.cdr.courseworkdatabase.main.home.client.*
import com.cdr.courseworkdatabase.main.home.client.AllBidsAdminObject
import com.cdr.courseworkdatabase.main.home.superAdmin.AddAdminFragment
import com.cdr.courseworkdatabase.main.home.superAdmin.HomeSuperAdminFragment
import com.cdr.courseworkdatabase.main.home.superAdmin.StatisticFragment
import com.cdr.courseworkdatabase.main.profile.ProfileFragment
import com.cdr.courseworkdatabase.model.storage.UserStorageWorker
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), NavigatorMain {

    private lateinit var binding: ActivityMainBinding // Main-Binding
    private lateinit var bottomNavigationView: BottomNavigationView // Bottom Navigation
    private val currentFragment: Fragment
        get() = supportFragmentManager.findFragmentById(R.id.fragmentContainer)!!
    private val fragmentLifecycleCallbacks = object : FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(
            fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?
        ) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            renderToolbar()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentLifecycleCallbacks, false)
        bottomNavigationView = binding.bottomNavigation.also {
            it.selectedItemId = R.id.homeButton
            it.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.homeButton -> {
                        showHomeScreen()
                        true
                    }
                    R.id.profileButton -> {
                        showProfileScreen()
                        true
                    }
                    else -> false
                }
            }
        } // Инициализация BottomNavigation

        if (savedInstanceState == null) showHomeScreen()
    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentLifecycleCallbacks)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun goBack() = onBackPressed()
    override fun showAddAdminScreen() = launchFragmentToBackStack(AddAdminFragment())
    override fun showAllRoomsScreen() = launchFragmentToBackStack(AllRoomsFragment())
    override fun showInfoRoomScreen(roomId: Long) =
        launchFragmentToBackStack(RoomInfoFragment.newInstance(roomId))

    override fun showTreatmentBidScreen(bookedRoom: BookRoom) =
        launchFragment(TreatmentBidFragment.newInstance(bookedRoom))

    override fun showTreatmentBidScreen(bidInfo: AllBidsAdminObject) =
        launchFragmentToBackStack(BidInfoFragment.newInstance(bidInfo))

    override fun showTopServiceScreen() = launchFragmentToBackStack(TopServiceFragment())
    override fun showStatisticScreen() = launchFragmentToBackStack(StatisticFragment())
    override fun showAllBidsScreen() = launchFragmentToBackStack(AllBitsFragment())
    override fun showComplaintScreen() = launchFragmentToBackStack(ComplaintFragment())
    override fun showAllClientsScreen() = launchFragmentToBackStack(AllClientsFragment())
    override fun showAllBidsAdminScreen() = launchFragmentToBackStack(AllBidsAdminFragment())
    override fun showBidInfoAdminScreen(bidInfo: AllBidsAdmin) =
        launchFragmentToBackStack(BidInfoAdminFragment.newInstance(bidInfo))

    override fun showBidDamageInfoAdminScreen(bidInfo: AllBidsAdmin) =
        launchFragmentToBackStack(BidIDamageInfoAdminFragment.newInstance(bidInfo))

    override fun showProfileScreen() = launchFragment(ProfileFragment())
    override fun showHomeScreen() {
        supportFragmentManager.popBackStack()
        val user = UserStorageWorker().createUserFromStorage(this) // Получение пользователя
        val homeFragment =
            when (user.role) {
                "Администратор" -> HomeAdminFragment()
                "Клиент" -> HomeClientFragment()
                "Суперадмин" -> HomeSuperAdminFragment()
                else -> HomeAdminFragment()
            }
        launchFragment(homeFragment)
    }

    override fun signOut() {
        supportFragmentManager.popBackStack()
        val intent = Intent(this, AuthenticationActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finishAfterTransition()
    }

    // Запуск нового fragment:
    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    // Запуск fragment в back stack:
    private fun launchFragmentToBackStack(fragment: Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragmentContainer, fragment).commit()
    }

    // Отрисовываем объекты в Toolbar:
    private fun renderToolbar() {
        val fragment = currentFragment

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.titleMarginStart = 0
        } else supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (fragment is HasCustomTitleMain) supportActionBar?.title =
            getString(fragment.getRestTitle())
        else supportActionBar?.title = getString(R.string.app_name)

        if (fragment is HasCustomIconMain) {
            binding.toolbar.titleMarginStart = 75
            supportActionBar?.setIcon(fragment.getResIcon())
        } else supportActionBar?.setIcon(ColorDrawable(resources.getColor(R.color.transparent)))

    }
}