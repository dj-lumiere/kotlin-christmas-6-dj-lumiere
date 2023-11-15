package christmas

import camp.nextstep.edu.missionutils.Console

class InputView {
    fun promptVisitDate(): Int {
        while (true) {
            try {
                println("안녕하세요! 우테코 식당 12월 이벤트 플래너입니다.")
                println("12월 중 식당 예상 방문 날짜는 언제인가요? (숫자만 입력해 주세요!)")
                val visitDayRaw = readVisitDate()
                val visitDay = parseVisitDate(visitDayRaw)
                return visitDay
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun promptOrder(restaurantMenu: RestaurantMenu): Order {
        while (true) {
            try {
                println("주문하실 메뉴를 메뉴와 개수를 알려 주세요. (e.g. 해산물파스타-2,레드와인-1,초코케이크-1)")
                val orderLine = readOrder()
                return parseOrder(orderLine, restaurantMenu)
            } catch (e: IllegalArgumentException) {
                println(e.message)
            }
        }
    }

    fun readVisitDate(): String {
        return Console.readLine()
    }

    fun readOrder(): String {
        return Console.readLine()
    }

}

