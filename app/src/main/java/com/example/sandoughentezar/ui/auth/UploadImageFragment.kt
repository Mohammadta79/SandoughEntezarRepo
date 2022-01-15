package com.example.sandoughentezar.ui.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.sandoughentezar.R
import com.example.sandoughentezar.api.state.Status
import com.example.sandoughentezar.databinding.FragmentUploadImageBinding
import com.example.sandoughentezar.viewModels.AuthViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class UploadImageFragment : Fragment() {

    private lateinit var binding: FragmentUploadImageBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private val GET_PASSPORT_PIC = 1
    private val GET_CARD_PIC = 2
    private val PASSPORT_PERMISSION_CODE = 3
    private val CARD_PERMISSION_CODE = 4
    var bottomSheetDialog: BottomSheetDialog? = null
    var bottomSheetView: View? = null
    private var national_id: String? = null

    var card_file: File? = null
    var passport_file: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            national_id = it["national_id"].toString()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnUpload.setOnClickListener {
            uploadImage()
        }
        binding.txtPassport.setOnClickListener {
            requestPassportPermission()
        }
        binding.txtNationalCard.setOnClickListener {
            requestCardPermission()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUploadImageBinding.inflate(inflater, container, false)
        return binding.root
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

    private fun uploadImage() {
        authViewModel.uploadImages(getBodyParams())
            .observe(viewLifecycleOwner) {
                when (it.status) {

                    Status.Success -> {
                        binding.progressBar.hideProgressBar()
                        binding.btnUpload.isEnabled = true
                        Log.d("uploadImage", it.toString())
                        when (it.data!!.statusCode) {
                            200 -> {
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
                        binding.btnUpload.isEnabled = false
                    }
                    Status.Failure -> {
                        Log.d("uploadImage", it.msg)

                        binding.progressBar.hideProgressBar()
                        binding.btnUpload.isEnabled = true
                        Toast.makeText(
                            requireContext(),
                            "خطا در برقراری ارتباط با سرور",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
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

    private fun getBodyParams(): RequestBody {

        return MultipartBody.Builder()
            .setType(MultipartBody.FORM)
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
            ).addFormDataPart("national_id", "6570066579")
//            .addPart(
//                passport_file!!.asRequestBody()
//            )
//            .addPart(
//                card_file!!.asRequestBody()
//
//            ).addFormDataPart("national_id", "6570066579")
            .build()
    }

    //Part.createFormData

}