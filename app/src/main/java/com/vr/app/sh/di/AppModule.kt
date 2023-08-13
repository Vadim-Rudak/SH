package com.vr.app.sh.di

import android.content.Context
import com.vr.app.sh.domain.UseCase.*
import com.vr.app.sh.ui.base.*
import dagger.Module
import dagger.Provides

@Module
class AppModule(val context: Context) {

    @Provides
    fun provideContext():Context{
        return context
    }

    @Provides
    fun provideAddBookViewModelFactory(
        context: Context,
        getAllBookListInternet: GetAllBookListInternet,
        saveBookListInBD: SaveBookListInBD,
        internetConnection: InternetConnection
    ):AddBookViewModelFactory{
        return AddBookViewModelFactory(context,getAllBookListInternet, saveBookListInBD, internetConnection)
    }

    @Provides
    fun provideAddTestViewModelFactory(
        context: Context,
        getInfoTests: GetListTestsInternet,
        saveTestInBD: SaveTestsInBD,
        sendTest: SendTestInfo,
        sendQuestions: SendQuestions,
        internetConnection: InternetConnection
    ):AddTestViewModelFactory{
        return AddTestViewModelFactory(context,getInfoTests,saveTestInBD, sendTest, sendQuestions, internetConnection)
    }

    @Provides
    fun provideAllSubjectsViewModelFactory(
        context: Context,
        getListTestsInternet: GetListTestsInternet,
        saveListTestsInBD: SaveTestsInBD,
        internetConnection: InternetConnection
    ): AllSubjectsViewModelFactory {
        return AllSubjectsViewModelFactory(context,getListTestsInternet, saveListTestsInBD, internetConnection)
    }

    @Provides
    fun provideAuthorizationViewModelFactory(
        context: Context,
        authorization: Authorization,
        setUserInBD: SetUserInBD,
        internetConnection: InternetConnection
    ): AuthorizationViewModelFactory {
        return AuthorizationViewModelFactory(context,authorization, setUserInBD, internetConnection)
    }

    @Provides
    fun provideBooksViewModelFactory(
        context: Context,
        getListBookInClass: GetListBookInClass,
        getBookFile: GetBookFile,
        internetConnection: InternetConnection
    ): BooksViewModelFactory {
        return BooksViewModelFactory(context,getListBookInClass, getBookFile, internetConnection)
    }

    @Provides
    fun provideMenuViewModelFactory(
        context: Context,
        getListBookInternet: GetAllBookListInternet,
        saveListBookInBD: SaveBookListInBD,
        internetConnection: InternetConnection
    ):MenuViewModelFactory{
        return MenuViewModelFactory(context,getListBookInternet, saveListBookInBD, internetConnection)
    }

    @Provides
    fun provideRegViewModelFactory(
        context: Context,
        registration: Registration,
        internetConnection: InternetConnection
    ):RegViewModelFactory{
        return RegViewModelFactory(context,registration, internetConnection)
    }

    @Provides
    fun provideResultViewModelFactory(
        context: Context,
        sendResult: SendResult,
        getUser: GetUserBD,
        internetConnection: InternetConnection
    ):ResultViewModelFactory{
        return ResultViewModelFactory(context,sendResult, getUser, internetConnection)
    }

    @Provides
    fun provideTestsOneClassViewModelFactory(
        context: Context,
        getListTestsInClass: GetListTestsInClass,
        getListQuestions: GetListQuestions,
        saveQuestionsInBD: SaveQuestionsInBD,
        internetConnection: InternetConnection,
    ):TestsOneClassViewModelFactory{
        return TestsOneClassViewModelFactory(context,getListTestsInClass, getListQuestions, saveQuestionsInBD, internetConnection)
    }

    @Provides
    fun provideOpenTestViewModelFactory(
        getListQuestionsBD: GetListQuestionsBD
    ):OpenTestViewModelFactory{
        return OpenTestViewModelFactory(getListQuestionsBD)
    }

    @Provides
    fun provideDayViewModelFactory(
        getLessonsInDay: GetLessonsInDay
    ):DayViewModelFactory{
        return DayViewModelFactory(getLessonsInDay)
    }

    @Provides
    fun provideTimeTableViewModelFactory(
        saveLessonInBD: SaveLessonInBD
    ):TimeTableViewModelFactory{
        return TimeTableViewModelFactory(saveLessonInBD)
    }
}