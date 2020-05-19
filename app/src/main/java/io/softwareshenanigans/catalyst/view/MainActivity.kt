package io.softwareshenanigans.catalyst.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import io.softwareshenanigans.catalyst.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
