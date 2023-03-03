package com.example.githubsample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.githubsample.ui.theme.LocalFeatureFlag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DebugFeatureFlag(
    private val dataStore: DataStore<Preferences>
) : FeatureFlag {
    data class Flag(
        val title: String,
        val description: String,
        val isNeedReboot: Boolean,
    )

    override fun isEnable(key: String): Boolean {
        return runBlocking {
            val prefKey = booleanPreferencesKey(key)
            dataStore.data.first()[prefKey] ?: false
        }
    }
    override fun isEnableFlow(key: String): Flow<Boolean> {
        val prefKey = booleanPreferencesKey(key)
        return dataStore.data.map { it[prefKey] ?: false }
    }

    override fun setOnChangeEnableListener(
        key: String,
        listener: FeatureFlag.OnChangeEnableListener
    ) {
        TODO("Not yet implemented")
    }

    fun set(key: String, isEnable: Boolean) {
        runBlocking {
            dataStore.edit { setting ->
                val prefKey = booleanPreferencesKey(key)
                setting[prefKey] = isEnable
            }
        }
    }
}

@Composable
fun FeatureFlagItems(
    flags: Map<String, DebugFeatureFlag.Flag>
) {
    val featureFlag = LocalFeatureFlag.current as DebugFeatureFlag

    LazyColumn() {
        items(flags.keys.toList()) { key ->
            flags[key]?.let { flag ->
                val isEnable: Boolean by featureFlag.isEnableFlow(key).collectAsState(initial = false)
                FlagItem(
                    key = key,
                    flag = flag,
                    isEnable = isEnable,
                    onCheckedChange = {
                        featureFlag.set(key, it)
                    },
                )
            }
        }
    }
}

@Composable
fun FlagItem(
    key: String,
    flag: DebugFeatureFlag.Flag,
    isEnable: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row {
        Column(
            modifier = Modifier.weight(1.0f)
        ) {
            Text(key)
            Text(flag.title)
            Text(flag.description)
        }
        Checkbox(checked = isEnable, onCheckedChange = onCheckedChange)
    }
}

@Preview
@Composable
fun PreviewFlagItem() {
    FlagItem(
        key = "test",
        flag = DebugFeatureFlag.Flag(
            title = "title",
            description = "description",
            isNeedReboot = false,
        ),
        isEnable = true,
        onCheckedChange = {}
    )
}
