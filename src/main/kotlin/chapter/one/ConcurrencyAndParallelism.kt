package chapter.one

import chapter.utils.addSpace
import io.reactivex.Observable

class ConcurrencyAndParallelism {

    /**
     * The contract of an Observable is that the events
     * of a Subscriber (onNext, onComplete, onError) are
     * sequential. There will never be concurrency, and every
     * event should be run in the same thread.
     */
    fun noConcurrencyInObservable() {

        // This is okay :)
        val i: Observable<String> = Observable.create { s ->
            Thread {
                s.onNext("one")
                s.onNext("two")
                s.onNext("three")
                s.onNext("four")
                s.onComplete()
            }.start()
        }

        i.subscribe {
            val thread = Thread.currentThread()
            print("\nThis is okay: $it - Thread: $thread")
        }

        // Please... don't (This is NOT okay)
        val d: Observable<String> = Observable.create { s ->
            Thread {
                s.onNext("one")
                s.onNext("two")
            }.start()

            Thread {
                s.onNext("three")
                s.onNext("four")
            }.start()

            // Ignoring need to emit s.onComplete() due to race of threads
        }

        d.subscribe {
            val thread = Thread.currentThread()
            print("\nThis is NOT okay: $it - Thread: $thread")
        }

        addSpace()
    }

    /**
     * Concurrency/parallelism issues can be solved using
     * composition. An Observable stream is always serialized,
     * but each Observable stream can be operate independently
     * of one another.This can be achieved with 'merge' function
     * or 'flatMap'
     */
    fun asynchronousObservables() {
        val a: Observable<String> = Observable.create { s ->
            Thread {
                s.onNext("one")
                s.onNext("two")
                s.onComplete()
            }.start()
        }

        val b: Observable<String> = Observable.create { s ->
            Thread {
                s.onNext("three")
                s.onNext("four")
                s.onComplete()
            }.start()
        }

        // This ubscribes to a and b concurrently,
        // and merges into a this sequential stream
        val c: Observable<String> = Observable.merge(a, b)

        c.subscribe {
            val thread = Thread.currentThread()
            print("\nAsync: $it - Thread: $thread")
        }

        addSpace()
    }
}