package org.xl.aibox

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
    override fun getClient(): HttpClient {
        return HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json()
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
        }
    }

    override val ApplicationDispatcher: CoroutineDispatcher
        get() = Dispatchers.Main
}

actual fun getPlatform(): Platform = JVMPlatform()