package org.atu

import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object RsaKeyHelper {
    fun generateRsaKeyPair(): RsaKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048)
        val pair = generator.generateKeyPair()
        pair.private as RSAPrivateKey
        return RsaKeyPair(
            pair.private as RSAPrivateKey,
            pair.public as RSAPublicKey
        )
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun convertRsaKeyToJwkString(publicKey: RSAPublicKey) : String
    {
        return "{" +
                "\"alg\": \"RS256\"," +
                "\"kty\": \"RSA\"," +
                "\"use\": \"sig\"," +
                "\"x5c\": \"${publicKey.encoded}\"," +
                "\"x5t\": \"${Base64.UrlSafe.encode(publicKey.encoded)}\"," +
                "\"kid\": \"${Base64.UrlSafe.encode(publicKey.encoded)}\"," +
                "\"e\": \"${Base64.UrlSafe.encode(publicKey.modulus.toByteArray())}\"," +
                "\"n\": \"${Base64.UrlSafe.encode(publicKey.publicExponent.toByteArray())}\"" +
                "}"
    }
}