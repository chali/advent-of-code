package cz.chali.advent


fun <T, R> Iterable<T>.scan(initial: R, operation: (previous: R, element: T) -> R): List<R> {
    var previous = initial
    val result = mutableListOf(initial)
    for (element in this) {
        val next = operation(previous, element)
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

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val size = this.first().size
    if (this.any { it.size != size })
        throw IllegalArgumentException("Matrix must be rectangle")
    return (0 .. size - 1).map { x ->
        this.indices.map { this[it][x] }
    }
}

fun <T> List<T>.sliding(windowSize: Int): List<List<T>> {
    return this.dropLast(windowSize - 1).mapIndexed { i, s -> this.subList(i, i + windowSize) }
}