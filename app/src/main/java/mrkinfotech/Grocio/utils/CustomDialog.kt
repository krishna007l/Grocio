package mrkinfotech.Grocio.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.ui.data.CheckoutDetails


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
        dialog.setCancelable(true)

        val view = activity.layoutInflater.inflate(
            R.layout.dialog_pic_address, null
        )

        val buttonAddressFromMap: AppCompatTextView =
            view.findViewById(R.id.buttonAddressFromMap)
        val editTextAddress: AppCompatEditText = view.findViewById(R.id.edittextAddress)
        val buttonConfirmLocation: AppCompatButton =
            view.findViewById(R.id.buttonConfirmLocation)

        editTextAddress.setText(currentAddress)
        editTextAddress.visibility = View.VISIBLE
        buttonConfirmLocation.visibility = View.VISIBLE
        buttonAddressFromMap.isVisible = selectAddress != null

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

    fun showCheckoutDialog(
        context: Context,
        activity: Activity,
        initialDetails: CheckoutDetails,
        totalAmount: String,
        onConfirm: (CheckoutDetails) -> Unit
    ): BottomSheetDialog {
        val dialog = BottomSheetDialog(context)
        val view = activity.layoutInflater.inflate(R.layout.dialog_checkout_details, null)

        val textCheckoutTotalValue: AppCompatTextView =
            view.findViewById(R.id.textCheckoutTotalValue)
        val editTextName: AppCompatEditText = view.findViewById(R.id.editTextCheckoutName)
        val editTextPhone: AppCompatEditText = view.findViewById(R.id.editTextCheckoutPhone)
        val editTextEmail: AppCompatEditText = view.findViewById(R.id.editTextCheckoutEmail)
        val editTextAddress: AppCompatEditText = view.findViewById(R.id.editTextCheckoutAddress)
        val editTextNote: AppCompatEditText = view.findViewById(R.id.editTextCheckoutNote)
        val buttonConfirm: AppCompatButton = view.findViewById(R.id.buttonConfirmOrder)

        textCheckoutTotalValue.text = totalAmount
        editTextName.setText(initialDetails.customerName)
        editTextPhone.setText(initialDetails.phoneNumber)
        editTextEmail.setText(initialDetails.email)
        editTextAddress.setText(initialDetails.deliveryAddress)
        editTextNote.setText(initialDetails.orderNote)

        buttonConfirm.setOnClickListener {
            val customerName = editTextName.text?.toString().orEmpty().trim()
            val phoneNumber = editTextPhone.text?.toString().orEmpty().trim()
            val email = editTextEmail.text?.toString().orEmpty().trim()
            val deliveryAddress = editTextAddress.text?.toString().orEmpty().trim()
            val orderNote = editTextNote.text?.toString().orEmpty().trim()

            when {
                customerName.isBlank() -> {
                    showToast(context, context.getString(R.string.str_checkout_name_required))
                }

                phoneNumber.length < 10 -> {
                    showToast(context, context.getString(R.string.str_checkout_phone_required))
                }

                email.isBlank() -> {
                    showToast(context, context.getString(R.string.str_checkout_email_required))
                }

                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    showToast(context, context.getString(R.string.str_checkout_email_invalid))
                }

                deliveryAddress.isBlank() -> {
                    showToast(context, context.getString(R.string.str_checkout_address_required))
                }

                else -> {
                    onConfirm(
                        CheckoutDetails(
                            customerName = customerName,
                            phoneNumber = phoneNumber,
                            email = email,
                            deliveryAddress = deliveryAddress,
                            orderNote = orderNote
                        )
                    )
                    dialog.dismiss()
                }
            }
        }

        dialog.setContentView(view)
        dialog.show()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        dialog.behavior.skipCollapsed = true
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog
    }

}
