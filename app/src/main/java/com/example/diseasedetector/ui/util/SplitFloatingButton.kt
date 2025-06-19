package com.example.diseasedetector.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diseasedetector.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplitFloatingButton(
    modifier: Modifier = Modifier,
    isChatSelected: Boolean,
    onChatClick: () -> Unit,
    onAnalysisClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .border(1.dp, Color.LightGray, RoundedCornerShape(28.dp))
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isChatSelected) Color(0xFF9B84FF) else Color.White)
                    .fillMaxSize()
                    .clickable { onChatClick() }
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.fl_chat),
                        contentDescription = "Chat",
                        tint = if (isChatSelected) Color.White else Color.Black,
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Chat",
                        color = if (isChatSelected) Color.White else Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (!isChatSelected) Color(0xFF9B84FF) else Color.White)
                    .fillMaxSize()
                    .clickable { onAnalysisClick() }
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.fl_scan),
                        contentDescription = "Analysis",
                        tint = if (!isChatSelected) Color.White else Color.Black,
                        modifier = Modifier.size(23.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Analysis",
                        color = if (!isChatSelected) Color.White else Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}
