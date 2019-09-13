package utility

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

fun scheduleDailyTask(timeToTrigger: LocalDateTime, action: () -> Unit): ScheduledExecutorService {
  val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mma")

  println("Generating timer event for daily posts")
  println("Current calendar is posting on ${timeToTrigger.format(formatter)}")

  val currentTime = singaporeDateTime.toInstant().toEpochMilli()

  val timeDifference = timeToTrigger.toEpochMilli(singaporeZone) - currentTime
  println("Time difference is $timeDifference")

  val postingTime = if (timeDifference < 0) timeToTrigger.plusDays(1) else timeToTrigger
  println("Events to be posted on ${postingTime.format(formatter)}")

  val delay = postingTime.toEpochMilli(singaporeZone) - currentTime
  println("Delay will be $delay")

  return Executors.newScheduledThreadPool(1).apply {
    scheduleAtFixedRate({ action() }, delay, TimeUnit.DAYS.toMillis(1), TimeUnit.MILLISECONDS)
  }
}