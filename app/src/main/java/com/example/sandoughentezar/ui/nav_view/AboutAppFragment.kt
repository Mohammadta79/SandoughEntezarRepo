package com.example.sandoughentezar.ui.nav_view

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.sandoughentezar.R
import dagger.hilt.android.AndroidEntryPoint


class AboutAppFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_app, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeTextColor()
        requireActivity().findViewById<TextView>(R.id.txt_app_tutorial).setOnClickListener {
            val browserIntent = Intent(
                //TODO:set video url
                Intent.ACTION_VIEW,
                Uri.parse("")
            )
            startActivity(browserIntent)
        }
        requireActivity().findViewById<CardView>(R.id.contact_us_mobile_developer)
            .setOnClickListener {
                callMobileNumber("+989164625214")

            }
        requireActivity().findViewById<CardView>(R.id.contact_us_email_developer)
            .setOnClickListener {
                sendEmail("topkook@yahoo.com")
            }
        requireActivity().findViewById<CardView>(R.id.contact_us_insta_developer)
            .setOnClickListener {
                instagramIntent("topkook.official")
            }
        requireActivity().findViewById<CardView>(R.id.contact_us_whatsapp_developer)
            .setOnClickListener {
                whatsappIntent("+989164625214")
            }
        requireActivity().findViewById<CardView>(R.id.contact_us_telegram_developer)
            .setOnClickListener {
                telegramIntent("topkook_ir")
            }
        requireActivity().findViewById<TextView>(R.id.txt_about_tk).setOnClickListener {
            val browserIntent = Intent(
                //TODO:set video url
                Intent.ACTION_VIEW,
                Uri.parse("https://topkook.ir/")
            )
            startActivity(browserIntent)
        }

    }

    private fun callMobileNumber(mobile: String) {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$mobile")
        requireActivity().startActivity(callIntent)

    }


    private fun sendEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data =
            Uri.parse("mailto:$email")
        intent.putExtra(Intent.EXTRA_SUBJECT, " موسسه انتظار")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun changeTextColor() {
        var first = "کلیه حقوق تولید و توسعه نرم افزار متعلق به "
        var middle = "<font color='#2F60F6'>تاپ کوک</font>"
        var last = " می باشد. "
        requireActivity().findViewById<TextView>(R.id.txt_about_tk).text =
            Html.fromHtml(first + middle + last)
    }

    private fun telegramIntent(telegram_id: String) {

        try {
            try {
                requireActivity().packageManager.getPackageInfo(
                    "org.telegram.messenger",
                    0
                )//Check for Telegram Messenger App
            } catch (e: Exception) {
                requireActivity().packageManager.getPackageInfo(
                    "org.thunderdog.challegram",
                    0
                )//Check for Telegram X App
            }
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("tg://resolve?domain=${telegram_id}")
                )
            )
        } catch (e: Exception) { //App not found open in browser
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.telegram.me/$telegram_id")
                )
            )
        }

    }

    private fun whatsappIntent(whatsapp_id: String) {
        val url = "https://wa.me/$whatsapp_id"
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        startActivity(i)
    }

    private fun instagramIntent(instagram_id: String) {
        val uri = Uri.parse("http://instagram.com/_u/$instagram_id")
        val likeIng = Intent(Intent.ACTION_VIEW, uri)

        likeIng.setPackage("com.instagram.android")

        try {
            startActivity(likeIng)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/$instagram_id")
                )
            )
        }
    }

}