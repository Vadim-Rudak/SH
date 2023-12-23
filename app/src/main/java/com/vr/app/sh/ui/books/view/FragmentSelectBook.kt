package com.vr.app.sh.ui.books.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import com.vr.app.sh.R
import com.vr.app.sh.app.App
import com.vr.app.sh.ui.base.BooksViewModelFactory
import com.vr.app.sh.ui.books.adapter.BooksItemDecoration
import com.vr.app.sh.ui.books.adapter.RecyclerViewAdapter
import com.vr.app.sh.ui.books.viewmodel.SubjectsViewModel
import com.vr.app.sh.ui.other.UseAlert
import kotlinx.coroutines.*
import java.io.File

class FragmentSelectBook() : Fragment() {

    @javax.inject.Inject
    lateinit var factory: BooksViewModelFactory

    var num_class:Int = 0
    lateinit var viewModel: SubjectsViewModel


    constructor(num_class:Int):this(){
        this.num_class = num_class
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as App).appComponent.injectFragmentSubjectsClass(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_subjects_class, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.subjects_recycler)
        recyclerView.layoutManager = GridLayoutManager(activity, 2)
        recyclerView.addItemDecoration(BooksItemDecoration(requireActivity()))

        Log.d("FFF","акт")
        val infoDowload = view.findViewById<TextView>(R.id.textInfoDowload)
        val progressbar = view.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        progressbar.apply {
            progressMax = 100f
            setProgressWithAnimation(0f,1000)
            progressBarWidth = 14f
            backgroundProgressBarWidth = 2f
            backgroundProgressBarColor = Color.parseColor("#969696")
            progressBarColorStart = Color.parseColor("#6767bb")
            progressBarColorEnd = Color.parseColor("#4c7daf")
            roundBorder = true
        }
        val textProgress = view.findViewById<TextView>(R.id.textProgress)

        factory.addNumClass(num_class)

        viewModel = ViewModelProvider(this, factory)
            .get(SubjectsViewModel::class.java)

        viewModel.progress.observe(viewLifecycleOwner){
            textProgress.text = "${it}%"
            progressbar.progress = it.toFloat()
            if (it.toInt()==100){
                viewModel.download.value = false
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner){
            UseAlert.errorMessage(it,requireContext())
        }

        viewModel.download.observe(viewLifecycleOwner){
            if (it){
                recyclerView.visibility = View.GONE
                textProgress.visibility = View.VISIBLE
                progressbar.visibility = View.VISIBLE
                textProgress.text = "0%"
                progressbar.progress = 0f
                activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.Main){
                        progressbar.visibility = View.GONE
                        textProgress.visibility = View.GONE
                        infoDowload.visibility = View.VISIBLE
                        if (viewModel.saveFileInMemory){
                            infoDowload.text = "Книга успешно загружена"
                        }else{
                            infoDowload.text = "Ошибка загрузки книги"
                        }
                        delay(1500)
                        infoDowload.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                    }
                }
            }
        }

        viewModel.listBooks.observe(viewLifecycleOwner){
            viewModel.adapter.setBook(it)
            Log.d("FFF", "size ${it.size}")
        }

        recyclerView.adapter = viewModel.adapter
        viewModel.adapter.setListener(object : RecyclerViewAdapter.Listener{
            override fun Clicked(pos_book: Int, name_book: String, id_book: Int) {
                val path = Environment.getExternalStorageDirectory().path + "/SchoolProg/Books/Class_" + num_class + "/" + name_book + ".pdf"
                if (File(path).exists()){
                    Log.d("FFF","Открыта книга")
                    val intent = Intent(activity, ReadPDF::class.java)
                    intent.putExtra("path",path)
                    intent.putExtra("name_book", name_book)
                    startActivity(intent)
                }else{
                    viewModel.saveFileDialog(path,id_book,requireContext())
                }
            }

            override fun LongClicked(pos_book: Int, name_book: String, id_book: Int) {
                viewModel.editMenu(context!!)
            }
        })
        return view
    }
}