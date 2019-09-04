package api

import org.junit.Test

class EngineersSGAPITest {
  @Test
  fun testEventsList() {
    val api = EngineersSGAPI()
    api.getEvents(20).forEach(::println)
  }
}