package com.justai.jaicf.template.utils

import com.google.gson.Gson
import com.justai.jaicf.template.templates.Template
import java.net.URL
import kotlin.math.roundToInt

class WeatherUtil {
    private val id = "43b462140ff2e70ef4fb899c3896a2ab"
    private val metric = "metric"
    private val lang = "ru"

    fun computeWeather(q: String): String {
        val url = "https://api.openweathermap.org/data/2.5/weather?APPID=${id}&units=${metric}&lang=${lang}&q=${q}"
        val response = URL(url).readText()

        val gson = Gson()
        val weather: Template = gson.fromJson(response, Template::class.java)

        return "Текущая погода в городе $q:\n" +
                "Температура: ${weather.main.temp.roundToInt()} °C\n" +
                "Влажность: ${weather.main.humidity} %\n" +
                "Скорость ветра: ${weather.wind.speed} м/c"
    }
}