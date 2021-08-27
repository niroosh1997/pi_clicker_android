package com.adhc.piclicker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adhc.piclicker.databinding.FragmentOpeningBinding
import com.adhc.piclicker.grpc.GrpcThread
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
            val grpcThread = GrpcThread("10.100.102.13", 0.0, "50051")
            executor.execute(grpcThread)

        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}