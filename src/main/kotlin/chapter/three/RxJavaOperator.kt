package chapter.three

import chapter.utils.addSpace
import io.reactivex.Observable
import io.reactivex.Observable.*
import java.util.concurrent.TimeUnit

class RxJavaOperator {

    /**
     *  Creating principal observable
     *  This shows that the original stream will never
     *  be modified by the operators. It rests the same
     *  throughout the execution
     */
    private val originalObs = Observable
        .range(1, 10)

    /**
     * filter(): only lets pass the items
     * that satisfy the given predicate
     */
    fun `filter operator`() {

        // Only even numbers
        val evenObs = originalObs.filter { it % 2 == 0 }

        // Only odd nubers
        val oddObs = originalObs.filter { it % 2 != 0 }

        evenObs.subscribe {
            println("Even number: $it")
        }

        oddObs.subscribe {
            println("Odd number: $it")
        }

        addSpace()
    }


    /**
     * map(): applies a transformation to each and
     * every value from upstream.
     */
    fun `map operator`() {

        // Squared numbers
        val squaredNums = originalObs.map { it * it }

        squaredNums.subscribe {
            println("First map: $it")
        }

        // Cubed numebrs
        val cubedNums = originalObs.map { it * it * it }

        cubedNums.subscribe {
            println("Second map: $it")
        }

        addSpace()
    }

    /**
     * flatMap(): this take an upstream and, with every
     * emitted element, will create an observable.
     */
    fun `flatMap operator`() {

        val flatMap = originalObs.flatMap<String> {
            just("Flatten observable: $it")
        }

        flatMap.subscribe {
            println(it)
        }
    }

    fun `flatMap plus timer =D`() {

        originalObs
            .flatMap {
                timer(it.toLong(), TimeUnit.MILLISECONDS)
            }.subscribe {
                print("\nTimer: $it")
            }

        addSpace()
    }


    fun `flatMap with delay`() {

        originalObs
            .flatMap {
                just(it).delay(it.toLong(), TimeUnit.SECONDS)
            }.subscribe {
                print("\nTimer: $it")
            }

        // Every subscription will start at the same time
        Thread.sleep(6_000L)
    }
}