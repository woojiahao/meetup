package com.github.woojiahao.utility.extensions

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Gson.read(text: String): T = fromJson<T>(text, object : TypeToken<T>() {}.type)