package org.atu

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Date

fun createSelfSignedJwt(
    jsonPayloadString: String,
    rsaPrivateKey: RSAPrivateKey,
): String {
    return JWT.create()
        .withPayload(jsonPayloadString)
        .withExpiresAt(Date(System.currentTimeMillis() + 6000000))
        .sign(Algorithm.RSA256(rsaPrivateKey))
}

fun validateSelfSignedJwt(
    jwt: String,
    rsaPublicKey: RSAPublicKey,
): String {
    try {
        val decodedJWT = JWT.decode(jwt)
        val algorithm: Algorithm = Algorithm.RSA256(rsaPublicKey, null)
        val verifier: JWTVerifier = JWT.require(algorithm).build()
        return verifier.verify(decodedJWT).payload
    } catch (e: Exception) {
        println("[ERROR] | validateSelfSignedJwt | Caught exception ${e.message}")
        return ""
    }
}
