package com.yeji.flowpractice

import android.app.Activity
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

class NewFragment : Fragment() {
    private var _binding: FragmentNewBinding? = null
    private val binding: FragmentNewBinding get() = requireNotNull(_binding)
    private val newViewModel: NewViewModel by activityViewModels<NewViewModel>()

    /**
     * onstart 전에 resultNewLaucncher callback 안에 setNewValues()되어서 channel의 값이 방출되었다
     * 방출된게 그냥 흘러가버려서 간발의 타이밍으로 빗겨가서 그렇다
     */
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
                    // sharedflow는 받던지 안받던지 상관없이 흘려보내면 끝임
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
                    // channel은 blocking queue처럼 동작하여 방출되지 않고 기다리고 있다가 준비되면 방출하기에 잘 나옴
                    newViewModel.channel.collect { str ->
                        binding.tvChannel.text = str
                        Log.d("yezzz NewFragment", "channel: ${str}")

                    }
                }
                launch {
                    // stateflow는 이미 방출되었지만 다시 값을 요구하여 최신값을 보여주기에 뷰에서 보임
                    newViewModel.stateFlow.collect { str ->
                        binding.tvStateFlow.text = str
                        Log.d("yezzz NewFragment", "stateFlow: ${str}")
                    }
                }
            }
        }

    }



    fun setNewValues(str: String?) {
            newViewModel.setNewValues(str)
            Log.d("yezzz NewFragment :: setNewValues", "")

        // fragment쪽에서 ViewModel의 값을 바꾸지 말고 ViewModel쪽에서 바꾸어야 한다.
//        lifecycleScope.launch {
//            newViewModel.setSharedFlow(str)
//            newViewModel.setChannel(str)
//            newViewModel.setStateFlow(str)
//            Log.d("yezzz NewFragment :: setNewValues", "")
//        }
    }

}