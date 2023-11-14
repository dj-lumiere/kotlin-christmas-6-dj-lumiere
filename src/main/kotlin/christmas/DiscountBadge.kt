package christmas

enum class DiscountBadge(val badgeName: String) {
    SANTA("산타"), TREE("트리"), STAR("별"), NONE("없음")
}

fun eventBadge(totalDiscount: Int): DiscountBadge {
    if (totalDiscount >= EventConstraints.SANTA_BADGE_THRESHOLD) {
        return DiscountBadge.SANTA
    }
    if (totalDiscount >= EventConstraints.TREE_BADGE_THRESHOLD) {
        return DiscountBadge.TREE
    }
    if (totalDiscount >= EventConstraints.STAR_BADGE_THRESHOLD) {
        return DiscountBadge.STAR
    }
    return DiscountBadge.NONE
}