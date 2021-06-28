package com.justai.jaicf.template.scenario

import com.justai.jaicf.activator.caila.caila
import com.justai.jaicf.builder.Scenario
import com.justai.jaicf.channel.telegram.telegram
import com.justai.jaicf.template.utils.WeatherUtil

val mainScenario = Scenario {
    state("greeting") {
        activators {
            regex("/start")
            intent("Greeting")
        }

        action {
            val name = request.telegram?.run {
                message.chat.firstName
            }

            reactions.run {
                say(
                    "Здравствуй, $name! Я - WeatherBot. Я могу помочь узнать погоду в любом городе. Давайте начнем!"
                )
            }
        }
    }

    state("weather") {
        activators {
            intent("Weather")
        }

        action {
            val weather = WeatherUtil()

            val city = activator.caila?.run {
                entities[0].value
            }

            if (city != null) {
                reactions.say(weather.computeWeather(city))
            } else {
                reactions.go("/fallback")
            }
        }
    }

    state("bye") {
        activators {
            intent("Bye")
        }

        action {
            reactions.sayRandom(
                "Пока!",
                "Еще увидимся!"
            )
        }
    }

    state("smalltalk", noContext = true) {
        activators {
            anyIntent()
        }

        action(caila) {
            activator.topIntent.answer?.let { reactions.say(it) } ?: reactions.go("/fallback")
        }
    }

    fallback {
        reactions.sayRandom(
            "К сожалению, я не могу это разобрать. Пожалуйста, попробуйте снова.",
            "Упс... Что-то пошло не так..."
        )
    }
}
