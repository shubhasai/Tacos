package `in`.vikins.tacos

import `in`.vikins.tacos.databinding.ActivityMainBinding
import android.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    val user = FirebaseAuth.getInstance().currentUser
    val userid = user?.uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val Navhost = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        binding.navMenu.setupWithNavController(Navhost.navController)

        setSupportActionBar(binding.topAppBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(this,binding.drawerLayout,R.string.app_name,R.string.app_name)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val navhost =  supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        binding.navView.setupWithNavController(navhost.navController)
        val headerView : View = binding.navView.getHeaderView(0)
        val navUsername : TextView = headerView.findViewById(R.id.username)
        val navUserdp : ImageView = headerView.findViewById(R.id.userdp)
        val database = FirebaseDatabase.getInstance().getReference("users")
        database.child(userid).get().addOnSuccessListener {
            if(it.exists()){
                val n = it.child("fname").value.toString()
                var name = n
                var d = it.child("dp").value.toString()
                val dp = d
                navUsername.text = name.uppercase()
                Glide.with(this@MainActivity).load(dp).error(R.drawable.ic_about).into(navUserdp)
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}