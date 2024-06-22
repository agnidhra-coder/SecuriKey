package com.example.securikey.fragments

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import com.example.securikey.R
import com.example.securikey.databinding.FragmentPasswordQrBinding
import com.example.securikey.sdk29AndUp
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class PasswordQRFragment : Fragment() {
    private var passwordQRBinding: FragmentPasswordQrBinding? = null
    private val binding get() = passwordQRBinding!!
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        passwordQRBinding = FragmentPasswordQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = arguments?.getString("password")

        generateQR(password)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.shareQRBtn.setOnClickListener {
            shareQR(binding.qrIV.drawable)
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts
            .RequestMultiplePermissions()){
            isReadPermissionGranted = it[android.Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
            isWritePermissionGranted = it[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted
        }
        requestPermissions()

        binding.saveQRBtn.setOnClickListener{
            saveQR(arguments?.getString("siteName")!!, (binding.qrIV.drawable as BitmapDrawable).bitmap)
        }

    }

    private fun generateQR(data: String?) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            binding.qrIV.setImageBitmap(bmp)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun shareQR(image: Drawable?) {
        val bitmapDrawable = image as BitmapDrawable
        val bitmap = bitmapDrawable.bitmap

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/jpeg"
        val uri = getImageToShare(bitmap)
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.putExtra(Intent.EXTRA_TEXT, arguments?.getString("siteName"))
        startActivity(Intent.createChooser(shareIntent, "Share QR Code via"))
    }

    private fun getImageToShare(bitmap: Bitmap?): Uri? {
        val folder = File(requireActivity().cacheDir, "images")
        var uri : Uri? = null
        try {
            folder.mkdirs()
            val file = File(folder, "shared_image.jpg")
            val outputStream = file.outputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            uri = FileProvider.getUriForFile(requireContext(), "com.example.securikey", file)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return uri
    }

    private fun requestPermissions(){
        val isReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val isWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val minSdkLevel = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        isReadPermissionGranted = isReadPermission
        isWritePermissionGranted = isWritePermission || minSdkLevel

        val permissionsToRequest = mutableListOf<String>()
        if (!isReadPermissionGranted) {
            permissionsToRequest.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritePermissionGranted){
            permissionsToRequest.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()){
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }

    }

    private fun saveQR(displayName : String, bitmap: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.HEIGHT, bitmap.height)
            put(MediaStore.Images.Media.WIDTH, bitmap.width)
        }

        return try {
            requireContext().contentResolver.insert(imageCollection, contentValues)?.also {
                requireContext().contentResolver.openOutputStream(it).use {outputStream ->
                    if(!bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream!!)){
                        throw IOException("Couldn't save bitmap")
                    }
                }
            } ?: throw IOException("Couldn't create MediaStore entry")
            Toast.makeText(requireContext(), "QR saved successfully", Toast.LENGTH_SHORT).show()
            true
        } catch (e: IOException){
            e.printStackTrace()
            false
        }
    }


}