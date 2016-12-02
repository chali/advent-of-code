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