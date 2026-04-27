package mrkinfotech.Grocio.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import mrkinfotech.Grocio.R


object CustomDialog {
    fun showToast(context: Context, message: String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

    fun showPicAddressDialog(
        context: Context,
        activity: Activity,
        currentAddress: String = "",
        onAddressSelected: (String) -> Unit,
        selectAddress: View.OnClickListener? = null
    ): BottomSheetDialog {
        val dialog = BottomSheetDialog(context)
        dialog.setCancelable(false)

        val view = activity.layoutInflater.inflate(
            R.layout.dialog_pic_address, null
        )

        val buttonAddressFromMap: AppCompatTextView =
            view.findViewById(R.id.buttonAddressFromMap)
        val editTextAddress: AppCompatEditText = view.findViewById(R.id.edittextAddress)
        val buttonConfirmLocation: AppCompatButton =
            view.findViewById(R.id.buttonConfirmLocation)

        if (currentAddress.isNotEmpty()) {
            editTextAddress.setText(currentAddress)
            editTextAddress.visibility = View.VISIBLE
            buttonConfirmLocation.visibility = View.VISIBLE
        } else {
            editTextAddress.visibility = View.GONE
            buttonConfirmLocation.visibility = View.GONE
        }

        buttonAddressFromMap.setOnClickListener {
            selectAddress?.onClick(it)
            dialog.dismiss()
        }

        buttonConfirmLocation.setOnClickListener {
            val address = editTextAddress.text.toString()
            if (address.isNotEmpty()) {
                onAddressSelected(address)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Please enter Your address", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.setContentView(view)
        dialog.show()
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

    fun showConfirmationDialog(
        context: Context,
        strMsg: String?,
        positiveText: String,
        negativeText: String,
        positiveListener: View.OnClickListener
    ) {
        val dialog = Dialog(context)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_confirm_alert)
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txtMsg: AppCompatTextView = dialog.findViewById(R.id.textViewMsg)
        txtMsg.text = strMsg
        val btnOk: AppCompatButton = dialog.findViewById(R.id.buttonOk)
        btnOk.text = positiveText
        val btnCancel: AppCompatButton = dialog.findViewById(R.id.buttonCancel)
        btnCancel.text = negativeText
        btnOk.setOnClickListener {
            dialog.dismiss()
            positiveListener.onClick(it)
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
