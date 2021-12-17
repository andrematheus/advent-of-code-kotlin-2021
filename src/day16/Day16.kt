package day16

import Day

sealed class Packet {
    data class Literal(val version: Int, val value: Long) : Packet()
    data class Operator(val typeId: Int, val version: Int, val subPackets: List<Packet>) : Packet() {
        fun interpret(): Long {
            return when (this.typeId) {
                0 -> subPackets.map { it.result() }.sum()
                1 -> subPackets.map { it.result() }.fold(1) { p1, p2 -> p1 * p2 }
                2 -> subPackets.minOf { it.result() }
                3 -> subPackets.maxOf { it.result() }
                5 -> if (subPackets[0].result() > subPackets[1].result()) 1 else 0
                6 -> if (subPackets[0].result() < subPackets[1].result()) 1 else 0
                7 -> if (subPackets[0].result() == subPackets[1].result()) 1 else 0
                else -> throw IllegalStateException()
            }
        }
    }

    fun sumOfVersions(): Int {
        return when (this) {
            is Literal -> this.version
            is Operator -> this.version + this.subPackets.sumOf { it.sumOfVersions() }
        }
    }

    fun result(): Long {
        return when (this) {
            is Literal -> this.value
            is Operator -> this.interpret()
        }
    }
}

fun stringOfHexaToStringOfBits(stringOfHexa: String): String {
    val stringOfBits = StringBuffer()
    stringOfHexa.forEach {
        when (it) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> throw IllegalStateException()
        }.let {
            stringOfBits.append(it)
        }
    }
    return stringOfBits.toString()
}

fun readPacket(bits: String): Pair<Packet, String> {
    fun readVersion(): Int {
        return Integer.parseInt(bits.substring(0 until 3), 2)
    }

    fun readPacketTypeId(): Int {
        return Integer.parseInt(bits.substring(3 until 6), 2)
    }

    fun readLiteralValue(bits: String): Pair<Long, String> {
        if (bits.length < 5) {
            throw IllegalStateException()
        }
        var idx = 0
        var chunk = bits.substring(0 until 5)
        val chunks = StringBuffer()
        chunks.append(chunk.substring(1))

        while (chunk[0] != '0') {
            idx += 5
            chunk = bits.substring(idx until idx + 5)
            chunks.append(chunk.substring(1))
        }

        return Pair(chunks.toString().toLong(2), bits.substring(idx + 5))
    }

    fun readOperator(typeId: Int, version: Int, bits: String): Pair<Packet.Operator, String> {
        fun readLengthTypeId(): Char {
            return bits[0]
        }

        fun readLengthType0Packets(bits: String): Pair<Packet.Operator, String> {
            fun readLengthSubPackets(): Int {
                return Integer.parseInt(bits.substring(0, 15), 2)
            }

            val lengthSubPackets = readLengthSubPackets()
            var subPacketBits = bits.substring(15, 15 + lengthSubPackets)
            val packets = mutableListOf<Packet>()
            while (subPacketBits.isNotEmpty()) {
                val (packet, rest) = readPacket(subPacketBits)
                packets.add(packet)
                subPacketBits = rest
            }
            return Pair(
                Packet.Operator(typeId, version, packets),
                bits.substring(15 + readLengthSubPackets())
            )
        }

        fun readLengthType1Packets(bits: String): Pair<Packet.Operator, String> {
            fun readNumberOfSubPackets(): Int {
                return Integer.parseInt(bits.substring(0, 11), 2)
            }

            var subPacketBits = bits.substring(11)
            val packets = mutableListOf<Packet>()
            val numberOfSubPackets = readNumberOfSubPackets()

            for (i in 0 until numberOfSubPackets) {
                val (packet, rest) = readPacket(subPacketBits)
                packets.add(packet)
                subPacketBits = rest
            }

            return Pair(
                Packet.Operator(typeId, version, packets),
                subPacketBits
            )
        }

        return when (readLengthTypeId()) {
            '0' -> readLengthType0Packets(bits.substring(1))
            '1' -> readLengthType1Packets(bits.substring(1))
            else -> throw IllegalStateException()
        }
    }

    val packetBits = bits.substring(6)
    val version = readVersion()

    val packetTypeId = readPacketTypeId()

    return when (packetTypeId) {
        4 -> {
            val (value, rest) = readLiteralValue(packetBits)
            Pair(Packet.Literal(version, value), rest)
        }

        else -> {
            readOperator(packetTypeId, version, packetBits)
        }
    }
}

fun day16(): Day<*, *> {
    fun part1(passedInput: List<String>): List<Int> {
        val input = if (passedInput.isEmpty()) {
            listOf(
                "8A004A801A8002F478",
                "620080001611562C8802118E34",
                "C0015000016115A2E0802F182340",
                "A0016C880162017C3686B18A3D4780",
            )
        } else {
            passedInput
        }

        return input.map { readPacket(stringOfHexaToStringOfBits(it)) }
            .map { it.first }
            .map { it.sumOfVersions() }
    }

    fun part2(passedInput: List<String>): List<Long> {
        val input = if (passedInput.isEmpty()) {
            listOf(
                "C200B40A82",
                "04005AC33890",
                "880086C3E88112",
                "CE00C43D881120",
                "D8005AC2A8F0",
                "F600BC2D8F",
                "9C005AC2F8F0",
                "9C0141080250320F1802104A08",
            )
        } else {
            passedInput
        }

        return input.map { readPacket(stringOfHexaToStringOfBits(it)) }
            .map { it.first }
            .map { it.result() }
    }

    return Day(16, listOf(16, 12, 23, 31), ::part1, listOf(3L, 54L, 7L, 9L, 1L, 0L, 0L, 1L), ::part2)
}
