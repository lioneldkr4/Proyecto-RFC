package com.example.rfcmultiplataforma

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform