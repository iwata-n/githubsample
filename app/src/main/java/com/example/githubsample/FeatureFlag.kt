package com.example.githubsample

import kotlinx.coroutines.flow.*

interface FeatureFlag {
    interface OnChangeEnableListener {
        fun onChangeEnable(isEnable: Boolean)
    }
    fun isEnable(key: String): Boolean
    fun isEnableFlow(key: String): Flow<Boolean>
    fun setOnChangeEnableListener(key: String, listener: OnChangeEnableListener)
}

class ReleaseFeatureFlag() : FeatureFlag {
    override fun isEnable(key: String): Boolean = false
    override fun isEnableFlow(key: String): Flow<Boolean> = flow {
        emit(false)
    }

    override fun setOnChangeEnableListener(
        key: String,
        listener: FeatureFlag.OnChangeEnableListener
    ) {
        listener.onChangeEnable(false)
    }
}
