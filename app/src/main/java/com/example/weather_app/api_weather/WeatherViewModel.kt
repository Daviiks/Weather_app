package com.example.weather_app.api_weather

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.weather_app.api_weather.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeatherResponse>()
    val currentWeather: LiveData<CurrentWeatherResponse> get() = _currentWeather

    private val _forecastData = MutableLiveData<List<Forecast>>()
    val forecastData: LiveData<List<Forecast>> get() = _forecastData

    // Метод для загрузки текущей погоды
    @SuppressLint("NullSafeMutableLiveData")
    fun fetchCurrentWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val currentResponse = RetrofitClient.instance.getCurrentWeather(city, apiKey)
                _currentWeather.value = currentResponse
            } catch (e: Exception) {
                // Обработка ошибок
                _currentWeather.value = null
            }
        }
    }

    // Метод для загрузки прогноза на 5 дней
    @SuppressLint("NullSafeMutableLiveData")
    fun fetch5DayForecast(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val forecastResponse = RetrofitClient.instance.get5DayForecast(city, apiKey)
                _forecastData.value = forecastResponse.list
                val filteredForecasts = forecastResponse.list.filter { forecast ->
                    val hour = forecast.dt_txt.substring(11, 13).toInt()
                    hour == 12 || hour == 12 // Оставляем только 00:00 и 12:00
                }
                _forecastData.value = filteredForecasts
            } catch (e: Exception) {
                // Обработка ошибок
                _forecastData.value = null
            }
        }
    }
}