package com.example.weather_app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mobile_test.databinding.ActivityMainBinding
import com.example.weather_app.api_weather.WeatherViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Обработка нажатия кнопки "Текущая погода"
        binding.currentWeatherButton.setOnClickListener {
            binding.currentWeather.text = "" // Очищаем текст перед загрузкой
            val city = binding.cityInput.text.toString()
            if (city.isNotEmpty()) {
                val apiKey = "a411f5c770e03952cdb607a00cdaf87b"
                viewModel.fetchCurrentWeather(city, apiKey)
            } else {
                binding.currentWeather.text = "Введите город"
            }
        }

        // Обработка нажатия кнопки "Прогноз на 5 дней"
        binding.forecastButton.setOnClickListener {
            binding.currentWeather.text = "" // Очищаем текст перед загрузкой
            val city = binding.cityInput.text.toString()
            if (city.isNotEmpty()) {
                val apiKey = "a411f5c770e03952cdb607a00cdaf87b"
                viewModel.fetch5DayForecast(city, apiKey)
            } else {
                binding.forecastButton.text = "Введите город"
            }
        }

        // Наблюдаем за текущей погодой
        viewModel.currentWeather.observe(this, Observer { current ->
            if (current != null) {
                binding.currentWeather.text = """
                Текущая погода:
                Город: ${current.name}
                Температура: ${current.main.temp}°C
                Описание: ${current.weather[0].description}
            """.trimIndent()
            } else {
                binding.currentWeather.text = "Ошибка при получении текущей погоды"
            }
        })

        // Наблюдаем за прогнозом на 5 дней
        viewModel.forecastData.observe(this, Observer { forecasts ->
            if (forecasts != null) {
                val forecastText = forecasts.joinToString("\n") { forecast ->
                    """
                    Дата: ${forecast.dt_txt}
                    Температура: ${forecast.main.temp}°C
                    Описание: ${forecast.weather[0].description}
                    """.trimIndent()
                }
                binding.currentWeather.text = forecastText
            } else {
                binding.currentWeather.text = "Ошибка при получении прогноза"
            }
        })
    }
}