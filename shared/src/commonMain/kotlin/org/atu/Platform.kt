package org.atu

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
