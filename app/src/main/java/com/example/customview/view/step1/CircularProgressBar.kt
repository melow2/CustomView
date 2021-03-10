package com.example.customview.view.step1

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.customview.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * parent에서 addView를 실행했을 경우 #1~ 순서로 실행한다.
 *
 * @author 권혁신
 * @version 0.0.8
 * @since 2021-03-10 오후 12:17
 **/
class CircularProgressBar(
    context: Context,
    attributeSet: AttributeSet
) : View(context, attributeSet) {

    val compositeDisposable = CompositeDisposable()

    val lineColor = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBar)
        .getColor(R.styleable.ProgressBar_strokeColor, Color.RED)
    val maxValue = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBar)
        .getInt(R.styleable.ProgressBar_maxValue, 100)
    var curValue = context.obtainStyledAttributes(attributeSet, R.styleable.ProgressBar)
        .getInt(R.styleable.ProgressBar_curValue, 0)


    /**
     * #1
     *
     * 부모 뷰가 addView(childViwe)를 호출한 뒤, 자식뷰가 윈도우에 붙게된다.
     * 이떄 뷰 id를 통해 접근할 수 있다.
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-10 오후 12:40
     **/
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.tag(TAG).d("onAttachedToWindow()")
    }

    /**
     * #2
     *
     * 뷰의 크기를 측정하는 단계.
     * 레이아웃에 맞게 특정 크기를 가져야 한다.
     *
     * 1. 뷰가 원하는 사이즈 계산.
     * 2. MeasureSpec에 따라 mode를 가져온다.
     * 3. MeasureSpec에 따라 mode를 체크하여 뷰의 크기를 결정한다.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-10 오후 12:41
     **/
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Timber.tag(TAG)
            .d("onMeasure():: widthMeasureSpec: $widthMeasureSpec, heightMeasureSpec: $heightMeasureSpec")

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        Timber.tag(TAG).d("onMeasure():: widthMode: $widthMode, widthSize: $widthSize")
        Timber.tag(TAG).d("onMeasure():: heightMode: $heightMode, widthSize: $heightSize")

        val width = when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize // match_parent,fill_parent
            MeasureSpec.AT_MOST -> 400 // wrap_content
            else -> widthMeasureSpec
        }

        val height = when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            MeasureSpec.AT_MOST -> 400
            else -> heightMeasureSpec
        }

        setMeasuredDimension(width, height)
    }

    /**
     * 뷰의 위치와 크기를 할당한다.
     *
     * onMeasure을 통해 사이즈가 결정된 후에 onLayout이 불린다.
     * 부모뷰일때 주로 쓰이고, child 뷰를 붙일때 위츠를 정해주는데 사용한다.
     * 넘어오는 파라미터는 어플리케이션 전체를 기준으로 위치가 넘어온다.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-10 오후 12:43
     **/

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Timber.tag(TAG).d("onLayout()")
    }


    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
        Timber.tag(TAG).d("dispatchDraw()")
    }

    /**
     * 뷰를 실제로 그리는 단계.
     *
     * onDraw()는 많은 시간이 소요된다.
     * Scroll 또는 Swipe할 경우 다시 onLayout() -> onDraw()를 호출한다.
     * 따라서 함수 내에서 객채할당을 피하고 한번 할당한 객ㅊ채를 재사용하는것을 권고한다.
     *
     * - invalidate(): 단순히 뷰를 다시 그릴때 사용, text또는 color가 변경되거나 이때 onDraw()함수를 재호출.
     * - requestLayout(): onMeasure()부터 뷰를 다시그린다. 뷰의 사이즈가 변경될때 그것을 다시 측정해야하기에.
     *
     * @author 권혁신
     * @version 0.0.8
     * @since 2021-03-10 오후 12:44
     **/
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Timber.tag(TAG).d("onDraw()")

        val width = measuredWidth + 0.0f
        val height = measuredHeight + 0.0f

        val circle = Paint()
        circle.color = this.lineColor
        circle.strokeWidth = 10f
        circle.isAntiAlias = false
        circle.style = Paint.Style.STROKE

        canvas?.drawArc(
            RectF(
                10f, 10f, width - 10f, height - 10f
            ), -90f,
            (this.curValue + 0.0f) / (this.maxValue + 0.0f) * 360, false, circle
        )

        val textp = Paint()
        textp.color = Color.BLACK
        textp.textSize = 30f
        textp.textAlign = Paint.Align.CENTER

        if (System.currentTimeMillis() / 1000 % 2 == 0L) {
            canvas?.drawText(
                "${this.curValue} / ${this.maxValue}",
                (width / 2),
                (height / 2),
                textp
            )
        }

        Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                invalidate()
            }, {

            })
            .addTo(compositeDisposable)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Timber.tag(TAG).d("onDetachedFromWindow()")
        compositeDisposable.dispose()
    }

    companion object {
        val TAG = CircularProgressBar::class.simpleName
    }
}