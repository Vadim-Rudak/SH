package com.vr.app.sh.ui.books.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.vr.app.sh.R
import com.vr.app.sh.data.repository.BookRepoImpl
import com.vr.app.sh.data.repository.UserRepoImpl
import com.vr.app.sh.domain.UseCase.GetUserBD
import com.vr.app.sh.domain.repository.UserRepo
import com.vr.app.sh.ui.books.adapter.ViewPager2Adapter

class Books : AppCompatActivity() {

    var num_class:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_books)

        val viewPager = findViewById<ViewPager2>(R.id.pager_windows_book)
        viewPager.adapter = ViewPager2Adapter(this)

        val tabLayout = findViewById<TabLayout>(R.id.tabs_windows_test1)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = "${(position + 1)} КЛАСС"
        }.attach()

        val userRepo:UserRepo = UserRepoImpl(this)
        val getUserBD = GetUserBD(userRepo)

        val fab = findViewById<FloatingActionButton>(R.id.FabBook)
        if (getUserBD.execute().role.equals("ADMIN")){
            fab.visibility = View.VISIBLE
        }else{
            fab.visibility = View.GONE
        }
        fab.setOnClickListener {
            var intent = Intent(this,AddBook::class.java)
            intent.putExtra("num_class", tabLayout.selectedTabPosition + 1)
            startActivity(intent)
        }

        isStoragePermissionGranted()
    }

    fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                false
            }
        } else {
            true
        }
    }
}

