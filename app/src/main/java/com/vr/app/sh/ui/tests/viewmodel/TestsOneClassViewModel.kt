package com.vr.app.sh.ui.tests.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vr.app.sh.R
import com.vr.app.sh.data.model.Test
import com.vr.app.sh.domain.UseCase.GetListQuestions
import com.vr.app.sh.domain.UseCase.GetListTestsInClass
import com.vr.app.sh.domain.UseCase.InternetConnection
import com.vr.app.sh.domain.UseCase.SaveQuestionsInBD
import com.vr.app.sh.ui.tests.adapter.BtnTestAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TestsOneClassViewModel(
    private val resources: Resources,
    getListTestsInClass: GetListTestsInClass,
    val getListQuestions: GetListQuestions,
    val saveQuestionsInBD: SaveQuestionsInBD,
    val internetConnection: InternetConnection,
    val num_class: Int
    ): ViewModel() {

    lateinit var name_test:String
    val adapter = BtnTestAdapter()
    val errorMessage = MutableLiveData<String>()
    val openTest = MutableLiveData<Boolean>()
    val listTests:LiveData<List<Test>> = getListTestsInClass.execute(num_class)
    var job: Job? = null

    fun saveQuestionsInDB(test_id:Int,nameTest:String){
        if (internetConnection.UseInternet()){
            job = CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main){
                    val listQuestions = getListQuestions.execute(test_id)
                    name_test = nameTest
                    if (listQuestions.isSuccessful){
                        saveQuestionsInBD.execute(listQuestions.body()!!)
                        if (listQuestions.body()!!.isNotEmpty()){
                            openTest.value = true
                        }else{
                            errorMessage.value = resources.getString(R.string.alrErrorNotQuestions)
                        }
                    }else{
                        errorMessage.value = resources.getString(R.string.alrErrorGetData)
                    }
                }
            }
        }else{
           errorMessage.value = resources.getString(R.string.alrNotInternetConnection)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}