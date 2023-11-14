package christmas

import java.time.LocalDate

fun main() {
    val restaurantMenu = RestaurantMenu()
    val visitDay = InputView().promptVisitDate()
    val visitDate = LocalDate.of(2023, 12, visitDay)
    val order = InputView().promptOrder(restaurantMenu)
    val discount = Discount(order, visitDate)
    OutputView().printMeritSummary(discount, order, visitDate)
}
