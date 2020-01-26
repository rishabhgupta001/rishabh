package com.example.delieverydemo

fun main() {
    A().doSomthing()

}

class A : i1, i2 {
    override fun doSomthing() {
        super<i1>.doSomthing()
        super<i2>.doSomthing()
       // println("A")
    }

}


interface i1 {
    fun doSomthing() = println("i1 do something")
}


interface i2 {
    fun doSomthing() = println("i2 do something")
}