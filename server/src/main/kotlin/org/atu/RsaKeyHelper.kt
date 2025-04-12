package org.atu

import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.SecureRandom
import java.security.interfaces.RSAPublicKey
import java.util.UUID
import kotlin.io.encoding.*

object RsaKeyHelper {
    fun generateRsaKeyPair(): RsaKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048, SecureRandom())
        val pair = generator.generateKeyPair()
        return RsaKeyPair(pair.private, pair.public)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun publicKeyPemFormat(key: PublicKey) : String
    {
        val rsaPublicKey = key as RSAPublicKey
        return "-----BEGIN PUBLIC KEY-----" +
                            Base64.encode( rsaPublicKey.encoded) + "-----END PUBLIC KEY-----"
    }
}