package com.chuks008.airclipboard.home.data.networking

/**
 * Used for formatting IP addresses
 */
object IpAddressMapper {

    const val IP_DELIMITER = "."

    /**
     * Create a prefix from an IP address (i.e. from 192.145.22.42 = 192.145.22)
     *
     * @param ipV4AddressValue IP address to prefix
     * @return an IP address value without the last set of numbers in the address
     */
    fun generateIPPrefix(ipV4AddressValue: String): String {
        val ipBlocks = ipV4AddressValue.split(IP_DELIMITER).toMutableList()
        ipBlocks.removeLast()
        return ipBlocks.joinToString(separator = IP_DELIMITER)
    }
}