package com.example.githubsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.githubsample.infra.remote.ApiModule
import com.example.githubsample.ui.theme.GitHubSampleTheme
import com.example.githubsample.ui.theme.LocalFeatureFlag
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    // private val api = ApiModule.provideRepositorySearch()
    private val api = ApiModule.provideGitHubApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var query by remember {
                mutableStateOf("query")
            }
            var response by remember {
                mutableStateOf("")
            }
            val scope = rememberCoroutineScope()

            GitHubSampleTheme(
                featureFlag = Module.featureFlag(this)
            ) {
                val featureFlag = LocalFeatureFlag.current
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
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
                                    response = if (featureFlag.isEnable("use_mock_api")) {
                                        "mock"
                                    } else {
                                        val resp = api.searchRepositories(query)
                                        resp.toString()
                                    }
                                }
                            }
                        ) {
                            Text("search")
                        }
                        Text(response)
                        FeatureFlagItems(flags = Module.flags)
                    }

                }
            }
        }
    }
}
