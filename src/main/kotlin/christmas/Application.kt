package christmas

import camp.nextstep.edu.missionutils.Console
import java.text.DecimalFormat
import java.time.LocalDate

object ErrorMessage{
    val invalidOrder = "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요."
    val invalidDate = "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요."
}

data class Food(val name: String = "", val category: FoodCategory = FoodCategory.NO_CATEGORY, val price: Int = 0)
class Order {
    val items: MutableMap<Food, Int> = mutableMapOf<Food, Int>().withDefault { 0 }
    val itemsPerCategory: MutableMap<FoodCategory, Int> = mutableMapOf<FoodCategory, Int>().withDefault { 0 }
    var totalQuantity: Int = 0
    var totalPrice: Int = 0
    var nonDrinkQuantity: Int = 0
    fun addItem(menuItem: Food, quantity: Int) {
        require(!items.containsKey(menuItem)) { ErrorMessage.invalidOrder }
        items[menuItem] = items.getValue(menuItem) + quantity
        totalQuantity += quantity
        totalPrice += quantity * menuItem.price
        require(totalQuantity <= OrderConstraints.MAXIMUM_FOOD_PER_ORDER) { ErrorMessage.invalidOrder }
        val itemCategory = menuItem.category
        itemsPerCategory[itemCategory] = itemsPerCategory.getValue(itemCategory) + quantity
        if (itemCategory != FoodCategory.DRINK) {
            nonDrinkQuantity += quantity
        }
    }

    fun consistsOfOnlyDrinks(): Boolean {
        return nonDrinkQuantity == 0
    }
}

enum class DiscountBadge(val badgeName: String) {
    SANTA("산타"), TREE("트리"), STAR("별"), NONE("없음")
}

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

enum class FoodCategory {
    APPETIZER, MAIN, DESSERT, DRINK, NO_CATEGORY
}

object OrderConstraints {
    const val MAXIMUM_FOOD_PER_ORDER = 20
}

class RestaurantMenu {
    val menuItems = mapOf(
        "양송이수프" to Food("양송이수프", FoodCategory.APPETIZER, 6_000),
        "타파스" to Food("타파스", FoodCategory.APPETIZER, 5_500),
        "시저샐러드" to Food("시저샐러드", FoodCategory.APPETIZER, 8_000),
        "티본스테이크" to Food("티본스테이크", FoodCategory.MAIN, 55_000),
        "바비큐립" to Food("바비큐립", FoodCategory.MAIN, 54_000),
        "해산물파스타" to Food("해산물파스타", FoodCategory.MAIN, 35_000),
        "크리스마스파스타" to Food("크리스마스파스타", FoodCategory.MAIN, 25_000),
        "초코케이크" to Food("초코케이크", FoodCategory.DESSERT, 15_000),
        "아이스크림" to Food("아이스크림", FoodCategory.DESSERT, 5_000),
        "제로콜라" to Food("제로콜라", FoodCategory.DRINK, 3_000),
        "레드와인" to Food("레드와인", FoodCategory.DRINK, 60_000),
        "샴페인" to Food("샴페인", FoodCategory.DRINK, 25_000),
    ).withDefault { Food() }

    fun getItem(name: String): Food {
        return menuItems[name] ?: throw IllegalArgumentException(ErrorMessage.invalidOrder)
    }

    fun hasItem(name: String): Boolean {
        return menuItems.containsKey(name)
    }
}

enum class DiscountEvent {
    CHRISTMAS_DDAY_EVENT, WEEKDAY_EVENT, WEEKEND_EVENT, SPECIAL_EVENT, FREE_CHAMPAGNE
}

class Discount(val order: Order, val visitDate: LocalDate) {
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

class InputView {
    fun promptVisitDate(): Int {
        while (true) {
            try {
                println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.")
                println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
                val visitDayRaw = readVisitDate()
                val visitDay = parseVisitDate(visitDayRaw)
                require(isValidDate(visitDay)) { ErrorMessage.invalidDate }
                return visitDay
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun promptOrder(restaurantMenu: RestaurantMenu): Order {
        while (true) {
            try {
                println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)")
                val orderLine = readOrder()
                return parseOrder(orderLine, restaurantMenu)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun readVisitDate(): String {
        return Console.readLine()
    }

    fun readOrder(): String {
        return Console.readLine()
    }

    fun parseVisitDate(input: String): Int {
        return input.toIntOrNull() ?: throw IllegalArgumentException(ErrorMessage.invalidDate)
    }

    fun parseOrder(input: String, restaurantMenu: RestaurantMenu): Order {
        val order = Order()
        val menus: List<String> = input.split(",")
        for (menu in menus) {
            val (itemName, quantity) = menu.split("-")
            require(restaurantMenu.hasItem(itemName)) { ErrorMessage.invalidOrder }
            val item = restaurantMenu.getItem(itemName)
            order.addItem(
                item,
                quantity.toIntOrNull() ?: throw IllegalArgumentException(ErrorMessage.invalidOrder)
            )
        }
        require(!order.consistsOfOnlyDrinks()) { ErrorMessage.invalidOrder }
        return order
    }
}

class OutputView {
    fun printMeritSummary(discount: Discount, order: Order, visitDate: LocalDate) {
        val (totalDiscount, additionalDiscount) = discount.calculateTotalDiscount()
        val totalPrice = order.totalPrice
        val visitDay = visitDate.dayOfMonth
        println("12월 ${visitDay}일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n")
        printOrder(order)
        printTotalPriceBeforeDiscount(order)
        printGiveAwayItem(order)
        printDiscount(discount, order, visitDate)
        printTotalDiscountAmount(totalDiscount, additionalDiscount)
        printNetAmount(totalPrice, totalDiscount)
        printEventBadge(totalDiscount, additionalDiscount)
    }

    fun printOrder(order: Order) {
        println("<주문 메뉴>")
        for ((item, quantity) in order.items) {
            println("${item.name} ${quantity}개")
        }
    }

    fun printTotalPriceBeforeDiscount(order: Order) {
        println("<할인 전 총주문 금액>")
        println("${DecimalFormat("#,###").format(order.totalPrice)}원\n")
    }

    fun printGiveAwayItem(order: Order) {
        println("<증정 메뉴>")
        if (isEligibleForFreeChampagne(order)) {
            println("샴페인 1개\n")
        } else {
            println("없음\n")
        }
    }

    fun printDiscount(discount: Discount, order: Order, visitDate: LocalDate) {
        println("<혜택 내역>")
        if (discount.eligibleEvents.isEmpty()) {
            println("없음")
            return
        }
        val discountMessages = mapOf(
            DiscountEvent.CHRISTMAS_DDAY_EVENT to "크리스마스 디데이 할인: ${
                DecimalFormat("#,###").format(
                    -christmasDDayDiscount(
                        visitDate,
                        order
                    )
                )
            }원",
            DiscountEvent.WEEKDAY_EVENT to "평일 할인: ${
                DecimalFormat("#,###").format(
                    -EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.DESSERT)
                )
            }원",
            DiscountEvent.WEEKEND_EVENT to "주말 할인: ${
                DecimalFormat("#,###").format(
                    -EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.MAIN)
                )
            }원",
            DiscountEvent.SPECIAL_EVENT to "특별 할인: ${DecimalFormat("#,###").format(-1000)}원",
            DiscountEvent.FREE_CHAMPAGNE to "증정 이벤트: -25,000원"
        )
        discount.eligibleEvents.forEach { event -> println(discountMessages[event]) }
    }

    fun printTotalDiscountAmount(totalDiscount: Int, additionalDiscount: Int) {
        println("<총혜택 금액>")
        println("${DecimalFormat("#,###").format(totalDiscount + additionalDiscount)}원\n")
    }

    fun printNetAmount(totalPrice: Int, totalDiscount: Int) {
        println("<할인 후 예상 결제 금액>")
        println("${DecimalFormat("#,###").format(totalPrice - totalDiscount)}원\n")
    }

    fun printEventBadge(totalDiscount: Int, additionalDiscount: Int) {
        println("<12월 이벤트 배지>")
        println(eventBadge(totalDiscount + additionalDiscount))
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

fun eventBadge(totalDiscount: Int): DiscountBadge {
    if (totalDiscount >= EventConstraints.SANTA_BADGE_THRESHOLD) {
        return DiscountBadge.SANTA
    }
    if (totalDiscount >= EventConstraints.TREE_BADGE_THRESHOLD) {
        return DiscountBadge.TREE
    }
    if (totalDiscount >= EventConstraints.STAR_BADGE_THRESHOLD) {
        return DiscountBadge.STAR
    }
    return DiscountBadge.NONE
}

fun isValidDate(day: Int): Boolean {
    return (EventConstraints.DECEMBER_START <= day) and (day <= EventConstraints.DECEMBER_END)
}

fun main() {
    val restaurantMenu = RestaurantMenu()
    val visitDay = InputView().promptVisitDate()
    val visitDate = LocalDate.of(2023, 12, visitDay)
    val order = InputView().promptOrder(restaurantMenu)
    val discount = Discount(order, visitDate)
    OutputView().printMeritSummary(discount, order, visitDate)
}
