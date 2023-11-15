package christmas

import java.text.DecimalFormat
import java.time.LocalDate

class OutputView {
    fun printMeritSummary(discount: Discount, order: Order, visitDate: LocalDate) {
        val (totalDiscount, additionalDiscount) = discount.calculateTotalDiscount()
        val totalPrice = order.totalPrice
        val visitDay = visitDate.dayOfMonth
        println("12월 ${visitDay}일에 우테코 식당에서 받을 이벤트 혜택 미리 보기!\n")
        printOrder(order)
        printTotalPriceBeforeDiscount(order)
        printGiveAwayItem(order)
        printDiscount(discount)
        printTotalDiscountAmount(totalDiscount, additionalDiscount)
        printNetAmount(totalPrice, totalDiscount)
        printEventBadge(totalDiscount, additionalDiscount)
    }

    private fun formatNumber(number: Int): String {
        return DecimalFormat("#,###").format(number)
    }

    private fun printOrder(order: Order) {
        println("<주문 메뉴>")
        for ((item, quantity) in order.items) {
            println("${item.name} ${quantity}개")
        }
    }

    private fun printTotalPriceBeforeDiscount(order: Order) {
        println("<할인 전 총주문 금액>")
        println("${formatNumber(order.totalPrice)}원\n")
    }

    private fun printGiveAwayItem(order: Order) {
        println("<증정 메뉴>")
        if (isEligibleForFreeChampagne(order)) {
            println("샴페인 1개\n")
        } else {
            println("없음\n")
        }
    }

    private fun printDiscount(discount: Discount) {
        println("<혜택 내역>")
        val eligibleEvents = discount.eligibleEvents
        if (eligibleEvents.isEmpty()) {
            println("없음")
            return
        }
        val discountMessages: Map<DiscountEvent, String> = mapOf(
            DiscountEvent.DDAY_EVENT to "크리스마스 디데이 할인: ${formatNumber(eligibleEvents.getValue(DiscountEvent.DDAY_EVENT))}원",
            DiscountEvent.WEEKDAY_EVENT to "평일 할인: ${formatNumber(eligibleEvents.getValue(DiscountEvent.WEEKDAY_EVENT))}원",
            DiscountEvent.WEEKEND_EVENT to "주말 할인: ${formatNumber(eligibleEvents.getValue(DiscountEvent.WEEKEND_EVENT))}원",
            DiscountEvent.SPECIAL_EVENT to "특별 할인: ${formatNumber(eligibleEvents.getValue(DiscountEvent.SPECIAL_EVENT))}원",
            DiscountEvent.FREE_CHAMPAGNE to "증정 이벤트: ${formatNumber(eligibleEvents.getValue(DiscountEvent.FREE_CHAMPAGNE))}원"
        )
        discount.eligibleEvents.forEach { (event, _) -> println(discountMessages[event]) }
    }

    private fun printTotalDiscountAmount(totalDiscount: Int, additionalDiscount: Int) {
        println("<총혜택 금액>")
        println("${formatNumber(totalDiscount + additionalDiscount)}원\n")
    }

    private fun printNetAmount(totalPrice: Int, totalDiscount: Int) {
        println("<할인 후 예상 결제 금액>")
        println("${formatNumber(totalPrice - totalDiscount)}원\n")
    }

    private fun printEventBadge(totalDiscount: Int, additionalDiscount: Int) {
        println("<12월 이벤트 배지>")
        println(eventBadge(totalDiscount + additionalDiscount).badgeName)
    }
}
