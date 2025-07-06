package mrkinfotech.Grocio.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
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