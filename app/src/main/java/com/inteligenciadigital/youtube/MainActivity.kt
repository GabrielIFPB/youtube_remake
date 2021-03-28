package com.inteligenciadigital.youtube

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.gson.GsonBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

	private lateinit var videoAdapter: VideoAdapter

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val videos = mutableListOf<Video>()

		this.videoAdapter = VideoAdapter(videos) { video ->
			println(video)
		}

		rv_main.layoutManager = LinearLayoutManager(this)
		rv_main.adapter = this.videoAdapter

		CoroutineScope(Dispatchers.IO).launch {
			val response = async { getVideo() }
			val listVideo = response.await()
			withContext(Dispatchers.Main) {
				listVideo?.let {
					videos.clear()
					videos.addAll(listVideo.data)
					videos.addAll(listVideo.data)
					videoAdapter.notifyDataSetChanged()
					motion_container.removeView(progress_recycle)
				}
			}

		}
//
//		CoroutineScope(Dispatcher.IO).launch {
//
//		}
	}

	private fun getVideo(): ListVideo? {
		val client = OkHttpClient.Builder().build()

		val request = Request.Builder()
			.get().url("https://tiagoaguiar.co/api/youtube-videos")
			.build()

		return try {
			val response = client.newCall(request).execute()
			if (response.isSuccessful) {
				GsonBuilder().create()
					.fromJson(response.body()?.string(), ListVideo::class.java)
			} else {
				null
			}
		} catch (e: Exception) {
			null
		}
	}
}