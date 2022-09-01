package com.yeji.flowpractice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yeji.flowpractice.databinding.FragmentNewBinding
import kotlinx.coroutines.launch

//class NewFragment : Fragment(), OnResultDataPassListener {
class NewFragment : Fragment() {
    private var _binding: FragmentNewBinding? = null
    private val binding: FragmentNewBinding get() = requireNotNull(_binding)
    private val newViewModel: NewViewModel by activityViewModels<NewViewModel>()

    private val resultNewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> // callback

        if (result.resultCode  == Activity.RESULT_OK) {
            val intent = result.data
            val resultStr: String? = intent?.getStringExtra("result")

            Log.d("yezzz NewFragment :: after closed Activity", "resultStr: ${resultStr ?: ""}")

            setNewValues(resultStr)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenNewActivity.setOnClickListener {
            val intent = Intent(requireContext(), NewActivity::class.java)
            resultNewLauncher.launch(intent)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    newViewModel.sharedFlow.collect { str ->
                        binding.tvSharedFlow.text = str
                        Log.d("yezzz NewFragment", "sharedFlow: ${str}")
                    }
                }
                launch {
                    /**
                     * Flow인 경우 다른 coroutine scope여도 받아올 수 있다!!!
                     * - MainViewModel에서 channel.send하는 경우가 다른 coroutine scope인 것이다.
                     *      ex.
                     *      viewModelScope.launch {
                    //            setSharedFlow(null)
                    //            setChannel(null)
                    //            setStateFlow(null)
                    //      }
                     */
                    newViewModel.channel.collect { str ->
                        binding.tvSharedFlow.text = str
                        Log.d("yezzz NewFragment", "channel: ${str}")

                    }
                }
                launch {
                    newViewModel.stateFlow.collect { str ->
                        binding.tvStateFlow.text = str
                        Log.d("yezzz NewFragment", "stateFlow: ${str}")
                    }
                }
            }
        }

    }



    fun setNewValues(str: String?) {
        lifecycleScope.launch {
            newViewModel.setSharedFlow(str)
            newViewModel.setChannel(str)
            newViewModel.setStateFlow(str)
            Log.d("yezzz NewFragment :: after closed NewActivity", "")

        }
    }


//    override fun onResultDataPass(resultData: String?) {
//        Log.d("yezzz NewFragment :: setNewValues", "resultStr: ${resultData ?: ""}")
//
//        setNewValues(resultData)
//    }


}