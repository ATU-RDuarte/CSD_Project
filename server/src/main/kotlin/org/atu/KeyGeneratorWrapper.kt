package org.atu

import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


object KeyGeneratorWrapper {
    @OptIn(ExperimentalEncodingApi::class)
    fun generateKeyPair(): RsaKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair = generator.generateKeyPair()
        pair.private as RSAPrivateKey
        return RsaKeyPair(
            pair.private as RSAPrivateKey,
            pair.public as RSAPublicKey
        )
    }
}