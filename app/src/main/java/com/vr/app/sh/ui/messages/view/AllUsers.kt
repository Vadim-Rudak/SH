package com.vr.app.sh.ui.messages.view

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vr.app.sh.R
import com.vr.app.sh.app.App
import com.vr.app.sh.ui.base.AllUsersViewModelFactory
import com.vr.app.sh.ui.messages.adapter.UserItemDecoration
import com.vr.app.sh.ui.messages.viewmodel.AllUsersViewModel
import javax.inject.Inject

class AllUsers : AppCompatActivity() {

    @Inject
    lateinit var factory: AllUsersViewModelFactory

    lateinit var viewModel:AllUsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_users)

        (applicationContext as App).appComponent.injectAllUsers(this)

        viewModel = ViewModelProvider(this, factory)
            .get(AllUsersViewModel::class.java)

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish()
        }
        val viewTitle = findViewById<TextView>(R.id.viewTitle)
        viewTitle.text = resources.getString(R.string.allUsersTitle)

        val recyclerView = findViewById<RecyclerView>(R.id.AllUsersList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AllUsers)
            addItemDecoration(UserItemDecoration(this@AllUsers))
            adapter = viewModel.adapter
        }

        viewModel.listUsers.observe(this){
            viewModel.adapter.setUsers(it)
        }

    }
}