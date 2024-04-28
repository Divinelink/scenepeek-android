package com.andreolas.movierama.settings

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.andreolas.movierama.R
import com.andreolas.movierama.settings.app.SettingsScreen
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSettingsTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(SettingsScreen::class.java)
//    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun navigateBetweenSettingsFragments() {
        dismissANRSystemDialog()
        onView(withText(R.string.preferences__appearance))
            .check(matches(isDisplayed()))
            .perform(click())

        pressBack()

        onView(withText(R.string.HelpSettingsFragment__help))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.HelpSettingsFragment__version))
            .check(matches(isDisplayed()))
    }

    @Test
    fun updateThemeTest() {
        dismissANRSystemDialog()
        onView(withText(R.string.preferences__appearance))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.preferences__theme))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.preferences__light_theme))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.preferences__theme))
            .check(matches(isDisplayed()))
            .perform(click())

        onView(withText(R.string.preferences__dark_theme))
            .check(matches(isDisplayed()))
            .perform(click())
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun dismissANRSystemDialog() {
            val device = UiDevice.getInstance(getInstrumentation())
            // If running the device in English Locale
            var waitButton = device.findObject(UiSelector().textContains("wait"))
            if (waitButton.exists()) {
                waitButton.click()
            }
            // If running the device in Japanese Locale
            waitButton = device.findObject(UiSelector().textContains("待機"))
            if (waitButton.exists()) {
                waitButton.click()
            }
        }
    }
}
