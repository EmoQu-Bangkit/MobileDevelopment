package com.capstone.emoqu.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.capstone.emoqu.R
import com.capstone.emoqu.component.CustomButton
import com.capstone.emoqu.databinding.ActivityRegisterBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.ui.auth.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customButton: CustomButton = binding.btnRegister

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val firstName = binding.edFirstName.text.toString()
                val lastName = binding.edLastName.text.toString()
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()

                customButton.setEnabledState(
                    firstName,
                    lastName,
                    email,
                    password,
                    isRegister = true
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        val imageViewGif = findViewById<ImageView>(R.id.iv_register)
        Glide.with(this)
            .asGif()
            .load(R.drawable.register)
            .into(imageViewGif)

        binding.edFirstName.addTextChangedListener(textWatcher)
        binding.edLastName.addTextChangedListener(textWatcher)
        binding.edEmail.addTextChangedListener(textWatcher)
        binding.edPassword.addTextChangedListener(textWatcher)

        binding.btnRegister.setOnClickListener {
            val firstName = binding.edFirstName.text.toString()
            val lastName = binding.edLastName.text.toString()
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            viewModel.register(firstName, lastName, email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Registration successful: ${result.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        navigateFromActivityToAnotherActivity(LoginActivity::class.java)
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Registration failed: ${result.error}",
                            Toast.LENGTH_LONG
                        ) .show()
                    }
                }
            }
        }

        makeSignUpLink()
    }

    private fun makeSignUpLink() {
        val text = getString(R.string.login_question)
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navigateFromActivityToAnotherActivity(LoginActivity::class.java)
            }
        }
        val startIndex = text.indexOf("Sign In")
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + "Sign In".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvRegisterQuestion.text = spannableString
        binding.tvRegisterQuestion.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateFromActivityToAnotherActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}