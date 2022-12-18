package ru.coursework.courseworkpostgreskotlin.util.errors.user

import ru.coursework.courseworkpostgreskotlin.util.errors.MyException

/**
 * @author Vlad Utts
 */
class UserNotCreatedException(override var message: String = "") : MyException(message)