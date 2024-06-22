package com.example.securikey

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.securikey.databinding.ActivityScanQrBinding

private const val CAMERA_REQUEST_CODE = 101
class ScanQR : AppCompatActivity() {
    private lateinit var binding: ActivityScanQrBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var clipboardManager: ClipboardManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        binding.cameraPermissionBtn.visibility = View.GONE
        setupPermissions()
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(this, binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                runOnUiThread {
                    clipboardManager.setPrimaryClip(ClipData.newPlainText("Scan Result", it.text))
                }
            }

            errorCallback = ErrorCallback {
                runOnUiThread {
                    Log.i("camera", "${it.message}")
                }
            }

        }

        codeScanner.startPreview()

    }

    private fun setupPermissions(){
        binding.cameraPermissionBtn.visibility = View.GONE
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest
            .permission.CAMERA)

        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest(){
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission
            .CAMERA), CAMERA_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE -> {
                if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "You need camera permission", Toast
                        .LENGTH_SHORT).show()
                    binding.cameraPermissionBtn.visibility = View.VISIBLE
                    binding.cameraPermissionBtn.setOnClickListener {
                        setupPermissions()
                    }
                }else{ }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}