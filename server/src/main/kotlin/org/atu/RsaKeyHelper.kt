package org.atu

import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

object RsaKeyHelper {
    fun generateRsaKeyPair(): RsaKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair = generator.generateKeyPair()
        return RsaKeyPair(
            pair.private as RSAPrivateKey,
            pair.public as RSAPublicKey
        )
    }
}