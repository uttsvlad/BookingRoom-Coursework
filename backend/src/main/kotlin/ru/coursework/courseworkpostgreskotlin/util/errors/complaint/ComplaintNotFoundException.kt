package ru.coursework.courseworkpostgreskotlin.util.errors.complaint

import ru.coursework.courseworkpostgreskotlin.util.errors.MyException

/**
 * @author Vlad Utts
 */
class ComplaintNotFoundException(override var message: String = "") : MyException(message)