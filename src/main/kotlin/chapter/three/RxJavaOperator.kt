package chapter.three

import chapter.utils.addSpace
import io.reactivex.Observable

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

}