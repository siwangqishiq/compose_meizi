package xyz.panyi.meizi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import xyz.panyi.meizi.page.AblumsPage
import xyz.panyi.meizi.ui.theme.Compose_meiziTheme

class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Compose_meiziTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AblumsPage(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
