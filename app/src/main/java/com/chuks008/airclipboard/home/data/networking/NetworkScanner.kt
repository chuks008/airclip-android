package com.chuks008.airclipboard.home.data.networking

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.Socket
import java.net.SocketAddress

/**
 * Scans through the current user's network connections in order to find the host's IP address
 * or a specific port to connect to
 */
object NetworkScanner {

    private val inetRange = 0..255
    const val DEFAULT_SCAN_TIMEOUT_IN_MILLIS = 15

    /**
     * Find the host IP connection a user is connected to when using a WIFI connection
     *
     * @return the IP address of the host computer
     */
    fun getIpv4HostAddress(): String? {
        NetworkInterface.getNetworkInterfaces()?.toList()?.map { networkInterface ->
            networkInterface.inetAddresses?.toList()?.find {
                !it.isLoopbackAddress && it is Inet4Address
            }?.let { return it.hostAddress }
        }
        return null
    }

    /**
     * Searches to a specific socket listening on a port on the network
     *
     * @param defaultGatewayIp Default IP address used by the host machine (i.e. user's PC) to connect
     * to the internet
     * @param port Port number listening on the default API gateway
     * @return a confirmation that the IP address and port are indeed on the user's network and is
     * reachable
     */
    suspend fun confirmSocketWithListeningPort(defaultGatewayIp: String, port: Int) = withContext(Dispatchers.IO) {
        var foundIp = ""
        val ipPrefix = IpAddressMapper.generateIPPrefix(defaultGatewayIp)
        for(suffix in inetRange) {
            val currentIP = "$ipPrefix.$suffix"
            val sock = Socket()
            try {
                val socketAddress: SocketAddress = InetSocketAddress(currentIP, port)
                sock.connect(socketAddress, DEFAULT_SCAN_TIMEOUT_IN_MILLIS)
                foundIp = currentIP
                break
            } catch (ex: Exception) {
               /** Do Nothing **/
            } finally {
                sock.close()
            }
        }
        return@withContext if(foundIp.isNotEmpty()) "$foundIp:$port" else null
    }
}