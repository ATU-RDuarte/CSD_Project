package org.atu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val serverUrl = "http://10.0.2.2:8080"
            App(serverUrl = serverUrl)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App("http://10.0.2.2:8080")
}
