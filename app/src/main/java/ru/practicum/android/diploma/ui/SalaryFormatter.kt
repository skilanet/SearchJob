package ru.practicum.android.diploma.ui

import android.content.Context
import ru.practicum.android.diploma.R
import java.util.Locale

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
                formatNumberWithSpaces(to),
                getCurrencySymbol(currency)
            )
        } else if (to == null) {
            context.getString(
                R.string.min_salary,
                formatNumberWithSpaces(from),
                getCurrencySymbol(currency)
            )
        } else {
            context.getString(
                R.string.full_salary,
                formatNumberWithSpaces(from),
                getCurrencySymbol(currency),
                formatNumberWithSpaces(to)
            )
        }
    }

    private fun getCurrencySymbol(currencyCode: String?): String {
        return when (currencyCode) {
            "RUB", "RUR" -> "₽"
            "BYR" -> "Br"
            "USD" -> "$"
            "EUR" -> "€"
            "KZT" -> "₸"
            "UAH" -> "₴"
            "AZN" -> "₼"
            "UZS" -> "сўм"
            "GEL" -> "₾"
            "KGT" -> "сом"
            else -> currencyCode ?: "?"
        }
    }

    private fun formatNumberWithSpaces(number: Int?): String {
        return String.format(Locale("ru"), "%,d", number).replace(',', ' ')
    }
}
