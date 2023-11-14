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
        printDiscount(discount, order, visitDate)
        printTotalDiscountAmount(totalDiscount, additionalDiscount)
        printNetAmount(totalPrice, totalDiscount)
        printEventBadge(totalDiscount, additionalDiscount)
    }

    private fun printOrder(order: Order) {
        println("<주문 메뉴>")
        for ((item, quantity) in order.items) {
            println("${item.name} ${quantity}개")
        }
    }

    private fun printTotalPriceBeforeDiscount(order: Order) {
        println("<할인 전 총주문 금액>")
        println("${DecimalFormat("#,###").format(order.totalPrice)}원\n")
    }

    private fun printGiveAwayItem(order: Order) {
        println("<증정 메뉴>")
        if (isEligibleForFreeChampagne(order)) {
            println("샴페인 1개\n")
        } else {
            println("없음\n")
        }
    }

    private fun printDiscount(discount: Discount, order: Order, visitDate: LocalDate) {
        println("<혜택 내역>")
        if (discount.eligibleEvents.isEmpty()) {
            println("없음")
            return
        }
        val discountMessages = mapOf(
            DiscountEvent.CHRISTMAS_DDAY_EVENT to "크리스마스 디데이 할인: ${
                DecimalFormat("#,###").format(
                    -christmasDDayDiscount(
                        visitDate,
                        order
                    )
                )
            }원",
            DiscountEvent.WEEKDAY_EVENT to "평일 할인: ${
                DecimalFormat("#,###").format(
                    -EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.DESSERT)
                )
            }원",
            DiscountEvent.WEEKEND_EVENT to "주말 할인: ${
                DecimalFormat("#,###").format(
                    -EventConstraints.DISCOUNT_PER_ITEM * order.itemsPerCategory.getValue(FoodCategory.MAIN)
                )
            }원",
            DiscountEvent.SPECIAL_EVENT to "특별 할인: ${DecimalFormat("#,###").format(-1000)}원",
            DiscountEvent.FREE_CHAMPAGNE to "증정 이벤트: -25,000원"
        )
        discount.eligibleEvents.forEach { event -> println(discountMessages[event]) }
    }

    private fun printTotalDiscountAmount(totalDiscount: Int, additionalDiscount: Int) {
        println("<총혜택 금액>")
        println("${DecimalFormat("#,###").format(totalDiscount + additionalDiscount)}원\n")
    }

    private fun printNetAmount(totalPrice: Int, totalDiscount: Int) {
        println("<할인 후 예상 결제 금액>")
        println("${DecimalFormat("#,###").format(totalPrice - totalDiscount)}원\n")
    }

    private fun printEventBadge(totalDiscount: Int, additionalDiscount: Int) {
        println("<12월 이벤트 배지>")
        println(eventBadge(totalDiscount + additionalDiscount).badgeName)
    }
}
