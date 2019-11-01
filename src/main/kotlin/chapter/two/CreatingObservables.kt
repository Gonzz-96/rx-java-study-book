package chapter.two

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe

class CreatingObservables {

    /**
     * Observable.just(value):
     * Creates an obserable instance that emits exactly one value to all
     * future subscribers and completes afterwards. Overloaded versions
     * of the just() operator can take anything teo to nine values to be emitted.
     */
    fun justOperator() {
        val o: Observable<String> = Observable.just("This is just() operator\n")

        o.subscribe {
            println(it)
        }
    }

    /**
     * Observable.from(iterable)
     * Similar to just(), but accepts Iterable<T> or T[]. thus
     * creating Observable<T> with as many values emitted as
     * elements in values collection. Another overloaded version accepts
     * a FUture<T>, emitting an event when the underlying FUture compeltes.
     */
    fun fromOperator() {

        val o: Observable<Int> = Observable.fromIterable(listOf(1, 2, 3, 5, 7, 11))

        o.subscribe {
            println("From iterable: $it")
        }
    }

    /**
     * Observable.range(from, n)
     * Produces n integer numbers starting from from. For example, (5, 3)
     * will produce 5, 6 and 7.
     */
    fun rangeOperator() {

        val o: Observable<Int> = Observable.range(5, 3)

        o.subscribe {
            print("\nRange: $it")
        }
    }

    /**
     * Observable.empty()
     * Completes immediately after subscription, without emitting
     * any values.
     */

    /**
     * Observable.empty()
     * Never emits notifications, neither values nor completion or error.
     * This stream is useful for testing purposes.
     */

    /**
     * Observable.error()
     * Emits an onError() notification immediately to every
     * subscriber. No other values are emmitted accodring to contract
     * onComplete() cannot occur as well.
     */
}