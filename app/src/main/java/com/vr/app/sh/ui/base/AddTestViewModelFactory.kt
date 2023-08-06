package com.vr.app.sh.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vr.app.sh.domain.UseCase.*
import com.vr.app.sh.ui.tests.viewmodel.AddTestViewModel

class AddTestViewModelFactory(
    val context: Context,
    val getInfoTests: GetListTestsInternet,
    val saveTestInBD: SaveTestsInBD,
    val sendTest: SendTestInfo,
    val sendQuestions: SendQuestions,
    val internetConnection: InternetConnection
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddTestViewModel::class.java)) {
            AddTestViewModel(context.resources,getInfoTests,saveTestInBD,sendTest,sendQuestions,internetConnection) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}