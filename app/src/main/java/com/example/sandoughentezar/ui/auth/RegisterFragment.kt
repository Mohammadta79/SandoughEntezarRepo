package com.example.sandoughentezar.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentRegisterBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.regex.Pattern


@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentRegisterBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val GET_PASSPORT_PIC = 1
    private val GET_CARD_PIC = 2
    private val PASSPORT_PERMISSION_CODE = 3
    private val CARD_PERMISSION_CODE = 4
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    var card_file: File? = null
    var passport_file: File? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnNextPage.setOnClickListener(this)
        binding.txtAccount.setOnClickListener {
            if (binding.cardAccount.visibility == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(
                    binding.rootCard,
                    AutoTransition()
                )
                binding.cardAccount.visibility = View.GONE
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.rootCard,
                    AutoTransition()
                )
                binding.cardAccount.visibility = View.VISIBLE
            }
        }
        binding.txtPersonalInfo.setOnClickListener {
            if (binding.cardPersonal.visibility == View.VISIBLE) {

                TransitionManager.beginDelayedTransition(
                    binding.rootPersonal,
                    AutoTransition()
                )
                binding.cardPersonal.visibility = View.GONE
            } else {
                TransitionManager.beginDelayedTransition(
                    binding.rootPersonal,
                    AutoTransition()
                )
                binding.cardPersonal.visibility = View.VISIBLE
            }
        }
        binding.txtPassport.setOnClickListener {
            requestPassportPermission()
        }
        binding.txtNationalCard.setOnClickListener {
            requestCardPermission()
        }
        binding.agreementChb.setOnClickListener {
            if (binding.agreementChb.isChecked) {
                enableRegister(1)
            } else {
                enableRegister(0)
            }
        }

    }

    private fun enableRegister(state: Int) {
        when (state) {
            0 -> {
                binding.btnNextPage.isEnabled = false
                binding.btnNextPage.setBackgroundResource(R.drawable.shape_btn_login_disable)
            }
            1 -> {
                binding.btnNextPage.isEnabled = true
                binding.btnNextPage.setBackgroundResource(R.drawable.shape_btn_login_enable)
            }
        }
    }

    fun isConnected(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    private fun setupWaitDialog() {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        bottomSheetView = layoutInflater.inflate(
            R.layout.dialog_wait_for_accept,
            requireActivity().findViewById<LinearLayout>(R.id.root_wait)
        )
        bottomSheetDialog!!.setContentView(bottomSheetView!!)
        bottomSheetDialog!!.show()
    }

    @Suppress("DEPRECATION")
    private fun pickPassportImage() {
        var intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GET_PASSPORT_PIC)
    }

    @Suppress("DEPRECATION")
    private fun pickCardImage() {
        var intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent, GET_CARD_PIC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GET_PASSPORT_PIC -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        var selectedImageUri: Uri? = data.data
                        val path = getPathFromURI(selectedImageUri)
                        if (path != null) {
                            passport_file = File(path)
                            binding.txtPassport.text = passport_file!!.name
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            GET_CARD_PIC -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    try {
                        var selectedImageUri: Uri? = data.data
                        val path = getPathFromURI(selectedImageUri)
                        if (path != null) {
                            card_file = File(path)
                            binding.txtNationalCard.text = card_file!!.name
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PASSPORT_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    pickPassportImage()
                } else {
                    requestPassportPermission()
                }
            }
            CARD_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    pickCardImage()
                } else {
                    requestCardPermission()
                }
            }
        }
    }

    private fun requestPassportPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PASSPORT_PERMISSION_CODE
            )
        } else {
            pickPassportImage()
        }
    }

    private fun requestCardPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                PASSPORT_PERMISSION_CODE
            )
        } else {
            pickCardImage()
        }
    }


    private fun getPathFromURI(contentUri: Uri?): String? {
        var res: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor =
            requireActivity().contentResolver.query(contentUri!!, proj, null, null, null)!!
        if (cursor.moveToFirst()) {
            val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            res = cursor.getString(column_index)
        }
        cursor.close()
        return res
    }


    private fun register() {
        if (!checkInput()) {
            Toast.makeText(
                requireContext(),
                "لطفا فیلد های ستاره دار را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidNationalID()) {
            Toast.makeText(
                requireContext(),
                "لطفا کد ملی 10 رقمی خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidMobile(binding.edtMobileNumber1.text.toString())) {
            Toast.makeText(
                requireContext(),
                "لطفا شماره موبایل خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidPostalCode()) {
            Toast.makeText(
                requireContext(),
                "لطفا کد پستی 10 رقمی خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else if (binding.edtMobileNumber2.text.isNotBlank() && (!isValidMobile(binding.edtMobileNumber2.text.toString()))) {

            Toast.makeText(
                requireContext(),
                "لطفا شماره موبایل دوم خود را به درستی وارد نمایید",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            binding.progressBar.showProgressBar()
            authViewModel.register(
                getBodyParams()
            ).observe(viewLifecycleOwner) {
                when (it.status) {

                    Status.Success -> {
                        binding.progressBar.hideProgressBar()
                        binding.btnNextPage.isEnabled = true

                        when (it.data!!.statusCode) {
                            200 -> {
                                findNavController().navigate(
                                    R.id.action_registerFragment_self,
                                )
                                setupWaitDialog()
                            }
                            500 -> {
                                Toast.makeText(
                                    requireContext(),
                                    "این اطلاعات از قبل ثبت شده است",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            400 -> {
                                Toast.makeText(
                                    requireContext(),
                                    "خطا در تایید اطلاعات وارد شده",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {
                                Toast.makeText(
                                    requireContext(),
                                    "خطا در برقراری ارتباط با سرور",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }


                    }
                    Status.Loading -> {
                        binding.progressBar.showProgressBar()
                        binding.btnNextPage.isEnabled = false
                    }
                    Status.Failure -> {
                        binding.progressBar.hideProgressBar()
                        binding.btnNextPage.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            "خطا در برقراری ارتباط با سرور",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        }
    }


    private fun checkInput(): Boolean {

        return !(binding.edtFullName.text.isBlank() || binding.edtFullName.text.isBlank() || binding.edtMobileNumber1.text.isBlank()
                || binding.edtPostalCode.text.isBlank() || binding.edtAddress.text.isBlank() || binding.edtAccountNumber.text.isBlank()
                || binding.edtCardNumber1.text.isBlank() || binding.edtShaba.text.isBlank())
//
    }

//    private fun register() {
//        if (!checkInput()) {
//            Toast.makeText(
//                requireContext(),
//                "لطفا تمامی فیلدهای ستاره دار را وارد نمایید",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//        authViewModel.register(
//            getBodyParams()
//        ).observe(viewLifecycleOwner) {
//            when (it.status) {
//
//                Status.Success -> {
//                   // binding.progressBar.hideProgressBar()
//                    binding.btnNextPage.isEnabled = true
//
//                    when (it.data!!.statusCode) {
//                        200 -> {
////                            var bundle = Bundle()
////                            bundle.putString("national_id", national_id)
////                            findNavController().navigate(
////                                R.id.action_accountDataFragment2_to_uploadImageFragment2,
////                                bundle
////                            )
//                        }
//                        500 -> {
//                            Toast.makeText(
//                                requireContext(),
//                                "این اطلاعات از قبل ثبت شده است",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        400 -> {
//                            Toast.makeText(
//                                requireContext(),
//                                "خطا در تایید اطلاعات وارد شده",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        else -> {
//                            Toast.makeText(
//                                requireContext(),
//                                "خطا در برقراری ارتباط با سرور",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//
//
//                }
//                Status.Loading -> {
//                    binding.progressBar.showProgressBar()
//                    binding.btnRegister.isEnabled = false
//                }
//                Status.Failure -> {
//                    binding.progressBar.hideProgressBar()
//                    binding.btnRegister.isEnabled = true
//                    Toast.makeText(
//                        requireContext(),
//                        "خطا در برقراری ارتباط با سرور",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
//        }
//    }

    private fun isValidNationalID(): Boolean {
        return binding.edtNationalId.text.trim().length == 10
    }

    private fun isValidMobile(phone: String): Boolean {
        var regex = "^(\\+98|0)?9\\d{9}\$"
        val pattern: Pattern = Pattern.compile(regex)
        return pattern.matcher(phone).matches()
    }

    private fun isValidPostalCode(): Boolean {
        return binding.edtPostalCode.text.trim().length == 10
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.btnNextPage.id -> {
                register()
            }
        }
    }

    private fun getBodyParams(): RequestBody {

        var mobile2: String? = if (binding.edtMobileNumber2.text != null) {
            binding.edtMobileNumber2.text.toString()
        } else {
            "null"
        }
        var phone: String? = if (binding.edtPhoneNumber.text != null) {
            binding.edtPhoneNumber.text.toString()
        } else {
            "null"
        }

        var card2: String? = if (binding.edtCardNumber2.text.isNotBlank()) {
            binding.edtCardNumber2.text.toString()
        } else {
            "null"
        }

        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("name", binding.edtFullName.text.toString())
            .addFormDataPart("national_id", binding.edtNationalId.text.toString())
            .addFormDataPart("mobile1", binding.edtMobileNumber1.text.toString())
            .addFormDataPart("postal_code", binding.edtPostalCode.text.toString())
            .addFormDataPart("address", binding.edtAddress.text.toString())
            .addFormDataPart("account_number", binding.edtAccountNumber.text.toString())
            .addFormDataPart("card1", binding.edtCardNumber1.text.toString())
            .addFormDataPart("shaba", binding.edtShaba.text.toString())
            .addFormDataPart("mobile2", mobile2!!)
            .addFormDataPart("phone", phone!!)
            .addFormDataPart("card2", card2!!)
            .addPart(
                MultipartBody.Part.createFormData(
                    "card_image",
                    card_file!!.name,
                    card_file!!.asRequestBody("image".toMediaTypeOrNull())
                )
            ).addPart(
                MultipartBody.Part.createFormData(
                    "passport_image",
                    passport_file!!.name,
                    passport_file!!.asRequestBody("image".toMediaTypeOrNull())
                )
            )
            .build()
    }


}