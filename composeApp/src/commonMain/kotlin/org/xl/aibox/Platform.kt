package org.xl.aibox

import io.ktor.client.*
import kotlinx.coroutines.CoroutineDispatcher


interface Platform {
    val name: String
    fun getClient(): HttpClient
    val ApplicationDispatcher: CoroutineDispatcher
}

expect fun getPlatform(): Platform
