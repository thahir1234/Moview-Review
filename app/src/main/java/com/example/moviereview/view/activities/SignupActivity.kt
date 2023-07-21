package com.example.moviereview.view.activities

//import com.example.moviereview.utils.Validation
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import com.example.moviereview.R
import com.example.moviereview.databinding.ActivitySignupBinding
import com.example.moviereview.db.local.entities.Accounts
import com.example.moviereview.db.local.entities.Users
import com.example.moviereview.db.local.viewmodel.AccountsViewModel
import com.example.moviereview.db.local.viewmodel.UsersViewModel
import com.example.moviereview.utils.HelperFunction
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

const val TAG = "signup"
class SignupActivity : AppCompatActivity() {

    lateinit var binding:ActivitySignupBinding

    lateinit var accountsViewModel: AccountsViewModel

    lateinit var usersViewModel: UsersViewModel

    lateinit var sharedPreferences : SharedPreferences

    private var name = ""
    private var email = ""
    private var password =""
    private var confirmPassword = ""
    private var bio = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("signUp","oncreate")


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        accountsViewModel = ViewModelProvider(this).get(AccountsViewModel::class.java)
        usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
        binding.signupBtn.setOnClickListener {
            name = binding.nameEtSignup.editText?.text.toString()
            email = binding.emailEtSignup.editText?.text.toString()
            password = binding.passwordEtSignup.editText?.text.toString()
            confirmPassword = binding.conpasswordEtSignup.editText?.text.toString()
            bio = binding.bioEtSignup.editText?.text.toString()
            insertUser()
        }

        binding.intiateLoginBtn.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        name = binding.nameEtSignup.editText?.text.toString()
        email = binding.emailEtSignup.editText?.text.toString()
        password = binding.passwordEtSignup.editText?.text.toString()
        confirmPassword = binding.conpasswordEtSignup.editText?.text.toString()
        bio = binding.bioEtSignup.editText?.text.toString()
        Log.i("signUp","onsave")
        Log.i("signUp",email)

        outState.putString("email",email)
        outState.putString("name",name)
        outState.putString("password",password)
        outState.putString("confirmPassword",confirmPassword)
        outState.putString("bio",bio)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        binding.emailEtSignup.editText?.setText(savedInstanceState.getString("email"))
        binding.nameEtSignup.editText?.setText(savedInstanceState.getString("name"))
        binding.passwordEtSignup.editText?.setText(savedInstanceState.getString("password"))
        binding.conpasswordEtSignup.editText?.setText(savedInstanceState.getString("confirmPassword"))
        binding.bioEtSignup.editText?.setText(savedInstanceState.getString("bio"))
        Log.i("signUp","onrestore")


    }
    private fun insertUser()
    {
        Log.i(TAG,"Inserting User")



        val okSignUp = signupVerification(email,name,password,confirmPassword)

        if(okSignUp)
        {
            val newAccount = Accounts(email,password)

            val newUser = Users(
                email =email, name = name,
                bio =bio, image = ContextCompat.getDrawable(this, R.drawable.account_profile_black)?.toBitmap()!!, dateCreated = "30/5/2023", userId = Random.nextInt(0,100))
            accountsViewModel.addAccount(newAccount)
            runBlocking {
                delay(400)
            }
            usersViewModel.addUser(newUser)
            sharedPreferences.edit().apply{
                this.putString("email",email)
                this.putString("password",password)
                this.putString("name",name)
                this.putString("bio",bio)
                this.putBoolean("loggedIn",true)
                apply()
            }
            Log.i("signup","signup success")
            startActivity(Intent(this,DefaultActivity::class.java))
        }
//        Log.i("signup","$name + $email + $password + $confirmPassword + $bio")

    }

    private fun signupVerification(email: String, name:String, password:String, confirmPassword :String) : Boolean
    {
        val user : List<Users>? = usersViewModel.readUserByName(name)

        val accounts : List<Accounts>? = accountsViewModel.readAccountByEmail(email)

        var okName :Boolean = false
        var okEmail:Boolean = false
        var okPassword:Boolean
        var okConPassword : Boolean


        okEmail = (accounts?.isEmpty() == true)

        okPassword = isValidPassword(password)

        okConPassword = (password == confirmPassword)


        if(!HelperFunction.isValidEmail(email)) {
            binding.emailEtSignup.error = "Enter a valid email!"
            binding.emailEtSignup.errorIconDrawable = null
            binding.emailEtSignup.editText?.requestFocus()
        }
        else if(!okEmail)
        {
            binding.emailEtSignup.error = "Email already exist!"
            binding.emailEtSignup.errorIconDrawable = null
            binding.emailEtSignup.editText?.requestFocus()
        }
        else{
            binding.emailEtSignup.error =null
        }

        if(!okPassword)
        {
            binding.passwordEtSignup.editText?.requestFocus()
            binding.passwordEtSignup.setError("Enter a valid password!(atleast 1 lowercase,1 uppercase,1 digit,1 special character)")
            binding.passwordEtSignup.errorIconDrawable = null

        }
        else
        {
            binding.passwordEtSignup.error =null

        }
        if(!okConPassword) {
            binding.conpasswordEtSignup.editText?.requestFocus()
            binding.conpasswordEtSignup.setError("Enter same passwords!")
            binding.conpasswordEtSignup.errorIconDrawable = null

        }
        else
        {
            binding.conpasswordEtSignup.error =null

        }
        if(okEmail && okPassword && okConPassword)
        {
            val toast = Toast.makeText(this,"Success Sign up",Toast.LENGTH_LONG)
            HelperFunction.showToast(toast,resources)
            return true
        }

        return false
    }
    fun isValidPassword(password: String?): Boolean {
        return when {
            !password?.contains(Regex("[A-Z]"))!! -> false
            !password.contains(Regex("[0-9]")) -> false
            !password.contains(Regex("[^a-zA-Z0-9 ]")) -> false
            else -> true
        }
    }

}
