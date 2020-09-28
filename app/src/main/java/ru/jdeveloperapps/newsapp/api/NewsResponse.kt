package ru.jdeveloperapps.newsapp.api

import ru.jdeveloperapps.newsapp.api.Article

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)