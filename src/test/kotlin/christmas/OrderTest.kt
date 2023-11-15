package christmas

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class OrderTest{
    @Test
    fun `중복된 메뉴를 넣을 때 에러 발생`(){
        val menuitem = RestaurantMenu().menuItems
        val order = Order()
        assertDoesNotThrow { order.addItem(menuitem.getValue("양송이수프"), 1) }
        assertThrows<IllegalArgumentException> { order.addItem(menuitem.getValue("양송이수프"), 1) }
    }

    @Test
    fun `잘못된 수량을 넣을 때 에러 발생`(){
        val menuitem = RestaurantMenu().menuItems
        val order = Order()
        assertDoesNotThrow { order.addItem(menuitem.getValue("양송이수프"), 1) }
        assertThrows<IllegalArgumentException> { order.addItem(menuitem.getValue("양송이수프"), -1) }
    }

    @Test
    fun `너무 많은 양을 넣을 때 에러 발생`(){
        val menuitem = RestaurantMenu().menuItems
        val order = Order()
        assertThrows<IllegalArgumentException> { order.addItem(menuitem.getValue("양송이수프"), 21) }
    }
}
