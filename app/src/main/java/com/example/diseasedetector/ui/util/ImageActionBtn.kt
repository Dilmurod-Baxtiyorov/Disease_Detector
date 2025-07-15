package com.example.diseasedetector.ui.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diseasedetector.R

@Composable
fun ImageActionBtn(
    isTakePictureSelected: Boolean,
    isUploadPictureSelected: Boolean,
    onTakePictureClick: () -> Unit,
    onUploadPictureClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable { onTakePictureClick() }
                .background(
                    if (isTakePictureSelected) Color(0xFF6036A4) else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    if (isTakePictureSelected) 0.dp else 1.dp,
                    if (isTakePictureSelected) Color.Transparent else Color(0xFF6036A4),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_camera),
                    contentDescription = null,
                    tint = if (isTakePictureSelected) Color.White else Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Take picture",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = if (isTakePictureSelected) Color.White else Color.Black
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .clickable { onUploadPictureClick() }
                .background(
                    if (isUploadPictureSelected) Color(0xFF6036A4) else Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    if (isUploadPictureSelected) 0.dp else 1.dp,
                    if (isUploadPictureSelected) Color.Transparent else Color(0xFF6036A4),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    tint = if (isUploadPictureSelected) Color.White else Color.Black,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upload picture",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.W500,
                    color = if (isUploadPictureSelected) Color.White else Color.Black
                )
            }
        }
    }
}