package chapter.three

import io.reactivex.Observable

class RxJavaOperator {
    /**
     * filter(): only lets pass the items
     * that satisfy the given predicate
     */
    fun `filter operator`() {

        // Creating observable
        val originalObs = Observable
            .range(1, 20)

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
    }

}