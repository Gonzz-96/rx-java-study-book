package chapter.three

import chapter.utils.addSpace
import io.reactivex.Observable
import io.reactivex.Observable.*
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Timed
import java.util.concurrent.TimeUnit

class RxJavaOperator {

    /**
     *  Creating principal observable
     *  This shows that the original stream will never
     *  be modified by the operators. It rests the same
     *  throughout the execution
     */
    private val originalObs = Observable.range(1, 10)

    /**
     * Another observable
     */
    private val anotherObs = Observable.range(10, 10)

    /**
     * filter(): only lets pass the items
     * that satisfy the given predicate
     */
    fun filterOperator() {

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
    fun mapOperator() {

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
    fun flatMapOperator() {

        val flatMap = originalObs.flatMap<String> {
            just("Flatten observable: $it")
        }

        flatMap.subscribe {
            println(it)
        }
    }

    fun flatMapPlusTimer() {

        originalObs
            .flatMap {
                timer(it.toLong(), TimeUnit.MILLISECONDS)
            }.subscribe {
                print("\nTimer: $it")
            }

        addSpace()
    }


    fun flatMapWithDelay() {

        originalObs
            .flatMap {
                just(it).delay(it.toLong(), TimeUnit.SECONDS)
            }.subscribe {
                print("\nTimer: $it")
            }

        // Every subscription will start at the same time
        Thread.sleep(11_000L)
    }


    /**
     * Example: for every day of the week, the load
     * time can be different. It may vary between 1 sec
     * and 10 seconds.
     */
    fun realCaseOfFlatMap() {

        Observable
            .just(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
            .flatMap(::getStreamOfDay)
            .subscribe(::println)

        Thread.sleep(9_000L)
        addSpace()
    }

    fun getStreamOfDay(today: DayOfWeek): Observable<String> =
        when(today) {
            DayOfWeek.MONDAY ->
                Observable
                    .interval(1, TimeUnit.SECONDS)
                    .take(5)
                    .map { i -> "Sunday: $i" }
            DayOfWeek.FRIDAY ->
                Observable
                    .interval(3, TimeUnit.SECONDS)
                    .take(5)
                    .map { i -> "Friday: $i" }
        }

    enum class DayOfWeek {
        MONDAY,
        FRIDAY
    }


    /**
     * concatMap(): is like flatMap, but all the observables
     * are subscribed sequentially: there is only one observable
     * running at a time. There is no concurrency
     */
    fun realCaseOfConcatMap() {

        Observable
            .just(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
            .concatMap(::getStreamOfDay)
            .subscribe(::println)

        Thread.sleep(9_000L)
        addSpace()
    }

    /**
     * There is an overloaded version of flatMap()
     * that controls the total numbers of concurrent subscriptions to inner streams
     */
    fun controllingTheConcurrencyOfFlatMap() {

        Observable
            .just(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
            .flatMap(::getStreamOfDay, 2)
            .subscribe(::println)

        Thread.sleep(9_000L)
        addSpace()
    }

    /**
     * merge(): this operator will transform a set of observable streams
     * into one only stream. It doesn't matter how long an observable
     * takes to emit its values, the observables will be subscribed
     * at the same time (concurrency)
     */
    fun usingMerge() {

        val newOriginalObs = originalObs.delay(1, TimeUnit.SECONDS)
        val newOtherObs = anotherObs.delay(2, TimeUnit.SECONDS)

        Observable.merge(newOriginalObs, newOtherObs).subscribe {
            println("Merged observable: $it")
        }

        addSpace()
        Thread.sleep(10_000L)
    }

    /**
     * zip(): the streams in here will be combined
     */
    fun usingZipAndZipWith() {

        Observable.zip(originalObs, anotherObs, BiFunction { x: Int, y: Int ->
            println("Zip! $x : $y")
        }).subscribe()

        originalObs.zipWith(anotherObs, BiFunction { x: Int, y: Int ->
            println("Zip! $x : $y")
        }).subscribe()
    }

    /**
     * CHess example
     */
    fun chessExample() {

        val oneToEight = range(1, 8)
        val ranks = oneToEight.map(Any::toString)
        val files = oneToEight
            .map { x -> 'a' + x - 1}
            .map { ascii -> ascii.toInt().toChar() }
            .map { ch -> ch.toString() }

        val squares = files
            .flatMap { file ->
                ranks.map { rank ->
                    file + rank
                }
            }

        squares.subscribe {
            println("Chess result: $it")
        }
    }


    /**
     * It's bold to suppose that two Observables will prodice
     * events with the same frequency.
     */
    fun synchronousObservables() {

        val red = Observable.interval(10, TimeUnit.MILLISECONDS)
        val green = Observable.interval(10, TimeUnit.MILLISECONDS)

        Observable.zip(
            red.timestamp(),
            green.timestamp(),
            BiFunction { x: Timed<Long>, y: Timed<Long> ->
                x.time() - y.time()
            }
        ).forEach(::println)

        Thread.sleep(10_000L)
    }


    /**
     * The difference between timers is bigger. This is error prone
     * and there can be some memory leaks.
     */
    fun asynchronousObservables() {

        val red = Observable.interval(10, TimeUnit.MILLISECONDS)
        val green = Observable.interval(11, TimeUnit.MILLISECONDS)

        Observable.zip(
            red.timestamp(),
            green.timestamp(),
            BiFunction { x: Timed<Long>, y: Timed<Long> ->
                x.time() - y.time()
            }
        ).forEach(::println)

        Thread.sleep(10_000L)
    }

    /**
     * combineLatest(): in a zip operation, combineLatest
     * can be helpful at the moment of combining two asynchronous
     * observables. What this will do is, for every event generated
     * by a strem, combine it with the LATEST event generated by the other
     * stream.
     */
    fun combineLatestOperator() {

        Observable.combineLatest(
            interval(17, TimeUnit.MILLISECONDS).map { x -> "S$x" },
            interval(10, TimeUnit.MILLISECONDS).map { y -> "F$y" },
            BiFunction { s: String, f: String -> "$s : $f"}
        ).forEach(::println)

        Thread.sleep(10_000L)
    }

    /**
     * withLatestFrom(): this is like combineLatest() but with a slight
     * difference: combineLatest will trigger every time something appears
     * in ANY stream. On the other side, withLatestFrom will trigger an event
     * every time something appears in a SPECIFIED stream.
     */
    fun withLatestFromOperator() {

        val fast = interval(10, TimeUnit.MILLISECONDS).map { "F$it" }
        val slow = interval(17, TimeUnit.MILLISECONDS).map { "S$it" }

        slow
            .withLatestFrom(fast, BiFunction { s: String, f: String -> "$s : $f"  })
            .forEach(::println)

        Thread.sleep(10_000L)
    }

    /**
     * startWith(): this provides some dummy events to one stream
     *
     */
    fun startWithOperator() {

        val fast = interval(10, TimeUnit.MILLISECONDS)
            .map { "F$it" }
            .delay(100, TimeUnit.MILLISECONDS)
            .startWith("FX")

        val slow = interval(17, TimeUnit.MILLISECONDS)
            .map { "S$it" }

        slow.withLatestFrom(fast, BiFunction { s: String, f: String ->
            "$s : $f"
        }).forEach(::println)

        Thread.sleep(10_000L)

        Observable
            .just(1, 2)
            .startWith(0)
            .subscribe(::println)
    }


    /**
     * amb(): this operator subscribes to all upstream Observables it controls
     * and waits for the very first item emitted. When one of the Observables emits
     * the first event, amb() discards all other streams.
     */
    fun ambOperator() {

        Observable.ambArray(
            stream(100L, 17L, "F"),
            stream(200L, 10L, "S")
        ).subscribe(::println)

        Thread.sleep(10_000L)
    }



    private fun stream(initialDelay: Long, interval: Long, name: String) : Observable<String> =
        Observable.interval(initialDelay, interval, TimeUnit.MILLISECONDS)
            .map { x -> name + x.toString() }
            .doOnSubscribe { println("Subscribed to $name") }
            .doOnTerminate { println("Unsubscribe from $name") }

}
