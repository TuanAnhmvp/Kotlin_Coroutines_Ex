package com.example.kotlin_coroutines_ex

import kotlinx.coroutines.*
import kotlin.Exception

fun main() {
    //demojob()
    //demoCoroutinesContext()
    //demoSupervisiorJob()

    heavyTaskInsideCoroutines()
}

fun heavyTaskInsideCoroutines() = runBlocking {
    val job = launch(Dispatchers.Default) {
        repeat(10){
            ensureActive()
            Thread.sleep(1000)

            println("out $it")
        }


    }
    delay(2000)
    job.cancel()
    println("job canceled with status = ${job.isCancelled}")
    Thread.sleep(10000)
}

fun demoSupervisiorJob(){
    val supervisor = SupervisorJob()
    with(CoroutineScope(supervisor)){
        launch {
            println("j1")
            throw Exception("BUG")
        }
        launch {
            println("j2")
        }
    }
}



fun demojob() = runBlocking {
    val job = Job()
    val scope = CoroutineScope(job + Dispatchers.Default)
   val job1 = scope.launch {
       try {
            delay(1000)
            println("haha")
        } catch (e: Exception) {
            println("BUB NE: $e")
        }
    }
    val job2 =scope.launch {
        delay(2000)
        println("hoho")
    }

    job1.cancel()
    job.join()
}

fun demoCoroutinesContext() = runBlocking {
    // job, exception handler, coroutines, dispatchers
    println("Thread runblocking : ${Thread.currentThread().name}")
    val job = Job()
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("BUG NE = $throwable")
    }

    val scope = CoroutineScope(
        job +
                Dispatchers.IO +
                CoroutineName("codeTA") +
                exceptionHandler
    )

    scope.launch {
        println("context = ${coroutineContext}")
        println("job = ${coroutineContext[Job]}")
        println("coroutines name = ${coroutineContext[CoroutineName]}")

        throw Exception("Ahihi")
    }

    scope.async {

    }

    delay(2000)
}