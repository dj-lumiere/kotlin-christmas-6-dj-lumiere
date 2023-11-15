package christmas

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate


class DiscountTest {

    @Test
    fun `주문 총 금액이 할인 대상 금액을 안 넘을 때 할인이 적용 안 되는지 확인하기`(){
        val menuitem = RestaurantMenu().menuItems
        val order = Order()
        order.addItem(menuitem.getValue("양송이수프"), 1)
        val date = LocalDate.of(2023, 12, 1)
        val discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.isEmpty(), true)
    }

    @Test
    fun `크리스마스 할인 금액 체크`(){
        val menuitem = RestaurantMenu().menuItems
        val order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 1)
        for (day in 1..25) {
            val date = LocalDate.of(2023, 12, day)
            val discount = Discount(order, date)
            discount.calculateTotalDiscount()
            assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.DDAY_EVENT), true)
            assertEquals(discount.eligibleEvents.getValue(DiscountEvent.DDAY_EVENT), 1000+(day-1)*100)
        }
        for (day in 26..31){
            val date = LocalDate.of(2023, 12, day)
            val discount = Discount(order, date)
            discount.calculateTotalDiscount()
            assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.DDAY_EVENT), false)
        }
    }

    @Test
    fun `평일에 디저트를 시키면 할인금액이 제대로 나오는지 확인하기`(){
        val menuitem = RestaurantMenu().menuItems
        val date = LocalDate.of(2023, 12, 4)
        var order = Order()
        order.addItem(menuitem.getValue("초코케이크"), 1)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.WEEKDAY_EVENT), 2_023)
        order = Order()
        order.addItem(menuitem.getValue("초코케이크"), 2)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.WEEKDAY_EVENT), 4_046)
    }

    @Test
    fun `평일에 디저트가 아닌 다른 메뉴를 시키면 할인금액이 0으로 나오는지 확인하기`(){
        val menuitem = RestaurantMenu().menuItems
        val date = LocalDate.of(2023, 12, 4)
        var order = Order()
        order.addItem(menuitem.getValue("시저샐러드"), 2)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), false)
        order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 2)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), false)
    }

    @Test
    fun `주말에 메인메뉴를 시키면 할인 금액이 맞게 나오는지 확인하기`(){
        val menuitem = RestaurantMenu().menuItems
        val date = LocalDate.of(2023, 12, 1)
        var order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 1)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKEND_EVENT), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.WEEKEND_EVENT), 2_023)
        order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 2)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKEND_EVENT), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.WEEKEND_EVENT), 4_046)
    }

    @Test
    fun `주말에 메인이 아닌 다른 메뉴를 시키면 할인 리스트에 주말 할인이 없는지 확인하기`(){
        val menuitem = RestaurantMenu().menuItems
        val date = LocalDate.of(2023, 12, 1)
        var order = Order()
        order.addItem(menuitem.getValue("시저샐러드"), 2)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), false)
        order = Order()
        order.addItem(menuitem.getValue("초코케이크"), 2)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.WEEKDAY_EVENT), false)
    }

    @Test
    fun `특별 할인 여부 확인`(){
        val menuitem = RestaurantMenu().menuItems
        var order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 1)
        var date = LocalDate.of(2023, 12, 3)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.SPECIAL_EVENT), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.SPECIAL_EVENT), 1_000)
        date = LocalDate.of(2023, 12, 4)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.SPECIAL_EVENT), false)
    }

    @Test
    fun `샴페인 제공 여부 확인`(){
        val menuitem = RestaurantMenu().menuItems
        var order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 13)
        var date = LocalDate.of(2023, 12, 3)
        var discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.FREE_CHAMPAGNE), true)
        assertEquals(discount.eligibleEvents.getValue(DiscountEvent.FREE_CHAMPAGNE), 25_000)
        order = Order()
        order.addItem(menuitem.getValue("티본스테이크"), 2)
        date = LocalDate.of(2023, 12, 4)
        discount = Discount(order, date)
        discount.calculateTotalDiscount()
        assertEquals(discount.eligibleEvents.containsKey(DiscountEvent.FREE_CHAMPAGNE), false)
    }
}