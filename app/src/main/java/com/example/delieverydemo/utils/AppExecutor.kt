package com.example.delieverydemo.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
​
 * Purpose –
 *
 * Executor:- Executor is an object that executes a submitted runnable tasks.
 * It is normally used instead of explicitly creating threads for each of a set of tasks.

 * @author ​Rishabh Gupta
​
 * Created on January 28, 2020
​
 * Modified on January 28, 2020
 *
 * */

class AppExecutor private constructor(val diskIo: Executor) {

    companion object {
        @Volatile
        private var instance: AppExecutor? = null
        private val LOCK = Any()

        operator fun invoke() = instance
            ?: synchronized(LOCK) {
                instance
                //thread pool used for disk access :- Executors.newSingleThreadExecutor()
                    ?: AppExecutor(Executors.newSingleThreadExecutor())
                        .also {
                            instance = it
                        }
            }
    }

}
