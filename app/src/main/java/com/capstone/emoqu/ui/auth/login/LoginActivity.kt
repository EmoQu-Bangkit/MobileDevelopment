package com.capstone.emoqu.ui.auth.login

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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.capstone.emoqu.R
import com.capstone.emoqu.component.CustomButton
import com.capstone.emoqu.databinding.ActivityLoginBinding
import com.capstone.emoqu.ui.ViewModelFactory
import com.capstone.emoqu.data.remote.Result
import com.capstone.emoqu.ui.auth.register.RegisterActivity
import com.capstone.emoqu.ui.fragment.TodayFragment

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val customButton: CustomButton = binding.btnLogin

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = binding.edEmail.text.toString()
                val password = binding.edPassword.text.toString()

                customButton.setEnabledState(
                    email,
                    password,
                    isRegister = false
                )
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        val imageViewGif = findViewById<ImageView>(R.id.iv_login)
        Glide.with(this)
            .asGif()
            .load(R.drawable.login)
            .into(imageViewGif)

        binding.edEmail.addTextChangedListener(textWatcher)
        binding.edPassword.addTextChangedListener(textWatcher)

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()
            viewModel.login(email, password).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressIndicator.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Login successful: ${result.data.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        navigateFromActivityToFragment(TodayFragment())
                    }
                    is Result.Error -> {
                        binding.progressIndicator.visibility = View.GONE
                        Toast.makeText(
                            this,
                            "Login failed: ${result.error}",
                            Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        makeSignUpLink()
    }

    private fun makeSignUpLink() {
        val text = getString(R.string.register_question)
        val spannableString = SpannableString(text)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                navigateFromActivityToAnotherActivity(RegisterActivity::class.java)
            }
        }
        val startIndex = text.indexOf("Sign Up")
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            startIndex + "Sign Up".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvLoginQuestion.text = spannableString
        binding.tvLoginQuestion.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun navigateFromActivityToAnotherActivity(destination: Class<*>) {
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }

    private fun navigateFromActivityToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}