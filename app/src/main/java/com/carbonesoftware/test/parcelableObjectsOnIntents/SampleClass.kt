package com.carbonesoftware.test.parcelableObjectsOnIntents

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SampleClass(
    val text: String,
    val numbers: Int
) : Parcelable
