package com.yeji.flowpractice

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yeji.flowpractice.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding get() = requireNotNull(_binding)
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newViewModel: NewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        newViewModel = ViewModelProvider(this)[NewViewModel::class.java]

        binding.btnOpenActivitySub.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            resultSubLauncher.launch(intent)
        }

        // case 1: Activity 종료 후 넘어온 값으로 설정
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    mainViewModel.sharedFlow.collect { str ->
                        binding.tvSharedFlow.text = str
                        Log.d("yezzz MainActivity", "sharedFlow: ${str}")
                    }
                }
                /**
                 * Channel인 경우 다른 coroutine scope면 받아올 수 없다!!!
                 * - 그래서 => externalScope.launch로 같은 coroutine scope로 동작하게끔 함!!!
                 * - MainViewModel에서 channel.send하는 경우가 다른 coroutine scope인 것이다.
                 *      ex.
                 *      viewModelScope.launch {
                //            setSharedFlow(null)
                //            setChannel(null)
                //            setStateFlow(null)
                //      }
                 */
                externalScope.launch {
                    // Channel인 경우 다른 coroutine scope면 받아올 수 없다!!!
                    val str = mainViewModel.channel.receive()
                    binding.tvChannel.text = str
                    Log.d("yezzz MainActivity", "channel: ${str}")
                }
                launch {
                    mainViewModel.stateFlow.collect { str ->
                        binding.tvStateFlow.text = str
                        Log.d("yezzz MainActivity", "stateFlow: ${str}")
                    }
                }





            }
        }




        // case 2: Fragment에서 Activity 종료 후 넘어온 값으로 설정
        // activity 안에 fragment 만들고 그 fragment 안에서 activity를 열어서 값을 전달하기
        // (=permission dialog 열리는 상황을 예제로 해보는 것!)
        /**
         * channel 값이 안찍히는 이유는?
         *
         */

        supportFragmentManager.beginTransaction()
            .add(binding.fragmentContainerView.id, NewFragment())
            .commit()
//        Log.d("yezzz NewActivity :: onCreate", "channel.tryReceive() -> " + viewModel.channel.tryReceive().toString())
    }

    private var resultSubLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult -> // callback

        if (result.resultCode  == Activity.RESULT_OK) {
            val intent = result.data
            val resultStr: String? = intent?.getStringExtra("result")

            Log.d("yezzz MainActivity :: after closed SubActivity", "resultStr: ${resultStr ?: ""}")

            setMainValues(resultStr)
        }
    }

    fun setMainValues(str: String?) {
        lifecycleScope.launch {
            mainViewModel.setSharedFlow(str)
            mainViewModel.setChannel(str)
            mainViewModel.setStateFlow(str)
        }
    }

}