@file:Suppress("MagicNumber")
package com.andreolas.movierama.base.communication

import io.ktor.client.plugins.logging.Logger

class HttpLogger : Logger {
    override fun log(message: String) {
        if (message.length > 2800) {
            var chunkCount = message.length / 2800
            if (chunkCount > 100) { // reduce clutter
                chunkCount = 1
            }
            var chunkMessage: String
            println("HttpClient: ")
            // integer division
            for (i in 0..chunkCount) {
                val max = 2800 * (i + 1)
                if (max >= message.length) {
                    chunkMessage = message.substring(2800 * i)
                    println(chunkMessage)
                } else {
                    chunkMessage = message.substring(2800 * i, max)
                    println(chunkMessage)
                }
            }
        } else {
            println("HttpClient:$message")
        }
    }
}
