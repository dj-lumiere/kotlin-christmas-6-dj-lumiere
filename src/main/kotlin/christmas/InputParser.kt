package christmas


fun parseVisitDate(input: String): Int {
    val visitDay = input.toIntOrNull() ?: throw IllegalArgumentException(ErrorMessage.INVALID_DATE)
    require((EventConstraints.DECEMBER_START <= visitDay) and (visitDay <= EventConstraints.DECEMBER_END)) { ErrorMessage.INVALID_DATE }
    return visitDay
}

fun parseOrder(input: String, restaurantMenu: RestaurantMenu): Order {
    val order = Order()
    val menus: List<String> = input.split(",")
    for (menu in menus) {
        val (itemName, quantity) = menu.split("-")
        require(restaurantMenu.hasItem(itemName)) { ErrorMessage.INVALID_ORDER }
        val item = restaurantMenu.getItem(itemName)
        order.addItem(
            item, quantity.toIntOrNull() ?: throw IllegalArgumentException(ErrorMessage.INVALID_ORDER)
        )
    }
    require(!order.consistsOfOnlyDrinks()) { ErrorMessage.INVALID_ORDER }
    return order
}