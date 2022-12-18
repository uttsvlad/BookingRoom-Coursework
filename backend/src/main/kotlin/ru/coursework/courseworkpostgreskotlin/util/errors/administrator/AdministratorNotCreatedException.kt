package ru.coursework.courseworkpostgreskotlin.util.errors.administrator

import ru.coursework.courseworkpostgreskotlin.util.errors.MyException

/**
 * @author Vlad Utts
 */
class AdministratorNotCreatedException(override var message: String = "") : MyException(message)