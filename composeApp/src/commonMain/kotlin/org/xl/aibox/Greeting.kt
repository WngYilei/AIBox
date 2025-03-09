package org.xl.aibox

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

val KEY ="sk-1d15ac806b0c4aa99d2f455d61e9b64c"