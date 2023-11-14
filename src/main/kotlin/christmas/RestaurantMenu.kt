package christmas

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
        return menuItems[name] ?: throw IllegalArgumentException(ErrorMessage.INVALID_ORDER)
    }

    fun hasItem(name: String): Boolean {
        return menuItems.containsKey(name)
    }
}