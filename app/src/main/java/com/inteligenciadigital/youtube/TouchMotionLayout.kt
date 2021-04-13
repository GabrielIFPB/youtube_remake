package com.inteligenciadigital.youtube

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import kotlinx.android.synthetic.main.video_detail.view.*
import kotlin.math.abs

class TouchMotionLayout(context: Context, attributeSet: AttributeSet) : MotionLayout(context, attributeSet) {

	private val iconArrowDown: ImageView by lazy {
		findViewById(R.id.hide_player)
	}

	private val imageBase: ImageView by lazy {
		findViewById(R.id.video_player)
	}

	private val playButton: ImageView by lazy {
		findViewById(R.id.play_button)
	}

	private val seekBar: SeekBar by lazy {
		findViewById(R.id.seek_bar)
	}

	private var startX: Float? = null
	private var startY: Float? = null
	private var isPause = false

	private lateinit var animationFadeIn: AnimatorSet
	private lateinit var animationFadeOu: AnimatorSet

	override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
		val isInTarget = this.touchEventInsideTargetView(this.imageBase, event!!)
		val isInProgress = (this.progress > 0.0f && this.progress < 1.0f)

		return if (isInProgress || isInTarget)
			super.onInterceptHoverEvent(event)
		else
			false
	}

	private fun touchEventInsideTargetView(v: View, ev: MotionEvent) : Boolean {
		if (ev.x > v.left && ev.x < v.right)
			if (ev.y > v.top && ev.y < v.bottom)
				return true

		return false
	}

	override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
		when (ev.action) {
			MotionEvent.ACTION_DOWN -> {
				this.startX = ev.x
				this.startY = ev.y
			}
			MotionEvent.ACTION_UP -> {
				var endX = ev.x
				var endY = ev.y

				if (this.isAClick(this.startX!!, endX, this.startY!!, endY)) {
					if (this.touchEventInsideTargetView(this.imageBase, ev)) {
						if (this.doClick(this.imageBase)) {
							return true
						}
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev)
	}

	private fun isAClick(startX: Float, endX: Float, startY: Float, endY: Float) : Boolean {
		val differenceX = abs(startX - endX)
		val differenceY = abs(startY - endY)

		return !(differenceX > 200 || differenceY > 200)
	}

	private fun doClick(view: View): Boolean {
		var isClickHandle = false

		if (this.progress < 0.5f) {
			isClickHandle = true

			when (view) {
				imageBase -> {
					if (this.isPause) {

					} else {
						animateFade {
							this.animationFadeOu.startDelay = 1000
							this.animationFadeOu.start()
						}
					}
				}
			}
		}

		return isClickHandle
	}

	private fun animateFade(onAnimationEndOn: () -> Unit) {
		this.animationFadeOu = AnimatorSet()
		this.animationFadeIn = AnimatorSet()

		this.fade(this.animationFadeIn, arrayOf(
			play_button,
			hide_player,
			next_button,
			previous_button,
			playlist_player,
			full_player,
			share_player,
			more_player,
			current_time,
			duration_time
		), true)

		this.animationFadeIn.play(
			ObjectAnimator.ofFloat(view_Frame, View.ALPHA, 0f, .5f)
		)

		val valueFadeIn = ValueAnimator.ofInt(0, 255)
			.apply {
				addUpdateListener {
					seek_bar.thumb.mutate().alpha = it.animatedValue as Int
				}
				duration = 200
			}

		this.animationFadeIn.play(valueFadeIn)

		this.fade(this.animationFadeOu, arrayOf(
			play_button,
			hide_player,
			next_button,
			previous_button,
			playlist_player,
			full_player,
			share_player,
			more_player,
			current_time,
			duration_time
		), true)

		val valueFadeOu = ValueAnimator.ofInt(255, 0)
			.apply {
				addUpdateListener {
					seek_bar.thumb.mutate().alpha = it.animatedValue as Int
				}
				duration = 200
			}

		this.animationFadeOu.play(valueFadeOu)

		this.animationFadeIn.addListener(object : Animator.AnimatorListener {
			override fun onAnimationStart(animation: Animator?) {

			}

			override fun onAnimationEnd(animation: Animator?) {
				onAnimationEndOn.invoke()
			}

			override fun onAnimationCancel(animation: Animator?) {

			}

			override fun onAnimationRepeat(animation: Animator?) {

			}

		})
		this.animationFadeIn.start()
	}

	private fun fade(animatoSet: AnimatorSet, view: Array<View>, toZero: Boolean) {
		view.forEach {
			animatoSet.play(
				ObjectAnimator.ofFloat(
					it, View.ALPHA,
					if (toZero) 0f else 1f,
					if (toZero) 1f else 0f
				)
			)
		}
	}
}