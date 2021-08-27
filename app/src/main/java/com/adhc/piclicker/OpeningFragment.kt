package com.adhc.piclicker

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adhc.piclicker.data.StatusData
import com.adhc.piclicker.databinding.FragmentOpeningBinding
import com.adhc.piclicker.grpc.GrpcTask
import com.adhc.piclicker.grpc.GrpcThread
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [OpeningFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpeningFragment : Fragment() {

    companion object {
        val TAG = OpeningFragment.javaClass.name
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

            //todo: run async task
            Log.d(TAG, "onViewCreated: run async task")
            val grpcThread = GrpcThread("10.0.2.2", "hey", "50051")
            grpcThread.start()

        }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}