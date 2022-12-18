package ru.coursework.courseworkpostgreskotlin.util.errors

import org.springframework.validation.BindingResult

/**
 * @author Vlad Utts
 */
class ErrorsReturner {
    companion object {
        fun returnTextOfErrors(bindingResult: BindingResult): String {
            val stringBuilder = StringBuilder()
            val errors = bindingResult.fieldErrors
            for (error in errors) {
                stringBuilder.append(error.field)
                    .append(" - ")
                    .append(error.defaultMessage)
                    .append("; ")
            }
            return stringBuilder.toString()
        }

        fun returnErrors(bindingResult: BindingResult, e: MyException) {
            e.message = returnTextOfErrors(bindingResult)

            throw e
        }
    }
}