package com.example.githubsample

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

object Module {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "feature_flag")

    val flags = mapOf(
        "use_review" to DebugFeatureFlag.Flag(
            title = "レビュー",
            description = "レビュー機能",
            isNeedReboot = false
        ),
        "use_mock_api" to DebugFeatureFlag.Flag(
            title = "Mock API機能",
            description = "Mock API機能を使う",
            isNeedReboot = false
        ),
    )

    fun featureFlag(context: Context): FeatureFlag {
        return if (BuildConfig.DEBUG) {
            DebugFeatureFlag(context.dataStore)
        } else {
            ReleaseFeatureFlag()
        }
    }
}
