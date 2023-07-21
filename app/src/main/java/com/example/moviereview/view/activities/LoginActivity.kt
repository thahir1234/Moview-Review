package com.example.moviereview.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.databinding.ActivityLoginBinding
import com.example.moviereview.db.local.entities.Accounts
import com.example.moviereview.db.local.viewmodel.AccountsViewModel
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction

class LoginActivity : AppCompatActivity(){

    lateinit var binding:ActivityLoginBinding

    lateinit var accountsViewModel: AccountsViewModel

    lateinit var usersViewModel: UsersViewModel

    lateinit var sharedPreferences : SharedPreferences

    var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        isLoggedIn = sharedPreferences.getBoolean("loggedIn",false)

        if(isLoggedIn)
        {
            setContentView(binding.root)
            Log.i("Login","true")
            startActivity(Intent(this,DefaultActivity::class.java))
        }
        else
        {
            Log.i("Login","false")
            setContentView(binding.root)
        }

        accountsViewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)
        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)

        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        binding.intiateSignupBtn.setOnClickListener {
            startActivity(Intent(this,SignupActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val email = binding.emailEt.editText?.text.toString().trim()
        val password = binding.passwordEt.editText?.text.toString().trim()

        outState.putString("email",email)
        outState.putString("password",password)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        binding.emailEt.editText?.setText(savedInstanceState.getString("email"))
        binding.passwordEt.editText?.setText(savedInstanceState.getString("password"))
    }
    private fun loginUser()
    {
        val email = binding.emailEt.editText?.text.toString().trim()
        val password = binding.passwordEt.editText?.text.toString().trim()

        if(loginVerification(email,password))
        {
            usersViewModel.getUserByEmail(email)
            usersViewModel.userByEmail.observe(this)
            {
                sharedPreferences.edit().apply{
                    this.putString("email",email)
                    this.putString("name",it.get(0).name)
                    this.putString("bio",it.get(0).bio)
                    this.putString("password",password)
                    this.putBoolean("loggedIn",true)
                    apply()
                }
            }


            val toast  = Toast.makeText(this,"Login Success",Toast.LENGTH_LONG)
            HelperFunction.showToast(toast, resources)
            startActivity(Intent(this,DefaultActivity::class.java))
        }
    }

    private fun loginVerification(email:String,password:String) : Boolean
    {
        var okCredentials = false

        val accounts : List<Accounts>? = accountsViewModel.readAccountByEmail(email)

        if(HelperFunction.isValidEmail(email) == false)
        {
            binding.emailEt.editText?.requestFocus()
            binding.emailEt.error = "Enter valid email!"
            binding.emailEt.errorIconDrawable = null
            return false
        }
        else
        {
            binding.emailEt.error = null

        }
        if(accounts?.isEmpty() == true)
        {
            binding.emailEt.editText?.requestFocus()
            binding.emailEt.error = "Email does not exist!"
            binding.emailEt.errorIconDrawable = null
            return false
        }
        else{
            binding.emailEt.error = null

        }
        if(accounts?.get(0)?.password != password)
        {
            binding.passwordEt.editText?.requestFocus()
            binding.passwordEt.error = "Password doesn't match!"
            binding.passwordEt.errorIconDrawable = null
            return false
        }
        else
        {
            binding.passwordEt.error = null
        }

        return true
    }
}