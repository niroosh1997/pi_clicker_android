package com.adhc.piclicker.grpc

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.adhc.piclicker.OpeningFragment
import com.adhc.piclicker.StatusFragment
import com.adhc.piclicker.data.StatusData
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.TimeUnit

class GrpcThread constructor(
    val host: String,
    val message: Double,
    val portStr: String
) : Thread() {


    companion object {
        val TAG = GrpcThread.javaClass.name
    }

    private var channel: ManagedChannel? = null

    override fun run() {
        Log.d(TAG, "run: thread started")
        val port = if (TextUtils.isEmpty(portStr)) 0 else Integer.valueOf(portStr)
        try {
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub: ChangeAngleServiceGrpc.ChangeAngleServiceBlockingStub =
                ChangeAngleServiceGrpc.newBlockingStub(channel)
            val request: ChangeAngleRequest =
                ChangeAngleRequest.newBuilder().setAngle(message).build()
            val reply: ChangeAngleReply = stub.changeAngle(request)
            Log.d(TAG, "run: got message:" + reply.getMessage())
            channel!!.shutdown().awaitTermination(1, TimeUnit.SECONDS)
            StatusFragment.statusData.connection = "Connected"
        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
    }


}