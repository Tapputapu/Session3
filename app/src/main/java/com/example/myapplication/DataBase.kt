package com.example.myapplication

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets

object DataBase {
    fun commonAction(arg: String, method: String, json: String = ""): String {
        val url = URL("$BASE_URL$arg")
        val con = url.openConnection() as HttpURLConnection
        con.apply {
            connectTimeout = TIMEOUT
            requestMethod = method
        }
        if (json.isNotEmpty()) {
            con.apply {
                doOutput = true
                setRequestProperty("Content-Type", "application/json;charset=utf-8")
            }
            OutputStreamWriter(con.outputStream, StandardCharsets.UTF_8).use { it.write(json) }
        }
        when (method) {
            INSERT -> if (con.responseCode != HttpURLConnection.HTTP_CREATED) return FAIL_MESSAGE(
                method
            )
            UPDATE -> if (con.responseCode != HttpURLConnection.HTTP_NO_CONTENT) return FAIL_MESSAGE(
                method
            )
            SELECT -> if (con.responseCode != HttpURLConnection.HTTP_OK) return FAIL_MESSAGE(method)
            DELETE -> if (con.responseCode != HttpURLConnection.HTTP_OK) return FAIL_MESSAGE(method)
        }
        return BufferedReader(
            InputStreamReader(
                con.inputStream,
                StandardCharsets.UTF_8
            )
        ).use { it.readText() }
    }

    val gson = Gson()

    inline fun <reified T> insert(arg: String, objects: T): T =
        gson.fromJson(
            commonAction(arg, INSERT, gson.toJson(objects)),
            object : TypeToken<T>() {}.type
        )

    inline fun <reified T> update(arg: String, objects: T): T =
        gson.fromJson(
            commonAction(arg, UPDATE, gson.toJson(objects)),
            object : TypeToken<T>() {}.type
        )

    inline fun <reified T> select(arg: String): T =
        gson.fromJson(
            commonAction(arg, SELECT),
            object : TypeToken<T>() {}.type
        )

    fun delete(arg: String) = commonAction(arg, DELETE)
}