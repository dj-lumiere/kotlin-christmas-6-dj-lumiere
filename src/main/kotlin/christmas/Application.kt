package christmas

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

fun main() {
    TODO("프로그램 구현")
}
