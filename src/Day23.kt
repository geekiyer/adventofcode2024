fun main() {
    val connections = readInput("Day23")

    // Part 1
    val (totalTriads, filteredTriads) = part1(connections)
    println("Total Triads: $totalTriads")
    println("Filtered Triads: $filteredTriads")

    // Part 2
    val password = part2(connections)
    println("LAN Party Password: $password")
}

fun part1(connections: List<String>): Pair<Int, Int> {
    // Build adjacency list
    val graph = buildAdjacencyList(connections)

    // Find all fully connected triads
    val nodes = graph.keys.toList()
    val triads = mutableSetOf<Set<String>>()

    for (i in nodes.indices) {
        for (j in i + 1 until nodes.size) {
            for (k in j + 1 until nodes.size) {
                val (a, b, c) = listOf(nodes[i], nodes[j], nodes[k])
                if (graph[a]?.contains(b) == true && graph[a]?.contains(c) == true  && graph[b]?.contains(c) == true ) {
                    triads.add(setOf(a, b, c))
                }
            }
        }
    }

    // Filter triads where at least one node starts with "t"
    val filteredTriads = triads.filter { it.any { node -> node.startsWith("t") } }

    // Return results
    return Pair(triads.size, filteredTriads.size)
}

fun part2(connections: List<String>): String {
    // Build adjacency list
    val graph = buildAdjacencyList(connections)

    val largestClique = mutableSetOf<String>()
    traverseGraph(emptySet(), graph.keys, emptySet(), graph, largestClique)

    return largestClique.sorted().joinToString(",")
}

private fun buildAdjacencyList(connections: List<String>): MutableMap<String, MutableSet<String>> {
    val graph = mutableMapOf<String, MutableSet<String>>()
    connections.forEach { line ->
        val (a, b) = line.split("-")
        graph.computeIfAbsent(a) { mutableSetOf() }.add(b)
        graph.computeIfAbsent(b) { mutableSetOf() }.add(a)
    }
    return graph
}

private fun traverseGraph(
    node1: Set<String>,
    node2: Set<String>,
    node3: Set<String>,
    graph: Map<String, Set<String>>,
    largestClique: MutableSet<String>
) {
    if (node2.isEmpty() && node3.isEmpty()) {
        if (node1.size > largestClique.size) {
            largestClique.clear()
            largestClique.addAll(node1)
        }
        return
    }

    // Choose a pivot
    val pivot = (node2 + node3).firstOrNull() ?: return

    // Explore nodes not connected to the pivot
    for (v in node2 - (graph[pivot] ?: emptySet())) {
        traverseGraph(
            node1 + v,
            node2.intersect(graph[v] ?: emptySet()),
            node3.intersect(graph[v] ?: emptySet()),
            graph,
            largestClique
        )
    }
}