package com.inteligenciadigital.youtube

import android.content.Context
import android.net.Uri
import android.view.SurfaceHolder
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class YoutubePlayer(private val context: Context) : SurfaceHolder.Callback {

	private var mediaPlayer: SimpleExoPlayer? = null

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

			val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
				.createMediaSource(Uri.parse(url))

			it.prepare(videoSource)
			this.play()
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
}