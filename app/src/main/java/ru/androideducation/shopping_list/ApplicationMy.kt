package ru.androideducation.shopping_list

import android.app.Application
import ru.androideducation.shopping_list.di.DaggerAppComponent


class ApplicationMy: Application() {
    val component by lazy {
        DaggerAppComponent.factory()
           .create(this)
    }
}