package com.example.diseasedetector.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChoiceBtn(isSelected: Boolean, disease: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 7.dp, horizontal = 10.dp)
            .background(
                if (isSelected) Color(0xFF9B84FF) else Color.White,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                if (isSelected) 0.dp else 1.dp,
                if (isSelected) Color.Transparent else Color(0xFF6036A4),
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = disease,
            color = if (isSelected) Color.White else Color(0xFF9B84FF),
            fontSize = 20.sp,
            fontWeight = FontWeight.W600,
            modifier = Modifier.padding(17.dp)
        )
    }
}