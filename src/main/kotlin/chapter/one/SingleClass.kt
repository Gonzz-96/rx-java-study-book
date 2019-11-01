package chapter.one
import chapter.utils.addSpace
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class SingleClass {

    /**
     * PAGE 18
     *
     * Single class is like Future, but lazy, cab be
     * subscribed multiple times. It interacts
     * with Observable
     */
    fun single() {
        val a : Single<String> =
            Single.just("Data A").subscribeOn(Schedulers.io())

        val b : Single<String> =
            Single.just("Data").subscribeOn(Schedulers.io())

        val o: Observable<String> = a.mergeWith(b).toObservable()

        o.subscribe {
            println("${Thread.currentThread()
            } Single: $it")
        }

        addSpace()
    }
}