package com.example.kotlinmessanger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessanger.Entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


class RegistrationActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        val Email: EditText = findViewById(R.id.Email_registration_activity)
        val Password: EditText = findViewById(R.id.Email_registration_activity)
        val Registation: Button = findViewById(R.id.Button_registration_activity)
        val SelectPhoto: ImageButton = findViewById(R.id.Select_photo_registration_activity)
        val Acc_Exist:TextView=findViewById(R.id.Have_acc_registration_activity)

        Acc_Exist.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        auth = Firebase.auth
        storage=Firebase.storage("gs://kotlinmessanger-271eb.appspot.com/")
        database=Firebase.database("https://kotlinmessanger-271eb-default-rtdb.europe-west1.firebasedatabase.app/")
        Registation.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        baseContext, "Please enter all fields",
                        Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Main", "createUserWithEmail:success ${task.result?.user?.uid}")

                            uploadImageToFirebase()
                        }
                    }
                    .addOnFailureListener {
                        Log.w("Main", "createUserWithEmail:failure ${it.message}")
                        Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
        }
        SelectPhoto.setOnClickListener {
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"

            startActivityForResult(intent,0)

        }

    }


    override fun onBackPressed() {
        finish()
    }
    var selectedPhotoUri: Uri?=null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            val SelectPhoto: ImageButton = findViewById(R.id.Select_photo_registration_activity)
            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            val CircleImgVew :CircleImageView=findViewById(R.id.circle_image_registration_activity)
            CircleImgVew.setImageBitmap(bitmap)
            SelectPhoto.alpha=0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            SelectPhoto.setBackgroundDrawable(bitmapDrawable)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SelectPhoto.foreground=null
            }
            val deg: Float = CircleImgVew.getRotation() + 90f
            CircleImgVew.animate().rotation(deg).setInterpolator(AccelerateDecelerateInterpolator())
        }
    }
    private fun uploadImageToFirebase(){
        if(selectedPhotoUri==null) return
        val filename= UUID.randomUUID().toString()
        val ref = storage.getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Main","Successfully upload image: ${it.metadata?.path}")
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToFirebaseDataBase(it.toString())
                    }
                }
                .addOnFailureListener{

                }

    }
    private fun saveUserToFirebaseDataBase(profileImageUrl:String){
        val Username:EditText=findViewById(R.id.Username_registration_activity)
        val uid = auth.uid ?: ""
        val ref = database.getReference("/users/$uid")
        val username:String=Username.text.toString()

        val user = User(uid,username,profileImageUrl)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Main","Data saved to Firebase")
                    val intent =Intent(this,LatesMessagesActivity::class.java)
                    startActivity(intent)
                }
    }
}
