package com.adhc.piclicker.grpc

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import com.adhc.piclicker.OpeningFragment
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import java.io.PrintWriter
import java.io.StringWriter

class GrpcThread constructor(
    val host: String,
    val message: String,
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
            val stub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)
            val request: HelloRequest = HelloRequest.newBuilder().setName(message).build()
            val reply: HelloReply = stub.sayHello(request)
            Log.d(TAG, "run: got message:" + reply.getMessage())
        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
    }


}