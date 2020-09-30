package ru.jdeveloperapps.newsapp.repository

import ru.jdeveloperapps.newsapp.api.RetrofitInstance
import ru.jdeveloperapps.newsapp.db.ArticleDatabase

class NewsRepository(
    val db: ArticleDatabase
) {
    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
}