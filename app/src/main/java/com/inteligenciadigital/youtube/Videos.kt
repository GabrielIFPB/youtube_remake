package com.inteligenciadigital.youtube

import java.text.SimpleDateFormat
import java.util.*

data class Video(
		val id: String,
		val title: String,
		val thumbnailUrl: String,
		val publishedAt: Date,
		val viewsCount: Long,
		val viewsCountLabel: String,
		val duration: Int,
		val videoUrl: String,
		val publisher: Publisher
)

data class Publisher(
		val id: String,
		val name: String,
		val pictureProfileUrl: String
)

data class ListVideo(
		val status: Int,
		val data: List<Video>
)

class VideoBuilder {
	var id: String = ""
	var title: String = ""
	var thumbnailUrl: String = ""
	var publishedAt: Date = Date()
	var viewsCount: Long = 0
	var viewsCountLabel: String = ""
	var duration: Int = 0
	var videoURl: String = ""
	var publisher: Publisher = PublisherBuilder().build()

	fun build() : Video = Video(
		id, title, thumbnailUrl, publishedAt, viewsCount,
		viewsCountLabel, duration, videoURl, publisher
	)

	fun publisher(block: PublisherBuilder.() -> Unit): Publisher =
		PublisherBuilder().apply(block).build()
}

class PublisherBuilder {
	var id: String = ""
	var name: String = ""
	var pictureProfileUrl: String = ""

	fun build() : Publisher =
		Publisher(id, name, pictureProfileUrl)
}

// DSL
fun video(block: VideoBuilder.() -> Unit): Video =
	VideoBuilder().apply(block).build()

fun videos() : List<Video> {
	return  arrayListOf(
		video {
			id = "UVpKBHO2fMg"
			thumbnailUrl = "https://img.youtube.com/vi/UVpKBHO2fMg/maxresdefault.jpg"
			title = "Entrevista com Marlon Wayans | The Noite (14/08/19)"
			viewsCount = 742497
			publishedAt = "2019-08-15".toDate()
			duration = 1886
			publisher {
				id = "sbtthenoite"
				name = "The Noite com Danilo Gentili"
				pictureProfileUrl = "https://yt3.ggpht.com/a/AGF-l7_3BYlSlp94WOjGe1UECUCdb73qRJVFH_t9Tw=s48-c-k-c0xffffffff-no-rj-mo"
			}
		},
		video {
			id = "PlYUZU0H5go"
			thumbnailUrl = "https://img.youtube.com/vi/PlYUZU0H5go/maxresdefault.jpg"
			title = "LAST CHRISTMAS Official Trailer (2019) Emilia Clarke Movie"
			viewsCount = 5468366
			publishedAt = "2019-08-28".toDate()
			duration = 194
			publisher {
				id = "UCpJN7kiUkDrH11p0GQhLyFw"
				name = "Movie Trailer Source"
				pictureProfileUrl = "https://yt3.ggpht.com/a/AGF-l7_Qmltcncwt0z_XzAzjxnuE5gVV9uj7zThg2w=s48-c-k-c0xffffffff-no-rj-mo"
			}
		},
		video {
			id = "UVpKBHO2fMg"
			thumbnailUrl = "https://img.youtube.com/vi/UVpKBHO2fMg/maxresdefault.jpg"
			title = "Entrevista com Marlon Wayans | The Noite (14/08/19)"
			viewsCount = 742497
			publishedAt = "2019-08-15".toDate()
			duration = 1886
			publisher {
				id = "sbtthenoite"
				name = "The Noite com Danilo Gentili"
				pictureProfileUrl = "https://yt3.ggpht.com/a/AGF-l7_3BYlSlp94WOjGe1UECUCdb73qRJVFH_t9Tw=s48-c-k-c0xffffffff-no-rj-mo"
			}
		},
		video {
			id = "PlYUZU0H5go"
			thumbnailUrl = "https://img.youtube.com/vi/PlYUZU0H5go/maxresdefault.jpg"
			title = "LAST CHRISTMAS Official Trailer (2019) Emilia Clarke Movie"
			viewsCount = 5468366
			publishedAt = "2019-08-28".toDate()
			duration = 194
			publisher {
				id = "UCpJN7kiUkDrH11p0GQhLyFw"
				name = "Movie Trailer Source"
				pictureProfileUrl = "https://yt3.ggpht.com/a/AGF-l7_Qmltcncwt0z_XzAzjxnuE5gVV9uj7zThg2w=s48-c-k-c0xffffffff-no-rj-mo"
			}
		}
	)
}

fun Date.formatted() : String =
	SimpleDateFormat("d MMM yyyy", Locale("pt", "BR")).format(this)

fun String.toDate() : Date =
	SimpleDateFormat("yyyy-mm-dd", Locale("pt", "BR")).parse(this)
