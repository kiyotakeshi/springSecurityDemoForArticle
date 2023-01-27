package com.example.zenn.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

/**
 * @author kiyota
 */
@ConfigurationProperties(prefix = "rsa")
data class RsaKeyProperties(val publicKey: RSAPublicKey, val privateKey: RSAPrivateKey)
