package `in`.vikins.tacos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private var email: EditText? =null
    private var pass:EditText? =null
    var firebaseauth :FirebaseAuth? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val rgtbtn = findViewById<TextView>(R.id.click_to_register)
        rgtbtn.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        email = findViewById(R.id.loginEmailAddress)
        pass = findViewById(R.id.loginPassword)
        val lgnntn = findViewById<ImageView>(R.id.login)
        firebaseauth = FirebaseAuth.getInstance()
        var curuser : FirebaseUser? = firebaseauth?.currentUser
        lgnntn.setOnClickListener {
            validate()
        }
        if (curuser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    fun validate(){
        var em :String = email?.text.toString()
        var psw :String = pass?.text.toString()
        if(em.isEmpty() || psw.isEmpty() ){
            Toast.makeText(this,"Enter All Fields", Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseauth?.signInWithEmailAndPassword(em,psw)?.addOnCompleteListener { task->
                Log.e("email",em)
                Log.e("pass",psw)
                if (task.isSuccessful){
                    varifyemail()
                    finish()
                }
                else{
                    Toast.makeText(this,"Incorrect Email Or Password",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun varifyemail(){
        val firebaseuser = FirebaseAuth.getInstance().currentUser
        val vmail = firebaseuser!!.isEmailVerified
        if(vmail){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else{
            Toast.makeText(this,"Verify Email",Toast.LENGTH_SHORT).show()
            firebaseauth?.signOut()
        }
    }

}