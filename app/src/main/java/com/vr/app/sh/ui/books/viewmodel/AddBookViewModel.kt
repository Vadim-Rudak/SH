package com.vr.app.sh.ui.books.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vr.app.sh.R
import com.vr.app.sh.domain.UseCase.GetAllBookListInternet
import com.vr.app.sh.domain.UseCase.SaveBookListInBD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class AddBookViewModel(private val resources: Resources,val getAllBookListInternet: GetAllBookListInternet,val saveBookListInBD: SaveBookListInBD,private val internetConnection:Boolean): ViewModel() {

    var path_file: String? = null
    lateinit var file: File
    val vizibileProgressBar = MutableLiveData<Boolean>()
    val send = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun sendFile(name_book:String,num_class:Int){
        if(internetConnection){
            vizibileProgressBar.value = true
            CoroutineScope(Dispatchers.IO).launch {
                val builder: OkHttpClient.Builder = OkHttpClient.Builder()
                builder.connectTimeout(120, TimeUnit.SECONDS);
                builder.readTimeout(120, TimeUnit.SECONDS);
                builder.writeTimeout(120, TimeUnit.SECONDS);
                var client: OkHttpClient = builder.build()
                var formBody: RequestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("resbook", "efgyefg", RequestBody.create("text/plain".toMediaTypeOrNull(), file))
                    .addFormDataPart("namebook", name_book)
                    .addFormDataPart("numclass",num_class.toString())
                    .build()

                val request: Request = Request.Builder().url("${com.vr.app.sh.data.api.NetworkService.BASE_URL}/AddNewFileAndroid").post(formBody).build()
                val response = client.newCall(request).execute()
                Log.d("FFF", response.message)
                Log.d("FFF", response.isSuccessful.toString())

                getAllBookListInternet.execute().also {
                    if (it.success){
                        it.list?.let { it1 -> saveBookListInBD.execute(it1) }
                        withContext(Dispatchers.Main){
                            vizibileProgressBar.value = false
                            send.value = true
                        }
                    }
                }
            }
        }else{
            vizibileProgressBar.value = false
            errorMessage.value = resources.getString(R.string.alrNotInternetConnection)
        }
    }
}