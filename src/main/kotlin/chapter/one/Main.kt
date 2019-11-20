package chapter.one

/**
 * Main function
 */
fun main(args: Array<String>) {

    // Async versus Syncs section
    val o = SynchronousObservables()

    o.synchronousObservable()
    o.inMemoryData()
    o.synchronousComputation()

    // Concurrency and Parallelism section
    val c = ConcurrencyAndParallelism()

    c.noConcurrencyInObservable()
    c.asynchronousObservables()

    // Lazy versus Eager section
    val l = LazyVersusEager()

    l.observableIsLazy()

    // Single
    val s = SingleClass()

    s.single()
}
