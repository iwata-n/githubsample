package com.example.githubsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.githubsample.infra.remote.ApiModule
import com.example.githubsample.ui.theme.GitHubSampleTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val api = ApiModule.provideRepositorySearch()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var owner by remember {
                mutableStateOf("iwata-n")
            }
            var repo by remember {
                mutableStateOf("wip")
            }
            var query by remember {
                mutableStateOf("query")
            }
            var response by remember {
                mutableStateOf("")
            }
            val scope = rememberCoroutineScope()

            GitHubSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        TextField(
                            value = query,
                            singleLine = true,
                            label = {
                                Text("query")
                            },
                            onValueChange = { v ->
                                query = v
                            }
                        )
                        Button(
                            onClick = {
                                scope.launch {
                                    val resp = api.searchRepositories(query)
                                    response = resp.toString()
                                }
                            }
                        ) {
                            Text("search")
                        }
                        Text(response)
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GitHubSampleTheme {
        Greeting("Android")
    }
}
