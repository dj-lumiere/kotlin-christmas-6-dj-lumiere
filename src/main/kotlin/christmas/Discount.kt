package christmas

import java.time.LocalDate

class Discount(private val order: Order, private val visitDate: LocalDate) {
    var totalDiscount: Int = 0
    var additionalDiscount: Int = 0
    val eligibleEvents: MutableList<DiscountEvent> = mutableListOf()

    fun calculateTotalDiscount(): Pair<Int, Int> {
        applyChristmasDDayDiscount()
        applyWeekdayDiscount()
        applyWeekendDiscount()
        applySpecialDiscount()
        applyFreeChampagne()
        return totalDiscount to additionalDiscount
    }

    fun applyChristmasDDayDiscount() {
        if (!isEligibleForChristmasDDayEvent(visitDate, order)) {
            return
        }
        eligibleEvents.add(DiscountEvent.CHRISTMAS_DDAY_EVENT)
        totalDiscount += christmasDDayDiscount(visitDate, order)
    }

    fun applyWeekdayDiscount() {
        if (!isEligibleForWeekdayEvent(visitDate, order)) {
            return
        }
        if (order.itemsPerCategory.getValue(FoodCategory.DESSERT) == 0) {
            return
        }
        eligibleEvents.add(DiscountEvent.WEEKDAY_EVENT)
        totalDiscount += 2_023 * order.itemsPerCategory.getValue(FoodCategory.DESSERT)
    }

    fun applyWeekendDiscount() {
        if (!isEligibleForWeekendEvent(visitDate, order)) {
            return
        }
        if (order.itemsPerCategory.getValue(FoodCategory.MAIN) == 0) {
            return
        }
        eligibleEvents.add(DiscountEvent.WEEKEND_EVENT)
        totalDiscount += 2_023 * order.itemsPerCategory.getValue(FoodCategory.MAIN)
    }

    fun applySpecialDiscount() {
        if (!isEligibleForSpecialDiscount(visitDate, order)) {
            return
        }
        eligibleEvents.add(DiscountEvent.SPECIAL_EVENT)
        totalDiscount += 1_000
    }

    fun applyFreeChampagne() {
        if (!isEligibleForFreeChampagne(order)) {
            return
        }
        eligibleEvents.add(DiscountEvent.FREE_CHAMPAGNE)
        additionalDiscount += 25_000
    }
}

fun christmasDDayDiscount(date: LocalDate, order: Order): Int {
    if (!isEligibleForChristmasDDayEvent(date, order)) {
        return 0
    }
    return 1000 + (date.dayOfMonth - 1) * 100
}

fun isWeekend(date: LocalDate): Boolean {
    return (date.dayOfWeek.value == EventConstraints.FRIDAY) or (date.dayOfWeek.value == EventConstraints.SATURDAY)
}

fun isWeekday(date: LocalDate): Boolean {
    return !isWeekend(date)
}

fun isEligibleForChristmasDDayEvent(date: LocalDate, order: Order): Boolean {
    return isEligibleForAnyEvent(order) and ((EventConstraints.CHRISTMAS_D_DAY_EVENT_START <= date) and (date <= EventConstraints.CHRISTMAS_D_DAY_EVENT_END))
}

fun isEligibleForWeekdayEvent(date: LocalDate, order: Order): Boolean {
    return isEligibleForAnyEvent(order) and isWeekday(date)
}

fun isEligibleForWeekendEvent(date: LocalDate, order: Order): Boolean {
    return isEligibleForAnyEvent(order) and isWeekend(date)
}

fun isEligibleForAnyEvent(order: Order): Boolean {
    return order.totalPrice >= EventConstraints.EVENT_THRESHOLD
}

fun isEligibleForSpecialDiscount(date: LocalDate, order: Order): Boolean {
    return EventConstraints.SPECIAL_DISCOUNT_DATE.contains(date) and isEligibleForAnyEvent(order)
}

fun isEligibleForFreeChampagne(order: Order): Boolean {
    return order.totalPrice >= EventConstraints.FREE_CHAMPAGNE_THRESHOLD
}