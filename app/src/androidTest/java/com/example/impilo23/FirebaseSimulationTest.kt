package com.example.impilo23

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random

/**
 * AUTOMATED SIMULATION SCRIPT
 * This test will automatically navigate the UI, register a test user,
 * navigate to Health Hub, and push health logs.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class FirebaseSimulationTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun runFullSimulation() {
        // 1. Start from Welcome Screen
        onView(withId(R.id.btnGetStarted)).perform(click())

        // 2. Go to Registration
        onView(withId(R.id.btnGoToRegister)).perform(click())

        // 3. Fill in Registration Details
        val randomId = Random.nextInt(1000, 9999)
        val testEmail = "test_user_$randomId@impilo.com"

        onView(withId(R.id.etRegUsername)).perform(typeText("Simulated User"), closeSoftKeyboard())
        onView(withId(R.id.etRegEmail)).perform(typeText(testEmail), closeSoftKeyboard())
        onView(withId(R.id.etRegPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.etWaterGoal)).perform(replaceText("3000"), closeSoftKeyboard())
        onView(withId(R.id.etWeightGoal)).perform(replaceText("85.0"), closeSoftKeyboard())

        // 4. Click Register
        onView(withId(R.id.btnRegister)).perform(click())

        // Wait for registration and navigation
        Thread.sleep(4000)

        // 5. Navigate to Health Hub
        onView(withId(R.id.btnNavHealth)).perform(click())

        // Wait for Health Hub to open
        Thread.sleep(2000)

        // 6. Log Water Intake
        onView(withId(R.id.btnLogWaterPill)).perform(click())

        // Final wait to finish sync
        Thread.sleep(2000)
    }
}
