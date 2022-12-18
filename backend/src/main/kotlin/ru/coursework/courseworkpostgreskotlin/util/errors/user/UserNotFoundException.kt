package ru.coursework.courseworkpostgreskotlin.util.errors.user

import ru.coursework.courseworkpostgreskotlin.util.errors.MyException

/**
 * @author Vlad Utts
 */
class UserNotFoundException(override var message: String = "") : MyException(message)