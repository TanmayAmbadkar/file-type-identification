package br.com.lucasalbuquerque.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.lucasalbuquerque.DarkApplication
import br.com.lucasalbuquerque.ui.layout.MainActivityUI
import org.jetbrains.anko.setContentView
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    @Inject lateinit var ui: MainActivityUI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DarkApplication.graph.inject(this)

        ui.setContentView(this)
    }
}