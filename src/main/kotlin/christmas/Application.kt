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

fun main() {
    TODO("프로그램 구현")
}
