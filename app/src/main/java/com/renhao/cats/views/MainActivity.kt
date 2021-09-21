package com.renhao.cats.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.renhao.cats.R
import com.renhao.cats.views.fragments.CatListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ensure fragment created only once - (restored automatically from savedInstanceState on config change)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                // Ensures immediate fragments do not go through lifecycle changes or have animations/transitions executed
                setReorderingAllowed(true)
                replace<CatListFragment>(R.id.cat_list_fragment_container)
            }
        }
    }
}