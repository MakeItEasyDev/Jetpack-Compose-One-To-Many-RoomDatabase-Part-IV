package com.jetpack.roomdbonetomanyrelationship

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jetpack.roomdbonetomanyrelationship.entity.Library
import com.jetpack.roomdbonetomanyrelationship.entity.User
import com.jetpack.roomdbonetomanyrelationship.entity.UserAndLibrary
import com.jetpack.roomdbonetomanyrelationship.ui.theme.LightGray100
import com.jetpack.roomdbonetomanyrelationship.ui.theme.Purple500
import com.jetpack.roomdbonetomanyrelationship.ui.theme.RoomDbOneToManyRelationshipTheme
import com.jetpack.roomdbonetomanyrelationship.viewmodel.UserViewModel
import com.jetpack.roomdbonetomanyrelationship.viewmodel.UserViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDbOneToManyRelationshipTheme {
                Surface(color = MaterialTheme.colors.background) {
                    OneToManyDb()
                }
            }
        }
    }
}

val userData = listOf(
    User(1, "User 1", 10),
    User(2, "User 2", 11),
    User(3, "User 3", 12),
    User(4, "User 4", 13),
    User(5, "User 5", 14)
)

val libraryData = listOf(
    Library(1, "Library 1", 1),
    Library(2, "Library 2", 2),
    Library(3, "Library 3", 1),
    Library(4, "Library 4", 2),
    Library(5, "Library 5", 4),
    Library(6, "Library 6", 5),
    Library(7, "Library 7", 1)
)

@Composable
fun OneToManyDb() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(context.applicationContext as Application)
    )

    //insert the data's
    userViewModel.addUser(userData)
    userViewModel.addLibrary(libraryData)

    val getUserRecord = userViewModel.readAllData.observeAsState(listOf()).value

    val userId = remember { mutableStateOf("") }

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple500)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "One To Many Relationship", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OutlinedTextField(
                    value = userId.value,
                    onValueChange = { userId.value = it },
                    label = { Text(text = "Enter User Id") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                Spacer(modifier = Modifier.height(25.dp))

                Button(
                    onClick = {
                        scope.launch {
                            userViewModel.getUser(userId.value.toInt())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(60.dp)
                        .padding(10.dp),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.buttonColors(Purple500)
                ) {
                    Text(text = "Submit", color = Color.White, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(50.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Purple500)
                        .padding(15.dp)
                ) {
                    Text(
                        text = "User Id",
                        modifier = Modifier.weight(0.3f),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Name",
                        modifier = Modifier.weight(0.3f),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Age",
                        modifier = Modifier.weight(0.3f),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (getUserRecord.isNotEmpty()) {
                    LazyColumn {
                        //User Data
                        items(getUserRecord.size) { index ->
                            UserDataList(getUserRecord[index])
                        }

                        //Library Data
                        item {
                            Spacer(modifier = Modifier.height(30.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Purple500)
                                    .padding(15.dp)
                            ) {
                                Text(
                                    text = "Library Id",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Name",
                                    modifier = Modifier.weight(0.3f),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        items(getUserRecord[0].library.size) {index ->
                            LibraryDataList(getUserRecord[0].library[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserDataList(userAndLibrary: UserAndLibrary) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray100)
            .padding(15.dp)
    ) {
        Text(
            text = userAndLibrary.user.userId.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userAndLibrary.user.name,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = userAndLibrary.user.age.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LibraryDataList(library: Library) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(LightGray100)
            .padding(15.dp)
    ) {
        Text(
            text = library.id.toString(),
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = library.title,
            modifier = Modifier.weight(0.3f),
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}


















