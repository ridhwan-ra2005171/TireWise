package com.mrm.tirewise.view.reusablecomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mrm.tirewise.R
import com.mrm.tirewise.view.theme.TireWiseTheme

@Composable
fun GoogleButton(text: String, onClick: () -> Unit) {
    Box(modifier = Modifier
        .wrapContentSize()
        .height(50.dp)
    ) {
            Row(horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(end = 22.dp)
                    .clip(MaterialTheme.shapes.large)
//                    .background(color = Color.LightGray)
                    .border(
                        width = 2.dp, color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.large
                    )
                    .clickable(onClick = onClick)
                    .padding(start = 10.dp, end = 20.dp)
                    .fillMaxHeight()) {
            Image(painter = painterResource(id = R.drawable.google_logo),
                contentDescription = "",
                modifier = Modifier.size(28.dp))
            Text(text = text,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(start = 10.dp))
            }
//        }
        Image(painter = painterResource(id = R.drawable.tire_marks_single_yellow), contentDescription = "",
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterEnd)
        )
    }
}

@Preview
@Composable
fun ArrowButtonRightPreview() {
    TireWiseTheme {
        Scaffold {
            val padd = it
            GoogleButton(
                "Continue with Google",
                onClick = {},
            )
        }
    }
}