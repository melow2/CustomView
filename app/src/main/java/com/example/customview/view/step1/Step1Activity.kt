package com.example.customview.view.step1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customview.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import kotlinx.android.synthetic.main.activity_step1.*
import java.util.*
import java.util.concurrent.TimeUnit


class Step1Activity : AppCompatActivity() {

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step1)

        Observable.interval(100,TimeUnit.MILLISECONDS)
            .filter { it<=100 }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                circular_progress_bar.curValue = it.toInt()
            }.addTo(compositeDisposable)
    }
}
