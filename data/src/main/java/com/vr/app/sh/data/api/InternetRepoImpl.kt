package com.vr.app.sh.data.api

import android.content.Context
import com.vr.app.sh.domain.model.AuthorizationEntity
import com.vr.app.sh.domain.model.Book
import com.vr.app.sh.domain.model.Question
import com.vr.app.sh.domain.model.Reg
import com.vr.app.sh.domain.model.Test
import com.vr.app.sh.domain.repository.internet.BookInternetRepo
import com.vr.app.sh.domain.repository.internet.DoorInSystemRepo
import com.vr.app.sh.domain.repository.internet.PhotoInternetRepo
import com.vr.app.sh.domain.repository.internet.QuestionsInternetRepo
import com.vr.app.sh.domain.repository.internet.ResultInternetRepo
import com.vr.app.sh.domain.repository.internet.TestsInternetRepo
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class InternetRepoImpl(val context: Context,private val networkService: NetworkService):
    DoorInSystemRepo,
    BookInternetRepo, TestsInternetRepo, QuestionsInternetRepo, ResultInternetRepo, PhotoInternetRepo {

    override suspend fun Authorization(requestBody: RequestBody): AuthorizationEntity {
        if(NetworkService.getInstance(context = context).loginInServ(requestBody).isSuccessful){
            return networkService.auth().body()!!
        }else{
            return AuthorizationEntity(false,"Ошибка подключения к серверу",null)
        }
    }

    override suspend fun Registration(requestBody: RequestBody,user_photo: MultipartBody.Part?): Reg {
        return networkService.registration(requestBody,user_photo).body()!!
    }

    override suspend fun getAllBookList(): Response<List<Book>> {
        return networkService.getAllBooks()
    }

    override fun getBookFile(id_book: Int): Call<ResponseBody> {
        return networkService.downloadFileWithFixedUrl(id_book)
    }

    override suspend fun getListTestsInSub(name_subject: String): Response<List<Test>> {
        return networkService.getAllTests(name_subject)
    }

    override suspend fun sendTest(requestBody: RequestBody): Response<ResponseBody> {
        return networkService.sendTest(requestBody)
    }

    override suspend fun getTestQuestions(num_class: Int): Response<List<Question>> {
        return networkService.getAllQuestions(num_class)
    }

    override suspend fun sendQuestions(requestBody: RequestBody): Response<ResponseBody> {
        return networkService.sendQuestions(requestBody)
    }

    override suspend fun sendResult(requestBody: RequestBody): Response<ResponseBody> {
        return networkService.sendResult(requestBody)
    }

    override fun downloadPhoto(userId: Int): Call<ResponseBody> {
        return networkService.downloadUserPhoto(userId)
    }

    override suspend fun verificationUser(userId: Int) {
        networkService.verificationUserInServer(userId)
    }
}