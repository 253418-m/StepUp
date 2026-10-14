
package com.example.stepup

import android.app.Application

class StepUpApplication : Application() {

    val container: StepUpContainer by lazy {
        StepUpContainer(applicationContext)
    }
}
