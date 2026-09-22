package com.example.impilo23

import com.example.impilo23.crypto.PasswordHasher
import com.example.impilo23.util.ValidationUtils
import org.junit.Assert.*
import org.junit.Test

/**
 * Robust automated unit testing layer covering main cryptographic validation features.
 */
class SecurityValidationTest {

    @Test
    fun testPasswordSaltingAndHashing() {
        val plainTextPassword = "SuperSecurePassword2026!"
        val saltBlock1 = PasswordHasher.generateSalt()
        val saltBlock2 = PasswordHasher.generateSalt()

        // Ensure each generated cryptographic salt block remains fully unique
        assertNotEquals(saltBlock1, saltBlock2)

        val hashResult1 = PasswordHasher.hashPassword(plainTextPassword, saltBlock1)
        val hashResult2 = PasswordHasher.hashPassword(plainTextPassword, saltBlock1)
        val hashResult3 = PasswordHasher.hashPassword(plainTextPassword, saltBlock2)

        // Verifies reproducibility matching exact configurations
        assertEquals(hashResult1, hashResult2)

        // Verifies absolute cryptographic collision prevention via distinct salts
        assertNotEquals(hashResult1, hashResult3)
    }

    @Test
    fun testEmailPatternConstraints() {
        assertTrue(ValidationUtils.isValidEmail("patient@impilo.co.za"))
        assertTrue(ValidationUtils.isValidEmail("doctor.smith@healthcare.org"))
        
        assertFalse(ValidationUtils.isValidEmail("vacant-at-sign.com"))
        assertFalse(ValidationUtils.isValidEmail("illegal-spaces @domain.com"))
        assertFalse(ValidationUtils.isValidEmail(""))
        assertFalse(ValidationUtils.isValidEmail(null))
    }

    @Test
    fun testPasswordLengthBoundaries() {
        assertTrue(ValidationUtils.isValidPassword("1234"))
        assertTrue(ValidationUtils.isValidPassword("complex_string_sequence"))
        
        assertFalse(ValidationUtils.isValidPassword("123"))
        assertFalse(ValidationUtils.isValidPassword(""))
        assertFalse(ValidationUtils.isValidPassword(null))
    }

    @Test
    fun testNumericalInputsBoundaries() {
        // Int fields check
        assertTrue(ValidationUtils.isValidInt("2500"))
        assertTrue(ValidationUtils.isValidInt("0"))
        assertFalse(ValidationUtils.isValidInt("-5"))
        assertFalse(ValidationUtils.isValidInt("abc"))

        // Double fields check
        assertTrue(ValidationUtils.isValidDouble("70.5"))
        assertTrue(ValidationUtils.isValidDouble("0.0"))
        assertFalse(ValidationUtils.isValidDouble("-12.4"))
        assertFalse(ValidationUtils.isValidDouble("xyz"))
    }
}
