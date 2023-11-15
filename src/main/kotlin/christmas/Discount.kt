package christmas

import java.time.LocalDate

class Discount(private val order: Order, private val visitDate: LocalDate) {
    var totalDiscount: Int = 0
    var additionalDiscount: Int = 0
    val eligibleEvents: MutableMap<DiscountEvent, Int> = mutableMapOf<DiscountEvent, Int>().withDefault { 0 }

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
        eligibleEvents[DiscountEvent.DDAY_EVENT] = christmasDDayDiscount(visitDate, order)
        totalDiscount += christmasDDayDiscount(visitDate, order)
    }

    fun applyWeekdayDiscount() {
        if (!isEligibleForWeekdayEvent(visitDate, order)) {
            return
        }
        if (order.itemsPerCategory.getValue(FoodCategory.DESSERT) == 0) {
            return
        }
        eligibleEvents[DiscountEvent.WEEKDAY_EVENT] =
            EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.DESSERT)
        totalDiscount += EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.DESSERT)
    }

    fun applyWeekendDiscount() {
        if (!isEligibleForWeekendEvent(visitDate, order)) {
            return
        }
        if (order.itemsPerCategory.getValue(FoodCategory.MAIN) == 0) {
            return
        }
        eligibleEvents[DiscountEvent.WEEKEND_EVENT] =
            EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.MAIN)
        totalDiscount += EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.MAIN)
    }

    fun applySpecialDiscount() {
        if (!isEligibleForSpecialDiscount(visitDate, order)) {
            return
        }
        eligibleEvents[DiscountEvent.SPECIAL_EVENT] = EventConstraints.SPECIAL_EVENT
        totalDiscount += EventConstraints.SPECIAL_EVENT
    }

    fun applyFreeChampagne() {
        if (!isEligibleForFreeChampagne(order)) {
            return
        }
        eligibleEvents[DiscountEvent.FREE_CHAMPAGNE] = EventConstraints.FREE_CHAMPAGNE_PRICE
        additionalDiscount += EventConstraints.FREE_CHAMPAGNE_PRICE
    }
}

fun christmasDDayDiscount(date: LocalDate, order: Order): Int {
    if (!isEligibleForChristmasDDayEvent(date, order)) {
        return 0
    }
    return EventConstraints.DDAY_BASE_VALUE + (date.dayOfMonth - 1) * EventConstraints.DDAY_INCREMENT
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
