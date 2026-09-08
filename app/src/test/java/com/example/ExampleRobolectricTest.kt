package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
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
    assertEquals("ObjectLens", appName)
  }

  @Test
  fun `verify knowledge base lookup`() {
    val laptop = com.example.data.local.ObjectKnowledgeBase.getObjectInfo("laptop")
    assertEquals("Laptop", laptop.name)
    assertEquals("Electronics", laptop.category)
    assertEquals("💻", laptop.iconEmoji)
  }
}
