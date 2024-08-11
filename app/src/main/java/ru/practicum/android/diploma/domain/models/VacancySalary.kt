package ru.practicum.android.diploma.domain.models

import android.content.Context
import ru.practicum.android.diploma.R

open class VacancySalary(
    open val salaryFrom: Int? = null,
    open val salaryTo: Int? = null,
    open val salaryCurrency: String? = null
) {
    fun generateSalary(context: Context): String {
        return if (salaryFrom == null && salaryTo == null) {
            context.resources.getString(R.string.empty_salary)
        } else if (salaryFrom == null) {
            context.resources.getString(
                R.string.max_salary,
                salaryTo,
                salaryCurrency
            )
        } else if (salaryTo == null) {
            context.resources.getString(
                R.string.min_salary,
                salaryFrom,
                salaryCurrency
            )
        } else {
            context.resources.getString(
                R.string.full_salary,
                salaryFrom,
                salaryCurrency,
                salaryTo
            )
        }
    }
}
