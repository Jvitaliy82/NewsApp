package ru.jdeveloperapps.newsapp.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.jdeveloperapps.newsapp.models.NewsResponse
import ru.jdeveloperapps.newsapp.repository.NewsRepository
import ru.jdeveloperapps.newsapp.util.Resourse

class NewsViewModel(
    val newsRepository: NewsRepository
): ViewModel() {

    val breakingNews : MutableLiveData<Resourse<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("us", 1)
    }

    fun getBreakingNews(countryCode: String, pageNumber: Int) = viewModelScope.launch {
        breakingNews.postValue(Resourse.Loading())
        val response = newsRepository.getBreakingNews(countryCode, pageNumber)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(responce: Response<NewsResponse>): Resourse<NewsResponse> {
        if (responce.isSuccessful) {
            responce.body()?.let {resultResponce ->
                return Resourse.Success(resultResponce)
            }
        }
        return Resourse.Error(responce.message())
    }
}