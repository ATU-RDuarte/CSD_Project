import org.atu.RsaKeyHelper
import org.atu.RsaKeyHelper.base64UrlToBytes
import org.atu.createSelfSignedJwt
import org.atu.validateSelfSignedJwt
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Test Suite for Shared module
 *
 * This class contains the unit tests for the shared module functionality
 *
 */
class SharedTest {
    /**
     * Test JWT verification happy path
     *
     * Tests a valid self signed JWT is validated successfully
     *
     */
    @Test
    fun jwtVerificationHappyPath() {
        val testPayload = "{\"test\":\"test\"}"
        val keyPair = RsaKeyHelper.generateRsaKeyPair()
        val testJwt = createSelfSignedJwt(testPayload, keyPair.privateKey as RSAPrivateKey)
        val validatedJwtPayload = validateSelfSignedJwt(testJwt, keyPair.publicKey as RSAPublicKey)
        assertTrue(
            base64UrlToBytes(validatedJwtPayload).decodeToString().contains("\"test\":\"test\""),
        )
    }

    /**
     * Test JWT verification happy path
     *
     * Tests a valid self signed JWT fails to verify due to invalid signature verification
     *
     */
    @Test
    fun jwtVerificationFailure() {
        val testPayload = "{\"test\":\"test\"}"
        val keyPair = RsaKeyHelper.generateRsaKeyPair()
        val invalidKeyPair = RsaKeyHelper.generateRsaKeyPair()
        val testJwt = createSelfSignedJwt(testPayload, keyPair.privateKey as RSAPrivateKey)
        val validatedJwtPayload =
            validateSelfSignedJwt(testJwt, invalidKeyPair.publicKey as RSAPublicKey)
        assertTrue(validatedJwtPayload.isEmpty())
    }
}
