package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.AgroSenseData
import com.example.model.Severity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("AgroSense", appName)
  }

  @Test
  fun `test early blight disease matching on tomato`() {
    val symptoms = setOf("Brown spots", "Yellow leaves")
    val results = AgroSenseData.analyze("tomato", symptoms)
    assertTrue(results.isNotEmpty())
    val topMatch = results.first()
    assertEquals("Early Blight", topMatch.disease.name)
    assertEquals(Severity.MEDIUM, topMatch.disease.severity)
    assertEquals(66, topMatch.confidence)
  }

  @Test
  fun `test bacterial leaf blight on rice`() {
    val symptoms = setOf("Yellow leaves", "Wilting", "Brown spots")
    val results = AgroSenseData.analyze("rice", symptoms)
    assertTrue(results.isNotEmpty())
    val topMatch = results.first()
    assertEquals("Bacterial Leaf Blight", topMatch.disease.name)
    assertEquals(Severity.HIGH, topMatch.disease.severity)
    assertEquals(100, topMatch.confidence)
  }
}

