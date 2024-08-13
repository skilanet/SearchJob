package ru.practicum.android.diploma.ui.vacancy

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.VacancyFull
import ru.practicum.android.diploma.ui.SalaryFormatter
import ru.practicum.android.diploma.util.BindingFragment

class VacancyFragment : BindingFragment<FragmentVacancyBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentVacancyBinding {
        return FragmentVacancyBinding.inflate(
            inflater,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        VacancyFull(
            id = "129102",
            name = "Курьер",
            employerName = "Сервис+ служба доставки",
            employerLogo90 = "https://img.hhcdn.ru/employer-logo/6600305.png",
            employerLogo240 = "https://img.hhcdn.ru/employer-logo/6600306.png",
            employerLogoOriginal = "https://img.hhcdn.ru/employer-logo-original/1244969.png",
            salaryFrom = 52200,
            salaryTo = 102225,
            salaryCurrency = "RUB",
            area = "Липецк",
            employment = "Полная занятость",
            schedule = "Сменный график",
            experience = "Нет опыта",
            keySkills = listOf("Android", "iOS", "Kotlin", "Swift", "Java"),
            description = "<p><strong><em><em>Курьер</em></em> вакансия свободна!</strong></p> <p><strong>Это не " +
                "доставка готовой еды! Товары </strong>из интернет-магазинов.</p> <p><strong>Новая компания в сфере " +
                "доставки и сборки товаров. </strong></p> <p><strong>⚠\uFE0F БОНУС до 2000 рублей за выход на работу в " +
                "течение 48 часов</strong></p> <ul> <li>Доставляйте заказы из интернет-магазинов и зарабатывайте " +
                "<strong>до 4500 руб/день + бонусы</strong>. <em><em>Опыт</em></em> не ну<em><em>жен</em></em> - " +
                "мы всему научим.</li> <li><em><em>Гибкий</em></em> график, стабильный заработок <strong><em>с " +
                "ежедневными выплатами</em></strong>, никаких штрафов!</li> </ul> <p>\u200B\u200B\u200B" +
                "\u200B\u200B\u200B</p> <p><strong>Как стать частью команды :</strong></p> <ol> <li> <p>Оставьте " +
                "отклик на вакансию.</p> </li> <li> <p>В течение 2 минут вы получите ссылку на форму заявки." +
                "</p> </li> <li> <p>Заполните форму и получите ин<em><em>форма</em></em>цию о выборе ближайшего " +
                "места работы.</p> </li> </ol> <p> </p> <p><strong>Идеально подойдет:</strong></p> <ul> <li>Тем," +
                "кто ищет подработку или основную работу</li> <li>Энергичным людям, которые хотят активно проводить " +
                "день</li> </ul> <p> </p> <p><strong>Мы предлагаем:</strong></p> <ul> <li><strong>Гарантированная " +
                "почасовая оплата</strong>, даже если не будет заказов</li> <li>Работать можно любым способом " +
                "передви<em><em>жен</em></em>ия или в комфортном складе</li> <li>Удобный график - вы сами выбираете " +
                "когда работать</li> <li>Ежедневные выплаты и быстрое трудоустройство</li> <li><strong>Выбирайте " +
                "район работы</strong>: работайте в своем районе, без необходимости ездить по всему городу</li> " +
                "<li>Бесплатное обучение с наставником в первый день</li> <li> <p><strong>Нет штрафов</strong>: мы " +
                "не штрафуем за отсутствие термосумки.</p> </li> <li> <p><strong>Телефонная поддержка</strong>: мы " +
                "всегда на связи, помогаем решать любые вопросы.</p> </li> <li> <p><strong>Возможность " +
                "совместительства</strong>: работайте, не мешая учебе или другой работе.</p> </li> <li>Развитие " +
                "внутри компании до руко<em><em>водител</em></em>я группы <em><em>курьер</em></em>ов / склада</li> " +
                "<li>Скидки до 30% на товары &quot;Купер&quot; для себя и семьи</li> </ul> <p> </p> <p><strong>Что " +
                "нужно делать:</strong></p> <ul> <li> <p>Доставлять - собирать заказы от интернет-магазинов</p> </li> " +
                "<li> <p>Ис<em><em>пользовать</em></em> <em><em>мобил</em></em>ьное прило<em><em>жен</em></em>ие для " +
                "получения заказов и ин<em><em>форма</em></em>ции о маршруте или составе заказа.</p> </li> </ul> <p> " +
                "</p> <p><strong>Мы ждем, что вы:</strong></p> <ul> <li>Готовы работать и учиться новому</li> " +
                "<li>Ответственны, пунктуальны и доброжелательны</li> </ul> <p> </p> <p><strong>Что вам понадобится" +
                ":</strong></p> <ul> <li> <p>Желание работать и зарабатывать.</p> </li> <li> <p>Ответственность и " +
                "пунктуальность</p> </li> </ul> <p> </p> <p><strong>Мы приветствуем:</strong></p> <ul> <li> <p><em>" +
                "<em>Опыт</em></em> работы в логистических компаниях, в сфере доставки (Сдек, Достависта, <em><em>" +
                "Авто</em></em> - зона, Яндекс Маркет, Delivery Club, Озон, Сбермаркет или в ООО &quot; Безопасный" +
                " <em><em>водител</em></em>ь &quot;)</p> </li> <li> <p><em><em>Опыт</em></em> работы пешим " +
                "промоутером</p> </li> </ul> <p> </p> <p><strong>Не упустите возможность стать частью " +
                "нашей команды!</strong></p> <p><strong>Делайте отклик прямо сейчас!</strong></p> <p> </p>"
        ).apply {
            with(binding) {
                textVacancyName.text = name
                textVacancySalary.text = SalaryFormatter.format(
                    requireContext(),
                    salaryFrom,
                    salaryTo,
                    salaryCurrency
                )
                Glide.with(requireContext())
                    .load(employerLogo90)
                    .placeholder(R.drawable.placeholder_ic)
                    .into(imageEmployerLogo)
                textEmployerName.text = employerName
                textEmployerLocation.text = area
                textExperience.text = experience
                textBusyness.text = getString(
                    R.string.busyness,
                    employment,
                    schedule
                )
                textParsedDescription.text = Html.fromHtml(
                    description,
                    Html.FROM_HTML_MODE_COMPACT
                )
                textKeySkillsTitle.isVisible = keySkills.isNotEmpty()
                    .also {
                        if (it) {
                            textKeySkills.text = Html.fromHtml(keySkills.joinToString(separator = "\n") { skill ->
                                "<li> <p>$skill</li> <p>"
                            }, Html.FROM_HTML_MODE_COMPACT)
                        }
                    }
            }
        }
    }
}
