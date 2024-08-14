package ru.practicum.android.diploma.ui

import android.content.Context
import ru.practicum.android.diploma.R

object SalaryFormatter {
    fun format(
        context: Context,
        from: Int?,
        to: Int?,
        currency: String?
    ): String {
        return if (from == null && to == null) {
            context.getString(R.string.empty_salary)
        } else if (from == null) {
            context.getString(
                R.string.max_salary,
                to,
                currency
            )
        } else if (to == null) {
            context.getString(
                R.string.min_salary,
                from,
                currency
            )
        } else {
            context.getString(
                R.string.full_salary,
                from,
                currency,
                to
            )
        }
    }
}
