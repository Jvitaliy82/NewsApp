package ru.jdeveloperapps.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.jdeveloperapps.newsapp.models.Article
import ru.jdeveloperapps.newsapp.models.NewsResponse
import ru.jdeveloperapps.newsapp.repository.NewsRepository
import ru.jdeveloperapps.newsapp.util.Resourse

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel() {

    val breakingNews : MutableLiveData<Resourse<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews : MutableLiveData<Resourse<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        breakingNews.postValue(Resourse.Loading())
        val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resourse.Loading())
        val response = newsRepository.searchNews(searchQuery, searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(responce: Response<NewsResponse>): Resourse<NewsResponse> {
        if (responce.isSuccessful) {
            responce.body()?.let {resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resourse.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resourse.Error(responce.message())
    }

    private fun handleSearchNewsResponse(responce: Response<NewsResponse>): Resourse<NewsResponse> {
        if (responce.isSuccessful) {
            responce.body()?.let {resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resourse.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resourse.Error(responce.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.ousert(article)
    }

    fun getSaveNews() = newsRepository.getSaveNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}