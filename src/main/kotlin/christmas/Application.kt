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

fun main() {
    TODO("프로그램 구현")
}
