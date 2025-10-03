package com.mrm.tirewise.navigation

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mrm.tirewise.model.UserData
import com.mrm.tirewise.view.screens.dashboard.DashboardDivider
import com.mrm.tirewise.view.theme.BORDER_WIDTH
import com.mrm.tirewise.view.theme.SCREEN_PADDING
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerContainer(navController: NavController, topBarTitle: String, userData: UserData?, onLogout: () -> Unit, content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    var navItems = emptyList<MenuItem>()
    DrawerMenuItems { navItems = it }
    Log.d("DrawerContainer", "DrawerContainer: $navItems")

    fun closeDrawer() {
        scope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
//        scrimColor = MaterialTheme.colorScheme.background,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                drawerTonalElevation =  0.dp,
                modifier = Modifier
                    .padding(end = 32.dp)
            ){
                Scaffold(
                    bottomBar = {
                        Row (modifier = Modifier.fillMaxWidth().padding(bottom = SCREEN_PADDING), horizontalArrangement = Arrangement.Center) {
                            OutlinedButton(
                                shape = MaterialTheme.shapes.large,
                                modifier = Modifier.width(120.dp),
                                border = BorderStroke(width = BORDER_WIDTH, color = Color.Black),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = Color.Black
                                ),
                                onClick = { closeDrawer() }) {
                                Text(text = "Close", fontWeight = FontWeight.Bold)
                            }
                        }
//                                ExtendedFloatingActionButton(
//                                    containerColor = ,
//                                    onClick = { closeDrawer() }) {
//                                    Text(text = "Close")
//                                }
                    },
                    topBar = {
                        Row(verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                        ){
                            if(userData?.profilePictureUrl != null){
                                Box(modifier = Modifier
                                    .fillMaxHeight(0.60f)
                                    .aspectRatio(1f)
                                    .clip(CircleShape)
                                    .background(Color.White, CircleShape)
                                    .border(
                                        BORDER_WIDTH,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    )) {
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = userData.profilePictureUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(0.6f), verticalArrangement = Arrangement.SpaceBetween) {
                                Text(text = userData?.userName?:"", fontWeight = FontWeight.Medium, fontSize = MaterialTheme.typography.titleLarge.fontSize)
                                Text(text = userData?.userEmail?:"", fontSize =  MaterialTheme.typography.bodyMedium.fontSize, color = MaterialTheme.colorScheme.onBackground, overflow = TextOverflow.Ellipsis)
                            }

                            IconButton(
                                onClick = {
//                                    navController.navigate(NavDestination.Welcome.route) {
//                                        // Pop up to the start destination of the graph to
//                                        // avoid building up a large stack of destinations
//                                        // on the back stack as users select items
//                                        popUpTo(NavDestination.Welcome.route) {
//                                            saveState = true
//                                        }
//                                        // Avoid multiple copies of the same destination when
//                                        // re-selecting the same item
//                                        launchSingleTop = true
//                                        // Restore state when re-selecting a previously selected item
//                                        restoreState = true
//                                    }
                                    onLogout()
                                    scope.launch {
                                        drawerState.close() //close drawer
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ExitToApp,
                                    contentDescription = "Logout"
                                )
                            }
                        }
                    }
                ) { padVal ->
                    Column( modifier = Modifier.padding(padVal)) {
                        DashboardDivider(modifier = Modifier.padding(vertical = 10.dp))
                        navItems.forEachIndexed { index, item ->

                            if (item.title.equals( "settings",true)) {
                                DashboardDivider( modifier = Modifier.padding(vertical = 8.dp, horizontal = SCREEN_PADDING))
                            }

                            NavigationDrawerItem(
                                modifier = Modifier.padding(horizontal = SCREEN_PADDING),
                                colors = NavigationDrawerItemDefaults.colors(
                                    unselectedContainerColor = Color.Transparent,
                                ),
                                label = { Text(text = item.title) },
                                selected = /*index == selectedItemIndex*/ navController.currentDestination?.route == item.destination,
                                onClick = {
                                    navController.navigate(item.destination) {
                                        // Pop up to the start destination of the graph to
                                        // avoid building up a large stack of destinations
                                        // on the back stack as users select items
//                                        popUpTo(item.destination) {
//                                            saveState = true
//                                        }
                                        // Avoid multiple copies of the same destination when
                                        // re-selecting the same item
                                        launchSingleTop = true
                                        // Restore state when re-selecting a previously selected item
                                        restoreState = true
                                    }
                                    selectedItemIndex = index
                                    scope.launch {
                                        drawerState.close() //close drawer
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(modifier = Modifier.shadow(5.dp),
                    title = { Text(text = topBarTitle, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(
                            onClick = {//to handle drawer
                                scope.launch {
                                    drawerState.open() //suspend function
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            content = { padding->
                Surface(
                    modifier = Modifier.padding(padding),//padding between topbar and content
                ) {
                    content()
                }
            },

            )
    }
}
