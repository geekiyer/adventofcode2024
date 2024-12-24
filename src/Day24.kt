private enum class Gate { AND, OR, XOR, INPUT }
private data class Wire(var name: String, var value: Boolean?, val gate: Gate, val input1: String?, val input2: String?)
private sealed class Circuit {
    data class MapCircuit(val wires: MutableMap<String, Wire>) : Circuit()
    data class ListCircuit(val wires: MutableList<Wire>) : Circuit()
}

fun main() {
    val input = readInputChar("Day24")
    println("Part 1: ${calculateDecimalNumber(input)}")
    println("Part 2: ${sortAndSwap(input)}")
}

private fun Circuit.run(): String {
    val wires = when (this) {
        is Circuit.MapCircuit -> this.wires.values
        is Circuit.ListCircuit -> this.wires
    }
    return wires
        .filter { Regex("""z\d+""").matches(it.name) }
        .sortedByDescending { it.name }
        .map { if (resolveWireValue(this, it)) 1 else 0 }
        .joinToString("")
        .toLong(2)
        .toString()
}

private fun parseCircuit(input: String, isMap: Boolean): Circuit {
    val wires = mutableListOf<Wire>()

    Regex("""(\w+): (\d)""").findAll(input).forEach {
        val (name, value) = it.destructured
        wires.add(Wire(name, value == "1", Gate.INPUT, null, null))
    }

    Regex("""(\w+) (AND|OR|XOR) (\w+) -> (\w+)""").findAll(input).forEach {
        val (input1, gate, input2, output) = it.destructured
        wires.add(Wire(output, null, Gate.valueOf(gate), input1, input2))
    }

    return if (isMap) Circuit.MapCircuit(wires.associateBy { it.name }.toMutableMap()) else Circuit.ListCircuit(wires)
}

private fun resolveWireValue(circuit: Circuit, wire: Wire): Boolean {
    if (wire.value != null) return wire.value!!

    val value = when (wire.gate) {
        Gate.INPUT -> wire.value!!
        Gate.AND -> resolveInputValue(circuit, wire.input1) and resolveInputValue(circuit, wire.input2)
        Gate.OR -> resolveInputValue(circuit, wire.input1) or resolveInputValue(circuit, wire.input2)
        Gate.XOR -> resolveInputValue(circuit, wire.input1) xor resolveInputValue(circuit, wire.input2)
    }

    wire.value = value
    return value
}

private fun resolveInputValue(circuit: Circuit, input: String?): Boolean {
    val wires = when (circuit) {
        is Circuit.MapCircuit -> circuit.wires
        is Circuit.ListCircuit -> circuit.wires.associateBy { it.name }
    }
    return resolveWireValue(circuit, wires[input!!]!!)
}

fun calculateDecimalNumber(input: String): String {
    val circuit = parseCircuit(input, isMap = true) as Circuit.MapCircuit
    return circuit.run()
}

fun sortAndSwap(input: String): String {
    val circuit = parseCircuit(input, isMap = false) as Circuit.ListCircuit

    val invalidEndWires = circuit.wires.filter {
        it.name.first() == 'z' && it.name != "z45" && it.gate != Gate.XOR
    }

    val invalidMidWires = circuit.wires.filter {
        it.name.first() != 'z'
                && it.input1?.first() != 'x' && it.input1?.first() != 'y'
                && it.input2?.first() != 'x' && it.input2?.first() != 'y'
                && it.gate == Gate.XOR
    }

    invalidMidWires.forEach { wire ->
        val toSwitch = invalidEndWires.firstOrNull { it.name == findFirstOutputFrom(circuit.wires, wire.name) }
        if (toSwitch != null) {
            val temp = wire.name
            wire.name = toSwitch.name
            toSwitch.name = temp
        }
    }

    val xInput = interpretWireAsNumber('x', circuit.wires)
    val yInput = interpretWireAsNumber('y', circuit.wires)

    val diffFromActual = xInput + yInput xor circuit.run().toLong()
    val zeroBits = diffFromActual
        .countTrailingZeroBits()
        .toString()
        .padStart(2, '0')

    val invalidCarryWires = circuit.wires.filter {
        it.input1?.endsWith(zeroBits) == true
                && it.input2?.endsWith(zeroBits) == true
    }

    return (invalidEndWires + invalidMidWires + invalidCarryWires)
        .map { it.name }
        .sorted()
        .joinToString(",")
}

private fun findFirstOutputFrom(wires: List<Wire>, wire: String): String? {
    val parents = wires.filter { it.input1 == wire || it.input2 == wire }

    val validOutput = parents.find { it.name.first() == 'z' }
    if (validOutput == null) return parents.firstNotNullOfOrNull { findFirstOutputFrom(wires, it.name) }

    val previousOutputNumber = validOutput.name.drop(1).toInt() - 1
    return "z" + previousOutputNumber.toString().padStart(2, '0')
}

private fun interpretWireAsNumber(start: Char, wires: List<Wire>): Long {
    return wires
        .filter { it.name.first() == start }
        .sortedByDescending(Wire::name)
        .map { if (it.value == true) '1' else '0' }
        .joinToString("")
        .toLong(2)
}
