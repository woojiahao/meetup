package com.github.woojiahao

import com.github.woojiahao.api.EngineersSGAPI
import com.github.woojiahao.commands.scheduleDailyUpdate
import com.github.woojiahao.configuration.botToken
import com.github.woojiahao.configuration.databaseUrl
import com.github.woojiahao.database.getDailyPostTiming
import com.github.woojiahao.database.setup
import dev.kord.common.annotation.KordPreview
import dev.kord.core.Kord
import me.jakejmattson.discordkt.api.dsl.bot
import java.util.concurrent.ScheduledExecutorService

var dailyPostService: ScheduledExecutorService? = null
val engineersSGAPI = EngineersSGAPI()

@KordPreview
suspend fun main(args: Array<String>) {
  setup(databaseUrl)

  bot(botToken) {
    prefix{ "-" }

    configure {
      allowMentionPrefix = true
      showStartupLog = true
      recommendCommands = true
    }

    presence {
      listening("--help")
    }

    onStart {
      loadDailyPost(kord)
    }
  }
}

private fun loadDailyPost(kord: Kord) {
  val dailyPostTiming = getDailyPostTiming() ?: return
  println(dailyPostTiming)
  scheduleDailyUpdate(dailyPostTiming, kord)
}