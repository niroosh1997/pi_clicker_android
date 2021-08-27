package com.adhc.piclicker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.adhc.piclicker.databinding.FragmentOpeningBinding
import com.adhc.piclicker.grpc.GrpcThread
import com.adhc.piclicker.grpc.WifiActions
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass.
 * Use the [OpeningFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpeningFragment : Fragment() {

    companion object {
        val TAG = OpeningFragment.javaClass.name
        val executor = Executors.newFixedThreadPool(1)
    }

    private var _binding: FragmentOpeningBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOpeningBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.floatingTurnOnButton.setOnClickListener { _ ->
            StatusFragment.statusData.connection = "connected"

            Log.d(TAG, "onViewCreated: clicked turn off light")
            Log.d(TAG, "onViewCreated: run thread")

            executor.execute(Runnable {
                var messageToToast = "didn't found raspberrypi"
                val wifiActions = WifiActions()
                val raspHost = wifiActions.searchRaspberrypiService(requireContext())
                if (raspHost != null) {
                    Log.d(TAG, "found raspberry in ip: $raspHost")
                    messageToToast = "found raspberry in ip: $raspHost"

                    StatusFragment.statusData.host = raspHost
                }


                requireActivity().runOnUiThread(Runnable {
                    Toast.makeText(context, messageToToast, Toast.LENGTH_LONG).show()
                })
            })
        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}