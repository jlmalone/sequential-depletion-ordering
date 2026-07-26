#!/usr/bin/env kotlin

/**
 * Numerical checks for the Sequential Depletion Ordering manuscript.
 *
 * These checks support transcription and debugging only; the paper contains
 * the proofs.
 *
 * Run with:
 *   kotlin sequential_depletion_verification.main.kts
 */

import java.util.Collections
import java.util.Locale
import java.util.Random
import kotlin.math.abs
import kotlin.math.ln
import kotlin.math.ln1p
import kotlin.math.max
import kotlin.math.pow

fun cost(order: List<Double>, q: Double): Double {
    var remaining = q + order.sum()
    var total = 0.0
    for (x in order) {
        total += x / remaining
        remaining -= x
    }
    return total
}

fun logShocks(order: List<Double>, q: Double): List<Double> {
    require(q > 0.0) { "q must be positive for log shocks" }
    var remaining = q + order.sum()
    return buildList {
        for (x in order) {
            val after = remaining - x
            add(ln(remaining / after))
            remaining = after
        }
    }
}

fun isMajorizedBy(
    x: List<Double>,
    y: List<Double>,
    tolerance: Double = 1e-10,
): Boolean {
    require(x.size == y.size) { "majorization vectors must have equal length" }
    val xs = x.sortedDescending()
    val ys = y.sortedDescending()
    if (abs(xs.sum() - ys.sum()) > tolerance) {
        return false
    }

    var xPrefix = 0.0
    var yPrefix = 0.0
    for (index in 0 until xs.lastIndex) {
        xPrefix += xs[index]
        yPrefix += ys[index]
        if (xPrefix > yPrefix + tolerance) {
            return false
        }
    }
    return true
}

fun swapIdentity(a: Double, b: Double, tail: Double): Pair<Double, Double> {
    val lhs =
        a / (tail + a + b) +
            b / (tail + b) -
            b / (tail + a + b) -
            a / (tail + a)
    val rhs =
        a * b * (a - b) /
            ((tail + a + b) * (tail + a) * (tail + b))
    return lhs to rhs
}

fun isClose(
    left: Double,
    right: Double,
    relativeTolerance: Double = 1e-11,
    absoluteTolerance: Double = 1e-11,
): Boolean =
    abs(left - right) <=
        max(absoluteTolerance, relativeTolerance * max(abs(left), abs(right)))

fun shuffledCopy(values: List<Double>, random: Random): List<Double> =
    values.toMutableList().also { Collections.shuffle(it, random) }

fun checkRandomDeterministic(trials: Int = 2_000) {
    val random = Random(20260724L)
    repeat(trials) {
        val n = random.nextInt(7) + 2
        val xs = List(n) { 10.0.pow(-1.0 + 2.0 * random.nextDouble()) }
        val q = 10.0.pow(-1.0 + 2.0 * random.nextDouble())

        val firstIndex = random.nextInt(n)
        var secondIndex = random.nextInt(n)
        while (secondIndex == firstIndex) {
            secondIndex = random.nextInt(n)
        }
        val tail = 10.0.pow(-1.0 + 2.0 * random.nextDouble())
        val (lhs, rhs) = swapIdentity(xs[firstIndex], xs[secondIndex], tail)
        check(isClose(lhs, rhs)) {
            "adjacent-swap identity mismatch: lhs=$lhs rhs=$rhs"
        }

        val ascending = xs.sorted()
        val descending = xs.sortedDescending()
        val permutation = shuffledCopy(xs, random)
        check(cost(ascending, q) <= cost(permutation, q) + 1e-11)
        check(cost(permutation, q) <= cost(descending, q) + 1e-11)

        val ascendingShocks = logShocks(ascending, q)
        val descendingShocks = logShocks(descending, q)
        val permutationShocks = logShocks(permutation, q)
        check(isMajorizedBy(descendingShocks, permutationShocks))
        check(isMajorizedBy(permutationShocks, ascendingShocks))
    }
}

fun format8(value: Double): String =
    String.format(Locale.US, "%.8f", value)

fun precedenceCounterexample() {
    val q = 1.0
    val greedy = cost(listOf(7.0, 8.0, 2.0), q) // B, A, C
    val better = cost(listOf(8.0, 2.0, 7.0), q) // A, C, B
    check(better < greedy)
    println(
        "Precedence example: greedy=${format8(greedy)}, " +
            "better=${format8(better)}",
    )
}

fun networkDelta(
    a: List<Double>,
    b: List<Double>,
    tail: List<Double>,
    weights: List<Double> = List(a.size) { 1.0 },
): Double {
    require(a.size == b.size && b.size == tail.size && tail.size == weights.size) {
        "network vectors and weights must have equal length"
    }
    return a.indices.sumOf { index ->
        val ar = a[index]
        val br = b[index]
        val tr = tail[index]
        weights[index] * ar * br * (ar - br) /
            ((tr + ar + br) * (tr + ar) * (tr + br))
    }
}

fun networkContextReversal() {
    val a = listOf(4.0, 1.0)
    val b = listOf(1.0, 3.0)
    val first = networkDelta(a, b, listOf(1.0, 1.0))
    val second = networkDelta(a, b, listOf(10.0, 1.0))
    check(first > 0.0 && second < 0.0)
    println(
        "Network reversal: delta(1,1)=${format8(first)}, " +
            "delta(10,1)=${format8(second)}",
    )
}

fun monteCarloExpectedCost(
    rates: List<Double>,
    order: List<Int>,
    q: Double,
    samples: Int,
    seed: Long,
): Double {
    val random = Random(seed)
    var total = 0.0
    repeat(samples) {
        val sizes = rates.map { rate -> -ln1p(-random.nextDouble()) / rate }
        total += cost(order.map(sizes::get), q)
    }
    return total / samples
}

fun stochasticExponentialCheck(samples: Int = 300_000) {
    // A larger exponential rate is smaller in likelihood-ratio order.
    val rates = listOf(5.0, 2.0, 0.8)
    val likelihoodRatioOrder = listOf(0, 1, 2)
    val reverseOrder = likelihoodRatioOrder.reversed()
    val q = 1.0
    val best =
        monteCarloExpectedCost(
            rates,
            likelihoodRatioOrder,
            q,
            samples,
            seed = 11L,
        )
    val worst =
        monteCarloExpectedCost(
            rates,
            reverseOrder,
            q,
            samples,
            seed = 12L,
        )
    check(best < worst)
    println(
        "Exponential MC: LR-order=${format8(best)}, " +
            "reverse=${format8(worst)}",
    )
}

fun <T> permutations(values: List<T>): Sequence<List<T>> =
    sequence {
        if (values.isEmpty()) {
            yield(emptyList())
        } else {
            for (index in values.indices) {
                val head = values[index]
                val tail = values.toMutableList().also { it.removeAt(index) }
                for (suffix in permutations(tail)) {
                    yield(listOf(head) + suffix)
                }
            }
        }
    }

fun exhaustiveSmallInstance() {
    val xs = listOf(1.0, 2.0, 4.0, 9.0)
    val q = 1.5
    val scored =
        permutations(xs)
            .map { order -> cost(order, q) to order }
            .sortedBy(Pair<Double, List<Double>>::first)
            .toList()
    check(scored.first().second == xs.sorted())
    check(scored.last().second == xs.sortedDescending())
    println(
        "Exhaustive 4-item min=${format8(scored.first().first)}, " +
            "max=${format8(scored.last().first)}",
    )
}

checkRandomDeterministic()
exhaustiveSmallInstance()
precedenceCounterexample()
networkContextReversal()
stochasticExponentialCheck()
println("All verification checks passed.")
