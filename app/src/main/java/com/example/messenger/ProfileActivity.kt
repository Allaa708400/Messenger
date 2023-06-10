package com.example.messenger

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.messenger.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import com.example.messenger.model.User
import java.util.UUID


class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    companion object {
        const val RC_SELECT_IMAGE = 1
    }

    private val storageInstance: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val currentUserStorageRef: StorageReference
        get() =
            storageInstance.reference.child(FirebaseAuth.getInstance().currentUser!!.uid.toString())

    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private val currentUserDocRef: DocumentReference
        get() =
            db.document("Users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // هذا السطر لكي يجعل ال icon الي في status bar باللون الاسود
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }

        setSupportActionBar(binding.profileToolbar)
        supportActionBar!!.title = "Me"
        // السطرين دول علشان اظهر السهم الي موجود (سهم الرجوع)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnSignOut.setOnClickListener {
            db.collection("Users").document(auth.currentUser!!.uid).update("online", false)

            auth.signOut()
            val intent = Intent(this@ProfileActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        setDataInProfile()


        binding.circleimageViewProfileImage.setOnClickListener {


    //        val myIntentImage = Intent().apply {
    //            action = Intent.ACTION_GET_CONTENT
   //             type = "image/*"
   //             putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
  //          }
   //         startActivityForResult(myIntentImage, RC_SELECT_IMAGE)
   //     }




        val myIntentImage = Intent().apply {

            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))

        }
        startActivityForResult(
            Intent.createChooser(myIntentImage, "Select Image"),
            RC_SELECT_IMAGE
        )

    }




    }

    // this put data (image and userName ) in activity
    private fun setDataInProfile() {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject<User>()
            binding.textViewUserName.text = user!!.name

            if (user.profileImage.isNotEmpty()) {
                Glide.with(this@ProfileActivity)
                    .load(storageInstance.getReference(user.profileImage))
                    .placeholder(R.drawable.icon_people)
                    .into(binding.circleimageViewProfileImage)
            }
        }

    }


    // هذه الدله علشان لما اضغط علي السهم يعمل finish for activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
                return true
            }
        }
        return false
    }





   // fun onClickImageProfile(view: View) {
  //      val myIntentImage = Intent().apply {
  //          action = Intent.ACTION_GET_CONTENT
 //           type = "image/*"
  //          putExtra(Intent.EXTRA_MIME_TYPES , arrayOf("image/jpeg" , "image/png"))
   //     }
  //      startActivityForResult(myIntentImage , RC_SELECT_IMAGE)
  //  }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_SELECT_IMAGE && resultCode== RESULT_OK){
            binding.progressProfile.visibility = View.VISIBLE

           binding.circleimageViewProfileImage.setImageURI(data!!.data)

            val selectedImagePath = data.data
            val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver , selectedImagePath)
            val outputStream = ByteArrayOutputStream()
            selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG , 20 , outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            // this method to upload image to storage in firebase
            // and after uploaded take location of image and add to Object User in fireStore
            uploadProfileImage(selectedImageBytes){ path ->
                currentUserDocRef.update("profileImage" , path).addOnSuccessListener {
                    Toast.makeText(this , "User Is Updated" , Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this , "User Failure Updated : ${it.message}" , Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun uploadProfileImage(selectedImageBytes: ByteArray , onSuccess:(imagePath:String)->Unit) {
        val ref = currentUserStorageRef.child("ProfileImages/${UUID.nameUUIDFromBytes(selectedImageBytes)}")
        ref.putBytes(selectedImageBytes).addOnSuccessListener {
           binding.progressProfile.visibility = View.INVISIBLE
            onSuccess(ref.path) // هنا ارسلت ال location بتاع الصوره الي في ال  firebase Storage
            Toast.makeText(this , "Successfully Upload Image" , Toast.LENGTH_LONG).show()
        }.addOnFailureListener{
            Toast.makeText(this , "Failure Upload Image : ${it.message}" , Toast.LENGTH_LONG).show()
        }
    }


}










    /*

    private val auth: FirebaseAuth by lazy {  FirebaseAuth.getInstance()  }

    companion object {

        const val RC_SELECT_IMAGE = 2

    }

    private lateinit var userName: String


    private val firestoreInstance: FirebaseFirestore by lazy {

        // FirebaseFirestore.getInstance()

        Firebase.firestore
    }

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("Users/${FirebaseAuth.getInstance().currentUser!!.uid.toString()}")
    //  get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser?.uid.toString()}")

    private val storageInstance: FirebaseStorage by lazy {

        FirebaseStorage.getInstance()
    }

    private val currentUserStorageRef: StorageReference
        get() = storageInstance.reference.child(FirebaseAuth.getInstance().currentUser!!.uid.toString())
    //  get() = storageInstance.reference.child(FirebaseAuth.getInstance().currentUser?.uid.toString())

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)

        setContentView(binding.root)



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // هذا السطر لكي يجعل ال icon الي في status bar باللون الاسود
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.statusBarColor = Color.WHITE
        }

        setSupportActionBar(binding.profileToolbar)

        supportActionBar?.title = "Me"

        supportActionBar?.setHomeButtonEnabled(true)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


      binding.btnSignOut.setOnClickListener {

            firestoreInstance.collection("Users").document(auth.currentUser!!.uid).update("online", false)

            auth.signOut()
            val intent = Intent(this@ProfileActivity , SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }


        getUserInfo()





        /*
                getUserInfo { user ->

                    userName = user.name

   if (user != null) {
                if(user.profileImage.isNotEmpty()){
                    Glide.with(this@ProfileActivity)
                        .load(storageInstance.getReference(user.profileImage))
                        .placeholder(R.drawable.ic_account_circle)
                        .into(binding.circleimageViewProfileImage)
                }

                    Toast.makeText(this@ProfileActivity, user.profileImage, Toast.LENGTH_LONG).show()

                    Toast.makeText(this@ProfileActivity, user.name, Toast.LENGTH_LONG).show()
                    Log.d("UserN", "name $userName")


                }

         */



        binding.circleimageViewProfileImage.setOnClickListener {

            val myIntentImage = Intent().apply {

                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))

            }
            startActivityForResult(
                Intent.createChooser(myIntentImage, "Select Image"),
                RC_SELECT_IMAGE
            )

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SELECT_IMAGE && resultCode == RESULT_OK
           // && data != null && data.data != null
        ) {


            binding.progressProfile.visibility = View.INVISIBLE

           binding.circleimageViewProfileImage.setImageURI(data!!.data)

            val selectedImagePath = data.data

            val selectedImageBmp = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 20, outputStream)

            val selectedImageBytes = outputStream.toByteArray()

       //     binding.circleimageViewProfileImage.setImageURI(data.data)

            uploadProfileImage(selectedImageBytes) { path ->


                currentUserDocRef.update("profileImage" , path).addOnSuccessListener {
                    Toast.makeText(this , "User Is Updated" , Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "User Failure Updated : ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }

                    //   val userFieldMap = mutableMapOf<String, Any>()

            //    userFieldMap["name"] = "userName"

           //     userFieldMap["profileImage"] = path

              //  currentUserDocRef.update(userFieldMap)

            //    Log.d("UserDoc", "doc $currentUserDocRef")

            }
        }
    }

    private fun uploadProfileImage(selectedImageBytes: ByteArray, onSuccess: (imagePath: String) -> Unit) {

     //   val ref = currentUserStorageRef.child("profilePictures/${UUID.nameUUIDFromBytes(selectedImageBytes)}")
        val ref = currentUserStorageRef.child("ProfileImages/${UUID.nameUUIDFromBytes(selectedImageBytes)}")

        ref.putBytes(selectedImageBytes).addOnCompleteListener {

            binding.progressProfile.visibility = View.INVISIBLE

         //   if (it.isSuccessful) {

                onSuccess(ref.path)

            Toast.makeText(this , "Successfully Upload Image" , Toast.LENGTH_LONG).show()

        }.addOnFailureListener{

            Toast.makeText(this , "Failure Upload Image : ${it.message}" , Toast.LENGTH_LONG).show()
        }
             //   binding.progressProfile.visibility = View.INVISIBLE

         //   } else {

          //      Toast.makeText(this@ProfileActivity, "Error : ${it.exception?.message.toString()}", Toast.LENGTH_LONG).show()
        //    }
        }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item?.itemId) {

            android.R.id.home -> {

                finish()
                return true
            }

        }

        return false
    }

    /*
    private fun getUserInfo(onComplete : (User) -> Unit){

        currentUserDocRef.get().addOnSuccessListener {

       //     onComplete(it.toObject(User::class.java)!!)

          //  it.toObject(User::class.java)?.let { it1 -> onComplete(it1) }



            val user: User? = it.toObject(User::class.java)
            if (user != null) {
                onComplete(user)
            } else {
                // Handle the case where the user object is null
            }
        }
    }
     */


     private fun getUserInfo(){
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject<User>()

            binding.textViewUserName.text = user!!.name
            if (user.profileImage.isEmpty()) {

                Glide.with(this@ProfileActivity)
                    .load(storageInstance.getReference(user.profileImage))
                    .placeholder(R.drawable.icon_chat)
                    .into(binding.circleimageViewProfileImage)
            }


        }

    }

}

    */