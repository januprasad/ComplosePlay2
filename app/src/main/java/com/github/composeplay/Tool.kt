package com.github.composeplay

fun main() {

    val value = 10
    val value1 = 101
    val result = value.takeIf {
        it in 1..100
    }
    val result1 = value1.takeUnless{
        it in 1..100
    }
    println(result)
    println(result1)
}