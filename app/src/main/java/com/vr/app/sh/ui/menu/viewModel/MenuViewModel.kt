package com.vr.app.sh.ui.menu.viewModel

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vr.app.sh.domain.UseCase.GetAllBookListInternet
import com.vr.app.sh.domain.UseCase.InternetConnection
import com.vr.app.sh.domain.UseCase.SaveBookListInBD
import kotlinx.coroutines.*
import java.io.File

class MenuViewModel(val getAllBookListInternet: GetAllBookListInternet,val saveBookListInBD: SaveBookListInBD,val internetConnection: InternetConnection): ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val statusListBook = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    var job: Job? = null

    init {
        createDirs()
    }

    fun createDirs(){
        val path = "${Environment.getExternalStorageDirectory().path}/SchoolProg/Books/Class_"
        var fileDir:File
        for (i in 1..11){
            fileDir = File(path+i)
            if (!fileDir.exists()){
                fileDir.mkdirs()
            }
        }
    }

    fun getAllBooks(){
        if (internetConnection.UseInternet()){
            job = CoroutineScope(Dispatchers.IO).launch {
                loading.postValue(true)
                statusListBook.postValue(false)
                withContext(Dispatchers.Main){
                    val ListBooks = getAllBookListInternet.execute()
                    if (ListBooks.isSuccessful){
                        loading.postValue(false)
                        statusListBook.postValue(true)
                        saveBookListInBD.execute(ListBooks.body()!!)
                    }else{
                        loading.postValue(false)
                        errorMessage.value = "Ошибка получения данных"
                    }
                }
            }
        }else{
            errorMessage.value = "Отсутствует подключение к интернету"
        }
    }

    fun errorMessage(textMessage:String,context: Context){
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Ошибка")
        alertDialog.setMessage(textMessage)
        alertDialog.setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        alertDialog.show()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}