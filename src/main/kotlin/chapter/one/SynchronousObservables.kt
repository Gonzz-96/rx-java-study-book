package chapter.one

import io.reactivex.Observable

class SynchronousObservables {

    private val list = listOf(1, 2, 3)

    /**
     * PAGE: 6
     * RxJava will never use concurrency at least
     * it is asked to so. This function is run in
     * subscribing thread (MAIN).
     */
    fun synchronousObservable() {
        val i: Observable<String> = Observable.create { s ->
            s.onNext("Hello, world!")
            s.onComplete()
        }

        i.subscribe {
            val thread = Thread.currentThread()
            println("$it - Thread: $thread")
        }
    }

    /**
     * PAGE: 7
     * For in-memory access, it is not needed to request
     * the data asynchronously. This function is run in
     * subscribing thread (MAIN)
     */
    fun inMemoryData() {
        val i: Observable<Int> = Observable.create { s ->
            s.onNext(list[1])
            s.onComplete()
        }

        i.subscribe {
            val thread = Thread.currentThread()
            println("\n$it - Thread: $thread\n")
        }
    }

    /**
     * PAGE: 7
     * RxJava uses operator to manipulate, combine and transform
     * data. Some actions are: 'map', 'filter', 'take', 'flatMap',
     * and 'groupBy'. These operators are run synchronously because
     * of performance reasons.
     */
    fun synchronousComputation() {

        val i: Observable<Int> = Observable.create { s ->
            s.onNext(1)
            s.onNext(2)
            s.onNext(3)
            s.onComplete()
        }

        i.map { "Number $it" }
            .subscribe {
                val thread = Thread.currentThread()
                println("$it - Thread: $thread")
            }
    }
}
