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
 * and push a health log to your Firebase console.
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
        // We use a random number in the email so every simulation creates a fresh user
        val randomId = Random.nextInt(1000, 9999)
        val testEmail = "test_user_$randomId@impilo.com"

        onView(withId(R.id.etRegUsername)).perform(typeText("Simulated User"), closeSoftKeyboard())
        onView(withId(R.id.etRegEmail)).perform(typeText(testEmail), closeSoftKeyboard())
        onView(withId(R.id.etRegPassword)).perform(typeText("password123"), closeSoftKeyboard())
        onView(withId(R.id.etWaterGoal)).perform(replaceText("3000"), closeSoftKeyboard())
        onView(withId(R.id.etWeightGoal)).perform(replaceText("85.0"), closeSoftKeyboard())

        // 4. Click Register (This creates the 'users' node in Firebase)
        onView(withId(R.id.btnRegister)).perform(click())

        // Wait a few seconds for the network and navigation
        Thread.sleep(5000)

        // 5. Log Health Metrics in Dashboard
        onView(withId(R.id.etLogWater)).perform(typeText("1500"), closeSoftKeyboard())
        onView(withId(R.id.etLogWeight)).perform(typeText("84.2"), closeSoftKeyboard())
        onView(withId(R.id.etLogHeartRate)).perform(typeText("72"), closeSoftKeyboard())
        onView(withId(R.id.etLogSys)).perform(typeText("120"), closeSoftKeyboard())
        onView(withId(R.id.etLogDia)).perform(typeText("80"), closeSoftKeyboard())

        // 6. Save Logs (This creates the 'health_logs' node in Firebase)
        onView(withId(R.id.btnSaveMetrics)).perform(click())

        // Final wait to ensure sync is finished before test closes
        Thread.sleep(3000)
    }
}
