package ru.coursework.courseworkpostgreskotlin.util.errors

/**
 * @author Vlad Utts
 */

open class MyException(override var message: String = "") : RuntimeException(message)