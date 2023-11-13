package christmas

enum class foodCategory(name: String) {
    APPETIZER("애피타이저"), MAIN("메인"), DESSERT("디저트"), DRINK("음료"), NO_CATEGORY("")
}

fun parseOrder(input: String): Map<String, Int> {
    val order: MutableMap<String, Int> = mutableMapOf()
    val menus: List<String> = input.split(",")
    for (menu in menus) {
        val (item, quantity) = menu.split("-")
        order[item] = quantity.toIntOrNull() ?: throw IllegalArgumentException("[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요.")
    }
    return order
}

fun areEveryFoodInMenu(order: Map<String, Int>): Boolean {
    for ((item, _) in order) {
        return categoryOfFood.containsKey(item)
    }
    return true
}

fun areEveryOrderUnique(order: Map<String, Int>): Boolean {
    val uniqueOrder: HashSet<String> = hashSetOf()
    for ((item, _) in order) {
        return uniqueOrder.add(item)
    }
    return true
}

fun categoryQuantityCount(order: Map<String, Int>): MutableMap<foodCategory, Int> {
    val categoryQuantity: MutableMap<foodCategory, Int> = mutableMapOf()
    for (category in foodCategory.entries) {
        categoryQuantity[category] = 0
    }
    for ((item, quantity) in order) {
        categoryQuantity[categoryOfFood[item]!!] = categoryQuantity[categoryOfFood[item]!!] + quantity
    }
    return categoryQuantity
}

fun isOrderConsistOfOnlyDrink(categoryQuantity: Map<foodCategory, Int>): Boolean {
    return (categoryQuantity[foodCategory.APPETIZER] == 0) and (categoryQuantity[foodCategory.MAIN] == 0) and (categoryQuantity[foodCategory.DESSERT] == 0)
}

fun isValidOrder(order: Map<String, Int>, categoryQuantity: Map<foodCategory, Int>): Boolean {
    return (isThereTooManyFoodToOrder(order) and areEveryFoodInMenu(order) and areEveryOrderUnique(order) and !isOrderConsistOfOnlyDrink(
        categoryQuantity
    ))
}

fun isValidDate(day: Int): Boolean {
    return (1 <= day) and (day <= 31)
}

fun parseVisitDate(input: String): Int {
    return input.toIntOrNull() ?: throw IllegalArgumentException("[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요.")
}

fun christmasDDayDiscount(date: LocalDate): Int {
    if (!((LocalDate.of(2023, 12, 1) <= date) and (date <= LocalDate.of(2023, 12, 25)))) {
        return 0
    }
    return 1000 + (date.dayOfMonth - 1) * 100
}

fun isWeekEnd(date: LocalDate): Boolean {
    return (date.dayOfWeek.value == 5) or (date.dayOfWeek.value == 6)
}

fun isWeekday(date: LocalDate): Boolean {
    return !isWeekEnd(date)
}

fun isEligibleForSpecialDiscount(date: LocalDate): Boolean {
    val specialDate: HashSet<LocalDate> = hashSetOf(
        LocalDate.of(2023, 12, 3),
        LocalDate.of(2023, 12, 10),
        LocalDate.of(2023, 12, 17),
        LocalDate.of(2023, 12, 24),
        LocalDate.of(2023, 12, 25),
        LocalDate.of(2023, 12, 31),
    )
    return specialDate.contains(date)
}

fun isEligibleForFreeChampagne(totalPrice: Int): Boolean {
    return totalPrice >= 120_000
}

fun eligibleEvent(date: LocalDate): ArrayList<Boolean> {
    val eligibleEvents: ArrayList<Boolean> = ArrayList(4)
    for (i in 0..3) {
        eligibleEvents.add(false)
    }
    if (christmasDDayDiscount(date) != 0) {
        eligibleEvents[0] = true
    }
    if (isWeekday(date)) {
        eligibleEvents[1] = true
    }
    if (isWeekEnd(date)) {
        eligibleEvents[2] = true
    }
    if (isEligibleForSpecialDiscount(date)) {
        eligibleEvents[3] = true
    }
    return eligibleEvents
}

fun main() = try {
    println(
        "안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.\n" + "12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)"
    )
    val visitDayRaw = Console.readLine()
    val visitDay = parseVisitDate(visitDayRaw)
    require(isValidDate(visitDay)) { "[ERROR] 유효하지 않은 날짜입니다. 다시 입력해 주세요." }
    val visitDate = LocalDate.of(2023, 12, visitDay)
    println(
        "주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)"
    )
    val orderLine = Console.readLine()
    val order = parseOrder(orderLine)
    require(isValidOrder(order)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
    println(
        "12월 ${visitDay}일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n"
    )
    println("<주문 메뉴>")
    val categoryQuantity = categoryQuantityCount(order)
    var totalPrice = 0
    for ((item, quantity) in order) {
        println("$item ${quantity}개")
        totalPrice = quantity * (foodPrice.getOrDefault(item, 0))
    }
    require(isOrderConsistOfOnlyDrink(categoryQuantity)) { "[ERROR] 유효하지 않은 주문입니다. 다시 입력해 주세요." }
    val discountList = eligibleEvent(visitDate)
    if (categoryQuantity[foodCategory.DESSERT] == 0) {
        discountList[1] = false
    }
    if (categoryQuantity[foodCategory.MAIN] == 0) {
        discountList[2] = false
    }
    if (totalPrice < 10_000) {
        discountList[0] = false
        discountList[1] = false
        discountList[2] = false
        discountList[3] = false
    }
} catch (e: IllegalArgumentException) {
    println(e.message)
}
