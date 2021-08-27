package com.adhc.piclicker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import com.adhc.piclicker.data.StatusData
import com.adhc.piclicker.databinding.FragmentStatusBinding
import com.adhc.piclicker.grpc.GrpcThread
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass.
 * Use the [StatusFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatusFragment : Fragment() {


    private var _binding: FragmentStatusBinding? = null

    companion object {
        val statusData = StatusData("disconnected", 0.0)
        val executor = Executors.newFixedThreadPool(1)
        val TAG = StatusFragment.javaClass.name
    }


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStatusBinding.inflate(inflater, container, false)
        _binding!!.status = statusData


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.seekBarAngle.min = 0
        binding.seekBarAngle.setOnSeekBarChangeListener(seekBarAngleListener)



        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    val seekBarAngleListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(var1: SeekBar?, var2: Int, var3: Boolean) {
            Log.d(TAG, "seek bar clicked")
            Log.d(TAG, "onViewCreated: " + binding.seekBarAngle.progress)
            val angle: Double = ((binding.seekBarAngle.progress - 50.0) / 50.0)

            // update layout
            statusData.angle = angle
            binding.status = statusData

            // call thread
            val grpcThread = GrpcThread("10.100.102.13", angle, "50051")
            executor.execute(grpcThread)
        }

        override fun onStartTrackingTouch(var1: SeekBar?) {

        }

        override fun onStopTrackingTouch(var1: SeekBar?) {

        }
    }
}