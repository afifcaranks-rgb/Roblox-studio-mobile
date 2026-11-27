package com.afifcaranks.robloxstudiomobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afifcaranks.robloxstudiomobile.ui.viewport.FilamentViewport

private val editorBg = Color(0xFF11151D)
private val panelBg = Color(0xFF1B2230)

@Composable
fun EditorScreen() {
    Column(Modifier.fillMaxSize().background(editorBg).padding(12.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("STUDIO MOBILE", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Project: Untitled", color = Color.LightGray, fontSize = 12.sp)
            }
            OutlinedButton(onClick = {}) { Text("Play") }
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ToolButton("Select", Modifier.weight(1f))
            ToolButton("Move", Modifier.weight(1f))
            ToolButton("Rotate", Modifier.weight(1f))
            ToolButton("Scale", Modifier.weight(1f))
        }

        Spacer(Modifier.height(12.dp))
        Text("3D WORKSPACE", color = Color(0xFFB9C7DA), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))

        Box(
            Modifier.fillMaxWidth().weight(1f).background(Color(0xFF090D14), RoundedCornerShape(12.dp))
        ) {
            FilamentViewport(Modifier.fillMaxSize())
            Text(
                "Drag to rotate",
                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp),
                color = Color(0xFFB9C7DA),
                fontSize = 11.sp
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoPanel("Explorer", "Workspace\n  Camera\n  Baseplate", Modifier.weight(1f))
            InfoPanel("Properties", "Select an object\nto inspect its properties.", Modifier.weight(1f))
        }
    }
}

@Composable
private fun ToolButton(label: String, modifier: Modifier) {
    OutlinedButton(onClick = {}, modifier = modifier) {
        Text(label, fontSize = 11.sp)
    }
}

@Composable
private fun InfoPanel(title: String, detail: String, modifier: Modifier) {
    Column(modifier.background(panelBg, RoundedCornerShape(10.dp)).padding(10.dp)) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(6.dp))
        Text(detail, color = Color.LightGray, fontSize = 12.sp)
    }
}
