package org.atu

import java.security.PrivateKey
import java.security.PublicKey

data class RsaKeyPair(val privateKey: PrivateKey, val publicKey: PublicKey)
