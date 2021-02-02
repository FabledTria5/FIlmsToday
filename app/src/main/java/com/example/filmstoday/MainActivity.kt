package com.example.filmstoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.filmstoday.databinding.MainActivityBinding
import com.example.filmstoday.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val product = ProductsDataClass(productName = "Motherboards", productAmount = 15)
        val newProduct = product.copy(productName = "Processors")

        binding.btnChangeText.setOnClickListener {
            binding.tvText.text = "You pressed the button"

            binding.tvMotherboards.text = product.toString()
            binding.tvProcessors.text = newProduct.toString()
        }

        val arrayList = arrayListOf(15, 22, 33, 7, 14, -5)

        for (element in arrayList) {
            println(element)
        }

        for (index in arrayList.indices) {
            println(index)
        }
    }
}