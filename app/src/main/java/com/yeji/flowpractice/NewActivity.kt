package com.yeji.flowpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yeji.flowpractice.databinding.ActivityNewBinding

class NewActivity : AppCompatActivity() {
    private var _binding: ActivityNewBinding? = null
    private val binding: ActivityNewBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCloseNewActivity.setOnClickListener {
            // Activity 종료 전 특정 값 전달
            val sendStr = "VALID"
            val intent = Intent()
                .putExtra("result", sendStr)
            setResult(RESULT_OK, intent)

            Log.d("yezzz NewActivity", "sendStr: ${sendStr ?: ""}")

            finish()
        }

    }

}