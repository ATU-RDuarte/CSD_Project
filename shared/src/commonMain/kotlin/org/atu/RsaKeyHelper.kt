package org.atu

import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PublicKey
import java.security.SecureRandom
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

object RsaKeyHelper {
    fun generateRsaKeyPair(): RsaKeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(2048, SecureRandom())
        val pair = generator.generateKeyPair()
        return RsaKeyPair(pair.private, pair.public)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun publicKeyPemFormat(key: PublicKey): String {
        val rsaPublicKey = key as RSAPublicKey
        return "-----BEGIN PUBLIC KEY-----" +
            Base64.encode(rsaPublicKey.encoded) + "-----END PUBLIC KEY-----"
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun pemToRsaPublicKey(pem: String): RSAPublicKey {
        val publicKeyB64 =
            pem.removePrefix("-----BEGIN PUBLIC KEY-----")
                .removeSuffix("-----END PUBLIC KEY-----")
        val decoded: ByteArray = Base64.decode(publicKeyB64)
        val spec =
            X509EncodedKeySpec(decoded)
        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(spec) as RSAPublicKey
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun bytesToBase64(asByteArray: ByteArray): String {
        return Base64.encode(asByteArray)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64ToBytes(asBase64: String): ByteArray {
        return Base64.decode(asBase64)
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun base64UrlToBytes(asBase64Url: String): ByteArray {
        return Base64.UrlSafe.withPadding(Base64.PaddingOption.ABSENT_OPTIONAL).decode(asBase64Url)
    }
}
