package com.adhc.piclicker.grpc

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.adhc.piclicker.StatusFragment
import com.adhc.piclicker.data.StatusData
import java.net.InetAddress
import java.util.concurrent.Executors


class WifiActions {

    companion object {
        val TAG = StatusFragment.javaClass.name
    }

    fun searchRaspberrypiService(context: Context): String? {
        try {
            val mWifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val mWifiInfo = mWifiManager.connectionInfo
            val subnet: String = getSubnetAddress(mWifiManager.dhcpInfo.gateway)
            for (i in 1..254) {
                val host = "$subnet.$i"
                if (InetAddress.getByName(host).isReachable(100)) {
                    Log.d(TAG, "host name is: " + InetAddress.getByName(host).canonicalHostName)
                    val hostname = InetAddress.getByName(host).canonicalHostName
                    if (hostname.equals("raspberrypi")) {
                        return host
                    }

                }
            }
        } catch (e: Exception) {
            System.out.println(e);
        }
        Log.d(TAG, "Finished scanning")
        return null
    }

    private fun getSubnetAddress(address: Int): String {
        return String.format(
            "%d.%d.%d",
            address and 0xff,
            address shr 8 and 0xff,
            address shr 16 and 0xff
        )
    }
}