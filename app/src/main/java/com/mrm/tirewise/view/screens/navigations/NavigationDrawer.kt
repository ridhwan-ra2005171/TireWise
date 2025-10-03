package com.mrm.tirewise.view.screens.navigations

import android.os.Build
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DrawerHeader(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 56.dp),
    contentAlignment = Alignment.Center
    ){
        Text(text = "Header", fontSize = 60.sp)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DrawerBody(
    items:List<MenuItem>,
    modifier: Modifier = Modifier,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onItemClick: (menuItem: MenuItem) -> Unit
){
    LazyColumn(modifier){
        items(items){ item->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(15.dp)
            ){
                Icon(
                    painter = painterResource(id = item.icon as Int),
                    contentDescription = item.contentDescription.toString()
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = item.title.toString(),
                    style = itemTextStyle,
                    modifier = Modifier.weight(1f))
            }

        }
    }
}