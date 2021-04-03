package com.inteligenciadigital.youtube

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class YoutubePlayer(private val context: Context) : SurfaceHolder.Callback {

	private var mediaPlayer: SimpleExoPlayer? = null
	var youtubePlayerListener: YoutubePlayerListener? = null
	private lateinit var runnable: Runnable
	private var handle = Handler(Looper.getMainLooper())

	override fun surfaceCreated(holder: SurfaceHolder) {
		if (this.mediaPlayer == null) {
			this.mediaPlayer = SimpleExoPlayer.Builder(context).build()
			this.mediaPlayer?.setVideoSurfaceHolder(holder)
		}
	}

	override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

	}

	override fun surfaceDestroyed(holder: SurfaceHolder) {
		this.mediaPlayer?.release()
	}

	fun setUrl(url: String) {
		this.mediaPlayer?.let {
			val dataSourceFactory = DefaultDataSourceFactory(
				context,
				Util.getUserAgent(context, "youtube")
			)

			var mediaItem = MediaItem.fromUri(url)
			val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
				.createMediaSource(mediaItem)
//				.createMediaSource(Uri.parse(url))

			it.setMediaItem(mediaItem)
//			it.prepare(videoSource)
			it.prepare()
			it.addListener(object : Player.EventListener {
				override fun onIsPlayingChanged(isPlaying: Boolean) {
					if (isPlaying)
						trackTime()
				}
			})
			this.play()
		}
	}

	private fun trackTime() {
		this.mediaPlayer?.let {
			this.youtubePlayerListener?.onTrackTime(it.currentPosition * 100 / it.duration)
			if (it.isPlaying) {
				this.runnable = Runnable { trackTime() }
				this.handle.postDelayed(this.runnable, 1000)
			}
		}
	}

	private fun play() {
		this.mediaPlayer?.playWhenReady = true
	}

	fun pause() {
		this.mediaPlayer?.playWhenReady = false
	}

	fun release() {
		this.mediaPlayer?.release()
	}

	interface YoutubePlayerListener {
		fun onPrepared(duration: Int)
		fun onTrackTime(currentPosition: Long)
	}
}