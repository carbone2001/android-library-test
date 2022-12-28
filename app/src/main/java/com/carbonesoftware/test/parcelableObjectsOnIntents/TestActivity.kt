package com.carbonesoftware.test.parcelableObjectsOnIntents

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.carbonesoftware.test.ui.theme.InvestigationTheme

class TestActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sampleObject = intent.getParcelableExtra<SampleClass>("sample-object")

        setContent {
            InvestigationTheme {
                Surface(content = {
                    sampleObject?.let{
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = "Texto:")
                                Text(text = it.text)
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Text(text = "Numero:")
                                Text(text = it.numbers.toString())
                            }
                        }
                    }
                })
            }
        }
    }
}