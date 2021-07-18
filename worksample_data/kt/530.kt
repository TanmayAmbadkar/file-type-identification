package dev.minutest.internal

import dev.minutest.*
import dev.minutest.experimental.transformedBy

/**
 * Internal implementation of [TestContextBuilder] which hides the details and the [NodeBuilder]ness.
 */
internal data class MinutestContextBuilder<PF, F>(
    val name: String,
    private val parentFixtureType: FixtureType<PF>,
    private val fixtureType: FixtureType<F>,
    private var fixtureFactory: FixtureFactory<PF, F>,
    override var autoFixture: Boolean = true,
    private val children: MutableList<NodeBuilder<F>> = mutableListOf(),
    private val befores: MutableList<(F, TestDescriptor) -> F> = mutableListOf(),
    private val afters: MutableList<(FixtureValue<F>, TestDescriptor) -> Unit> = mutableListOf(),
    private val afterAlls: MutableList<() -> Unit> = mutableListOf(),
    private val markers: MutableList<Any> = mutableListOf(),
    private val transforms: MutableList<NodeTransform<PF>> = mutableListOf()
) : TestContextBuilder<PF, F>(), NodeBuilder<PF> {

    override fun fixture(factory: Unit.(testDescriptor: TestDescriptor) -> F) {
        if (fixtureFactory is ExplicitFixtureFactory)
            throw IllegalStateException("Fixture already set in context \"$name\"")
        fixtureFactory = ExplicitFixtureFactory(parentFixtureType, fixtureType) { _, testDescriptor ->
            Unit.factory(testDescriptor)
        }
    }

    override fun deriveFixture(f: (PF).(TestDescriptor) -> F) {
        if (fixtureFactory is ExplicitFixtureFactory)
            throw IllegalStateException("Fixture already set in context \"$name\"")
        if (fixtureFactory.outputType.isSubtypeOf(parentFixtureType))
            fixtureFactory = ExplicitFixtureFactory(parentFixtureType, fixtureType, f)
        else
            error("You can't deriveFixture in context \"$name\" because the parent context has no fixture")
    }

    override fun before(operation: F.(TestDescriptor) -> Unit) {
        before_ { testDescriptor ->
            this.operation(testDescriptor)
            this
        }
    }

    override fun before_(f: F.(TestDescriptor) -> F) {
        befores.add(f)
    }

    override fun after(operation: F.(TestDescriptor) -> Unit) {
        afters.add { result, testDescriptor -> result.value.operation(testDescriptor) }
    }

    override fun after2(operation: FixtureValue<F>.(TestDescriptor) -> Unit) {
        afters.add(operation)
    }

    override fun test_(name: String, f: F.(TestDescriptor) -> F): NodeBuilder<F> =
        addChild(TestBuilder(name, f))

    override fun context(name: String, block: TestContextBuilder<F, F>.() -> Unit) =
        newContext(
            name,
            fixtureType,
            if (fixtureFactory.isCompatibleWith(fixtureType, fixtureType))
                IdFixtureFactory(fixtureType)
            else
                UnsafeFixtureFactory(fixtureFactory.outputType),
            block)

    override fun <G> internalDerivedContext(
        name: String,
        newFixtureType: FixtureType<G>,
        block: TestContextBuilder<F, G>.() -> Unit
    ): NodeBuilder<F> = newContext(
        name,
        newFixtureType,
        UnsafeFixtureFactory(fixtureType),
        block
    )

    override fun addMarker(marker: Any) {
        markers.add(marker)
    }

    override fun addTransform(transform: NodeTransform<PF>) {
        transforms.add(transform)
    }

    private fun <G> newContext(
        name: String,
        newFixtureType: FixtureType<G>,
        fixtureFactory: FixtureFactory<F, G>,
        block: TestContextBuilder<F, G>.() -> Unit
    ): NodeBuilder<F> = addChild(
        LateContextBuilder(
            name,
            this.fixtureType,
            newFixtureType,
            fixtureFactory,
            autoFixture,
            block
        )
    )

    private fun <T : NodeBuilder<F>> addChild(child: T): T {
        children.add(child)
        return child
    }

    override fun afterAll(f: () -> Unit) {
        afterAlls.add(f)
    }

    override fun buildNode(): Node<PF> {
        return PreparedContext(
            name,
            children.map { it.buildNode() },
            markers,
            parentFixtureType,
            fixtureType,
            befores,
            afters,
            afterAlls,
            checkedFixtureFactory()
        ).transformedBy(transforms)
    }

    private fun checkedFixtureFactory(): (PF, TestDescriptor) -> F = when {
        // broken out for debugging
        fixtureFactory is ExplicitFixtureFactory ->
            fixtureFactory
        fixtureFactory.isCompatibleWith(parentFixtureType, fixtureType) ->
            fixtureFactory
        thisContextDoesntReferenceTheFixture() ->
            fixtureFactory
        autoFixture ->
            automaticFixtureFactory() ?: error("Cannot automatically create fixture in context \"$name\"")
        else ->
            error("Fixture has not been set in context \"$name\"")
    }

    private fun thisContextDoesntReferenceTheFixture() =
        befores.isEmpty() && afters.isEmpty() && !children.any { it is TestBuilder<F> || it.isDerivedContext()}

    @Suppress("UNCHECKED_CAST")
    private fun automaticFixtureFactory() =
        this.fixtureType.creator()?.let { creator ->
            { _: PF, _: TestDescriptor ->
                creator()
            }
        }

    private fun <F> NodeBuilder<F>.isDerivedContext() =
        this is MinutestContextBuilder<F, *> && this.fixtureType != this.parentFixtureType ||
            this is LateContextBuilder<F, *> && this.delegate.fixtureType != this.delegate.parentFixtureType
}