package cz.chali.advent


fun <T, R> Iterable<T>.scan(initial: R, operation: (element: T, previous: R) -> R): List<R> {
    var previous = initial
    val result = mutableListOf(initial)
    for (element in this) {
        val next = operation(element, previous)
        result.add(next)
        previous = next
    }
    return result
}

fun <T> Iterable<T>.combinations(groupSize: Int): Iterable<List<T>> {
    fun comb(elements: Iterable<T>, size: Int): Iterable<List<T>> {
        if (size == 1)
            return elements.map { listOf(it) }
        else {
            return elements.mapIndexed { index, element ->
                comb(elements.drop(index + 1), size - 1).map { it.plusElement(element) }
            }.flatten()
        }
    }
    return comb(this, groupSize)
}