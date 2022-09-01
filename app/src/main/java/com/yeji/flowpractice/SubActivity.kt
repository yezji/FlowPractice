package com.yeji.flowpractice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.yeji.flowpractice.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    private var _binding: ActivitySubBinding? = null
    private val binding: ActivitySubBinding get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnClose.setOnClickListener {
            val sendStr = "VALID"
            val intent = Intent()
                .putExtra("result", sendStr)
            setResult(RESULT_OK, intent)
            Log.d("yezzz SubActivity", "sendStr: ${sendStr ?: ""}")
            finish()
        }
    }

}