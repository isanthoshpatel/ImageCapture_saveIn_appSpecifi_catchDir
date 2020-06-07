package com.example.imagecapture_savein_appspecifi_catchdir

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var granted = false
    var savedUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        p()

        bt_takePhoto.setOnClickListener {
            takePhoto()
        }

    }

    fun p() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            granted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.size > 0) {
            if (grantResults.get(0) == PackageManager.PERMISSION_GRANTED) {
                granted = true
            } else {
                p()
            }
        }
    }

    fun takePhoto() {
        Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
            startActivityForResult(this, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            var bitmap = data!!.extras!!.get("data") as Bitmap

            var file = File(
                cacheDir,
                SimpleDateFormat(
                    "yy-mm-dd-hh-mm-ss-SSS",
                    Locale.getDefault()
                ).format(SystemClock.currentThreadTimeMillis())
            )

            var fileoutpustream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileoutpustream)
            savedUri = Uri.fromFile(file)
            imageView.setImageURI(savedUri)
        }
    }

}
