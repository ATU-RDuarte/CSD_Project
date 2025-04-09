package org.atu

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

data class RsaKeyPair(val privateKey: RSAPrivateKey, val publicKey: RSAPublicKey)
