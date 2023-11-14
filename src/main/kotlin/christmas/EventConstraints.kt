package christmas

import java.time.LocalDate

object EventConstraints {
    val CHRISTMAS_D_DAY_EVENT_START: LocalDate = LocalDate.of(2023, 12, 1)
    val CHRISTMAS_D_DAY_EVENT_END: LocalDate = LocalDate.of(2023, 12, 25)
    val SPECIAL_DISCOUNT_DATE = hashSetOf(
        LocalDate.of(2023, 12, 3),
        LocalDate.of(2023, 12, 10),
        LocalDate.of(2023, 12, 17),
        LocalDate.of(2023, 12, 24),
        LocalDate.of(2023, 12, 25),
        LocalDate.of(2023, 12, 31),
    )
    const val FRIDAY = 5
    const val SATURDAY = 6
    const val FREE_CHAMPAGNE_THRESHOLD = 120_000
    const val SANTA_BADGE_THRESHOLD = 20_000
    const val TREE_BADGE_THRESHOLD = 10_000
    const val STAR_BADGE_THRESHOLD = 5_000
    const val EVENT_THRESHOLD = 10_000
    const val DECEMBER_START = 1
    const val DECEMBER_END = 31
    const val DISCOUNT_PER_ITEM = 2_023
}