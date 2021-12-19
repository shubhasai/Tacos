package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.ActivityRegisterBinding
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    private var ufullname: EditText?=null
    private var uemail:EditText?=null
    private var upass:EditText?=null
    private var firebaseregister: FirebaseAuth?=null
    private lateinit var mfirebasedatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ufullname = findViewById(R.id.rgtname)
        uemail = findViewById(R.id.rgtEmailAddress)
        upass = findViewById(R.id.rgtPassword)
        firebaseregister = FirebaseAuth.getInstance()
        binding.register.setOnClickListener{
            register()
        }
    }
    private fun register(){
        val email:String = uemail?.text?.trim().toString()
        val password:String = upass?.text.toString()
        val n:String = ufullname?.text.toString()
        if(email.isEmpty() || password.isEmpty() || n.isEmpty()){
            Toast.makeText(this,"Enter All Fields",Toast.LENGTH_SHORT).show()
        }
        else{
            firebaseregister!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task->
                    if(task.isSuccessful){
                        savedata()
                        checkemail()
                    } else{
                        Toast.makeText(this,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                        Log.e("SG","RG error:"+(task.exception!!.message))
                    }
                }
        }
    }
    private fun checkemail(){
        val firebaseuser = firebaseregister?.currentUser
        firebaseuser?.sendEmailVerification()?.addOnCompleteListener { task->
            if(task.isSuccessful){
                Toast.makeText(this,"Verification Mail Sent",Toast.LENGTH_SHORT).show()
                firebaseregister?.signOut()
                finish()
                startActivity(Intent(this,LoginActivity::class.java))
            }
            else{
                Toast.makeText(this,"Verification Mail Sent",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun savedata(){
        val udemail :String = uemail?.text.toString()
        val udname:String =ufullname?.text.toString()
//        val fbdatabase = FirebaseDatabase.getInstance()
//        val fbreference = fbdatabase.getReference(firebaseregister?.uid.toString()).
        mfirebasedatabase = Firebase.database.reference.child("users")
        mfirebasedatabase.child(firebaseregister?.uid.toString()).child("fname").setValue(udname)
        mfirebasedatabase.child(firebaseregister?.uid.toString()).child("email").setValue(udemail)
        mfirebasedatabase.child(firebaseregister?.uid.toString()).child("uid").setValue(firebaseregister?.uid.toString())
    }
}