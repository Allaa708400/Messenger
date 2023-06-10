package com.example.messenger



import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.fragments.ChatFragment
import com.example.messenger.fragments.DiscoverFragment
import com.example.messenger.fragments.PeopleFragment
import com.example.messenger.model.User

//class MainActivity : AppCompatActivity() ,OnNavigationItemSelectedListener,
//    BottomNavigationView.OnNavigationItemSelectedListener{

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        var senderName:String = ""
    }
    private val auth: FirebaseAuth by lazy {  FirebaseAuth.getInstance()  }
    private val db:FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val currentUserDocRef:DocumentReference get() =
        db.document("Users/${auth.currentUser!!.uid.toString()}")

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val mChatFragment = ChatFragment()
    private val mPeopleFragment = PeopleFragment()
    private val mMoreFragment = DiscoverFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // هذا السطر لكي يجعل ال icon الي في status bar باللون الاسود
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }

        setSupportActionBar(binding.toolbarMain)
        supportActionBar!!.title = ""


        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject<User>()
            setNameAndImage(user!!)
        }

        setFragment(mChatFragment)


        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_chat_item -> {
                    setFragment(mChatFragment)

                    currentUserDocRef.get().addOnSuccessListener {
                        val user = it.toObject<User>()
                        setNameAndImage(user!!)
                    }

                    true
                }

                R.id.navigation_people_item -> {
                    setFragment(mPeopleFragment)
                    binding.titleToolbarTextview.text = "People"
                    true
                }

                R.id.navigation_more_item -> {
                    setFragment(mMoreFragment)
                    binding.titleToolbarTextview.text = "Discover"
                    true
                }

                else -> false
            }
        }

        binding.circleimageViewProfileImage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        // this make the user is online
        if(auth.currentUser != null)
            db.collection("Users").document(auth.currentUser!!.uid).update("online", true)
    }




    private fun setNameAndImage(user: User) {
        senderName = user.name  // this take sender name i am need in other activity

        binding.titleToolbarTextview.text = senderName
        if (user.profileImage.isNotEmpty()) {
            Glide.with(this@MainActivity)
                .load(storageInstance.getReference(user.profileImage))
                .placeholder(R.drawable.icon_people)
                .into(binding.circleimageViewProfileImage)
        }


    }








    fun setFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.coordinatorLayout_main_content, fragment)
        ft.commit()
    }


    //  هذه الداله علشان لما اعمل تسجيل دخول الي MainActivity
    // واعمل باك يخرجني بره التطبيق مش كل شويه يفضل يشغلها تاني
    override fun onBackPressed() {
        if(auth.currentUser != null)
            db.collection("Users").document(auth.currentUser!!.uid).update("online", false)

        val mainIntent = Intent(Intent.ACTION_MAIN)
        mainIntent.addCategory(Intent.CATEGORY_HOME)
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainIntent)
    }
}




    /*
    private lateinit var binding: ActivityMainBinding

    companion object {
        var senderName: String = ""
    }

    private val mAuth: FirebaseAuth by lazy {

        FirebaseAuth.getInstance()
    }

    private val currentUserDocRef: DocumentReference
        get() =
            firestoreInstance.document("Users/${mAuth.currentUser!!.uid.toString()}")

    private val firestoreInstance: FirebaseFirestore by lazy {

        FirebaseFirestore.getInstance()

    }

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val mChatFragment = ChatFragment()

    private val mPeopleFragment = PeopleFragment()

    private val mMoreFragment = DiscoverFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        /*
        firestoreInstance.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .get()
            .addOnSuccessListener {

                val user = it.toObject(User::class.java)

                if (user!!.profileImage.isNotEmpty()){

                    Glide.with(this@MainActivity)
                        .load(storageInstance.getReference(user.profileImage))
                        .into(binding.circleimageViewProfileImage)

                }else {

                    binding.circleimageViewProfileImage.setImageResource(R.drawable.icon_people)
                }

            }

         */





        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject<User>()
            setNameAndImage(user!!)
        }




        setSupportActionBar(binding.toolbarMain)

        supportActionBar?.title = ""


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        } else {

            window.statusBarColor = Color.WHITE
        }

        // binding.bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)

        setFragment(mChatFragment)



        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_chat_item -> {
                    setFragment(mChatFragment)

                    currentUserDocRef.get().addOnSuccessListener {
                        val user = it.toObject<User>()
                        setNameAndImage(user!!)
                    }

                    true
                }

                R.id.navigation_people_item -> {
                    setFragment(mPeopleFragment)
                    binding.titleToolbarTextview.text = "People"
                    true
                }

                R.id.navigation_more_item -> {
                    setFragment(mMoreFragment)
                    binding.titleToolbarTextview.text = "Discover"
                    true
                }

                else -> false
            }
        }

        binding.circleimageViewProfileImage.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

    }


    private fun setNameAndImage(user: User) {
        senderName = user.name  // this take sender name i am need in other activity

        binding.titleToolbarTextview.text = senderName
        if (user.profileImage.isNotEmpty()) {
            Glide.with(this@MainActivity)
                .load(storageInstance.getReference(user.profileImage))
                .placeholder(R.drawable.icon_people)
                .into(binding.circleimageViewProfileImage)
        }
    }

    fun setFragment(fragment: Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.coordinatorLayout_main_content, fragment)
        ft.commit()
    }

}

     */





/*

   override fun onNavigationItemSelected(item: MenuItem): Boolean {



        when(item.itemId){

            R.id.navigation_chat_item ->{

                setFragment(mChatFragment)

                return true
            }
            R.id.navigation_people_item ->{

                setFragment(mPeopleFragment)

                return true
            }
            R.id.navigation_more_item ->{

                setFragment(mMoreFragment)

                return true
            }

            else -> return false
        }


    }





    private fun setFragment(fragment: Fragment) {

        val fr = supportFragmentManager.beginTransaction()
        fr.replace(R.id.coordinatorLayout_main_content,fragment)
        fr.commit()

    }

 */












