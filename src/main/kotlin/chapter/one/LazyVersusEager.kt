package chapter.one

import chapter.utils.addSpace
import io.reactivex.Observable

class LazyVersusEager {

    /**
     * RxJava Observable is lazy by definition. This means
     * that, despite the Observable object is created, the code
     * wrapped by it won't be executed until a observer subscribes
     * to it. This permits the reusability of Observables.
     */
    fun observableIsLazy() {

        val o: Observable<String> = Observable.create { s ->
            s.onNext("Someone has subscribed: I'll run!")
            s.onComplete()
        }

        o.subscribe{ println("\nSubscriber 1: $it")}
        o.subscribe{ println("Subscriber 2: $it")}
    }
}
