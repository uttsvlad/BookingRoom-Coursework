package ru.coursework.courseworkpostgreskotlin.util.errors.category

import ru.coursework.courseworkpostgreskotlin.util.errors.MyException

/**
 * @author Vlad Utts
 */
class CategoryNotFoundException(override var message: String = "") : MyException(message)