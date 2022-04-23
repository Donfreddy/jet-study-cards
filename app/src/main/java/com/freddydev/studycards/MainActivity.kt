package com.freddydev.studycards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.freddydev.studycards.common.NiceButton
import com.freddydev.studycards.home.HomeList
import com.freddydev.studycards.home.ListFile
import com.freddydev.studycards.theme.StudyCardsTheme

class MainActivity : ComponentActivity() {
  private val data = mutableStateListOf(
    ListFile("Test 1", "14/06/2021"),
    ListFile("Test 2", "14/06/2021"),
    ListFile("Test 3", "14/06/2021"),
    ListFile("Test 4", "14/06/2021")
  )

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      StudyCardsTheme {
        // A surface container using the 'background' color from the theme
        Surface(color = MaterialTheme.colors.background) {
          var editMode by remember { mutableStateOf(false) }
          Scaffold(
            topBar = {
              val title = if (editMode) "Cancel" else "Edit"
              Column {
                Row(Modifier.padding(16.dp)) {
                  NiceButton(title = title) {
                    editMode = !editMode
                  }
                  Spacer(Modifier.weight(1f))
                  NiceButton(title = "Add") {
                    data.add(ListFile("Test ${data.size + 1}", "14/06/2021"))
                  }
                }
              }
            }
          ) {
            HomeList(
              editMode, data, {},
              { index ->
                data.removeAt(index)
              }
            )
          }
        }
      }
    }
  }
}

/**
 * https://github.com/sergenes/compose-demo
 */