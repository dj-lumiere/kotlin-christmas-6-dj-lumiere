package christmas

class Order {
    val items: MutableMap<Food, Int> = mutableMapOf<Food, Int>().withDefault { 0 }
    val itemsPerCategory: MutableMap<FoodCategory, Int> = mutableMapOf<FoodCategory, Int>().withDefault { 0 }
    var totalQuantity: Int = 0
    var totalPrice: Int = 0
    var nonDrinkQuantity: Int = 0
    fun addItem(menuItem: Food, quantity: Int) {
        require(!items.containsKey(menuItem)) { ErrorMessage.INVALID_ORDER }
        require(quantity > 0) { ErrorMessage.INVALID_ORDER }
        items[menuItem] = items.getValue(menuItem) + quantity
        totalQuantity += quantity
        totalPrice += quantity * menuItem.price
        require(totalQuantity <= OrderConstraints.MAXIMUM_FOOD_PER_ORDER) { ErrorMessage.INVALID_ORDER }
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
