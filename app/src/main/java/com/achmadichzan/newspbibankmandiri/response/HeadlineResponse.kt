package com.achmadichzan.newspbibankmandiri.response

import com.google.gson.annotations.SerializedName

data class HeadlineResponse(

	@field:SerializedName("totalResults")
	val totalResults: Int? = null,

	@field:SerializedName("articles")
	val articles: List<ArticlesItems?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Sources(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class ArticlesItems(

	@field:SerializedName("publishedAt")
	val publishedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("urlToImage")
	val urlToImage: Any? = null,

	@field:SerializedName("description")
	val description: Any? = null,

	@field:SerializedName("source")
	val source: Sources? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("url")
	val url: String? = null,

	@field:SerializedName("content")
	val content: Any? = null
)
