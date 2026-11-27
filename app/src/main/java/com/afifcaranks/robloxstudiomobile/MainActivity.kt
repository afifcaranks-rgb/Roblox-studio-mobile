package com.afifcaranks.robloxstudiomobile
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.afifcaranks.robloxstudiomobile.ui.EditorScreen
import com.afifcaranks.robloxstudiomobile.ui.theme.StudioMobileTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { StudioMobileTheme { Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) { EditorScreen() } } }
    }
}
