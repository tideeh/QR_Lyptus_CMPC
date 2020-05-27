package br.com.polenflorestal.qrcodecmpc

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import br.com.polenflorestal.qrcodecmpc.Constants.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException


class QRScanner : AppCompatActivity() {

    private val TAG = "QRScanner"
    var svCamera: SurfaceView? = null
    var barcode: BarcodeDetector? = null
    var cameraSource: CameraSource? = null
    var holder: SurfaceHolder? = null
    var rlCamera: RelativeLayout? = null
    var vLeft: View? = null
    var vTop: View? = null
    var vRight: View? = null
    var vBottom: View? = null
    var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_q_r_scanner)
        svCamera = findViewById(R.id.svCamera)
        rlCamera = findViewById(R.id.llCamera)
        vLeft = findViewById(R.id.vLeft)
        vTop = findViewById(R.id.vTop)
        vRight = findViewById(R.id.vRight)
        vBottom = findViewById(R.id.vBottom)
        setUI()
        startScanning()
    }

    private fun setUI() {
        val intent = intent
        val showToolBar = intent.getBooleanExtra(QR_IS_TOOLBAR_SHOW, false)
        val toolbar_text = intent.getStringExtra(QR_TOOLBAR_TEXT)
        val toolbar_drawable_id = intent.getIntExtra(QR_TOOLBAR_DRAWABLE_ID, 0)
        val toolbar_background_color =
            intent.getStringExtra(QR_TOOLBAR_BACKGROUND_COLOR)
        val toolbar_text_color = intent.getStringExtra(QR_TOOLBAR_TEXT_COLOR)
        val background_color = intent.getStringExtra(QR_BACKGROUND_COLOR)
        val camera_margin_left = intent.getIntExtra(QR_CAMERA_MARGIN_LEFT, 0)
        val camera_margin_top = intent.getIntExtra(QR_CAMERA_MARGIN_TOP, 0)
        val camera_margin_right = intent.getIntExtra(QR_CAMERA_MARGIN_RIGHT, 0)
        val camera_margin_bottom = intent.getIntExtra(QR_CAMERA_MARGIN_BOTTOM, 0)
        val camera_border = intent.getIntExtra(QR_CAMERA_BORDER, 0)
        val camera_border_color = intent.getStringExtra(QR_CAMERA_BORDER_COLOR)
        val is_scan_bar = intent.getBooleanExtra(QR_IS_SCAN_BAR, false)
        val is_beep = intent.getBooleanExtra(QR_IS_BEEP, false)
        val beep_resource_id = intent.getIntExtra(QR_BEEP_RESOURCE_ID, 0)

        if (null != background_color) {
            rlCamera!!.setBackgroundColor(Color.parseColor(background_color))
        }
        if (camera_border > 0) {
            val params1 =
                vLeft?.layoutParams as RelativeLayout.LayoutParams
            params1.width = camera_border
            params1.setMargins(0, 0, 0, camera_border)
            vLeft!!.layoutParams = params1
            val params2 =
                vTop?.layoutParams as RelativeLayout.LayoutParams
            params2.height = camera_border
            params2.setMargins(camera_border, 0, 0, 0)
            vTop!!.layoutParams = params2
            val params3 =
                vRight?.layoutParams as RelativeLayout.LayoutParams
            params3.width = camera_border
            params3.setMargins(0, camera_border, 0, 0)
            vRight!!.layoutParams = params3
            val params4 =
                vBottom?.layoutParams as RelativeLayout.LayoutParams
            params4.height = camera_border
            params4.setMargins(0, 0, camera_border, 0)
            vBottom!!.layoutParams = params4
            if (null != camera_border_color) {
                vLeft!!.setBackgroundColor(Color.parseColor(camera_border_color))
                vTop!!.setBackgroundColor(Color.parseColor(camera_border_color))
                vRight!!.setBackgroundColor(Color.parseColor(camera_border_color))
                vBottom!!.setBackgroundColor(Color.parseColor(camera_border_color))
            }
        }
        rlCamera!!.setPadding(
            camera_margin_left,
            camera_margin_top,
            camera_margin_right,
            camera_margin_bottom
        )
        if (is_scan_bar) {
            startAnimation()
        }
        if (is_beep) {
            if (beep_resource_id > 0) {
                mp = MediaPlayer.create(applicationContext, beep_resource_id)
            } else {
                mp = MediaPlayer.create(applicationContext, R.raw.beep)
            }
        }
    }

    private fun startAnimation() {
        val vScanBar: View = findViewById(R.id.vScanBar)
        vScanBar.visibility = View.VISIBLE
        val animation =
            AnimationUtils.loadAnimation(this@QRScanner, R.anim.anim_scan_effect)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                vScanBar.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        vScanBar.startAnimation(animation)
    }

    private fun startScanning() {
        svCamera!!.setZOrderMediaOverlay(true)
        holder = svCamera!!.holder
        barcode = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build()
        if (!barcode!!.isOperational) {
            Log.e(
                TAG,
                "onCreate: " + "Sorry! Could not setup the decoder or do not have camera permission."
            )
            val intent = Intent()
            intent.putExtra(QR_DATA, "")
            setResult(AppCompatActivity.RESULT_CANCELED, intent)
            finish()
        }
        cameraSource = CameraSource.Builder(this, barcode)
            .setFacing(CameraSource.CAMERA_FACING_BACK)
            .setRequestedFps(24F)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1920, 1024)
            .build()
        svCamera!!.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
                try {
                    cameraSource!!.start(svCamera!!.holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(
                surfaceHolder: SurfaceHolder,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {}
        })
        barcode!!.setProcessor(object : Detector.Processor<Barcode?> {
            override fun release() {}
            override fun receiveDetections(detections: Detector.Detections<Barcode?>) {
                val barcodes: SparseArray<Barcode?>? = detections.detectedItems
                if (barcodes?.size() ?: 0 > 0) {
                    if (null != mp) {
                        mp!!.start()
                    }
                    val code: String = barcodes?.valueAt(0)?.rawValue ?: "-1"
                    val intent = Intent()
                    intent.putExtra(QR_DATA, code)
                    setResult(AppCompatActivity.RESULT_OK, intent)
                    Log.d(TAG, "receiveDetections: $code")
                    finish()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        if (cameraSource != null) {
            cameraSource!!.stop()
            cameraSource!!.release()
            cameraSource = null
        }
    }
}
