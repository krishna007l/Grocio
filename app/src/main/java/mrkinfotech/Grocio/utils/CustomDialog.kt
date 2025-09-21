package mrkinfotech.Grocio.utils

import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.contracts.contract
import kotlin.math.cos

object CustomDialog {
    fun showTostMessage(Context: Context,messge : String){
        Toast.makeText(
            Context,
            messge
            ,Toast.LENGTH_SHORT
                ).show()
    }

}
