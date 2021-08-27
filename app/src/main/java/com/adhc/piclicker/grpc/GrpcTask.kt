package com.adhc.piclicker.grpc

import android.R
import android.app.Activity
import android.os.AsyncTask
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder;
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.TimeUnit
import java.lang.ref.WeakReference


class GrpcTask constructor(activity: Activity) :
    AsyncTask<String?, Void?, String>() {
    private val activityReference: WeakReference<Activity>
    private var channel: ManagedChannel? = null
    protected override fun doInBackground(vararg params: String?): String {
        print("sss")
        val host = params[0]
        val message = params[1]
        val portStr = params[2]
        val port = if (TextUtils.isEmpty(portStr)) 0 else Integer.valueOf(portStr)
        return try {
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub: GreeterGrpc.GreeterBlockingStub = GreeterGrpc.newBlockingStub(channel)
            val request: HelloRequest = HelloRequest.newBuilder().setName(message).build()
            val reply: HelloReply = stub.sayHello(request)
            reply.getMessage()
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()
            java.lang.String.format("Failed... : %n%s", sw)
        }
    }

    override fun onPostExecute(result: String) {
        try {
            channel!!.shutdown().awaitTermination(1, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
//        val activity: Activity = activityReference.get() ?: return
//        val resultText = activity.findViewById<View>(R.id.grpc_response_text) as TextView
//        val sendButton: Button = activity.findViewById<View>(R.id.send_button) as Button
//        resultText.text = result
//        sendButton.setEnabled(true)
    }

    init {
        activityReference = WeakReference<Activity>(activity)
    }
}

