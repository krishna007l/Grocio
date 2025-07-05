package mrkinfotech.Grocio.utils

import android.content.Context
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext

object CustomDialog {
    fun showtostmassagee (context: Context , name:String){
        Toast.makeText(context, name, Toast.LENGTH_SHORT)
            .show()
    }
}