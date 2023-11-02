package de.ticktrax.ticktrax_geo.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import de.ticktrax.ticktrax_geo.R
import de.ticktrax.ticktrax_geo.databinding.FragmentExport2MailBinding
import de.ticktrax.ticktrax_geo.myTools.ExportViaMail
import de.ticktrax.ticktrax_geo.myTools.logDebug
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.lang.Exception

class Export2Mail_Fragment : Fragment() {
    private lateinit var binding: FragmentExport2MailBinding
    private val viewModel: TickTraxViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentExport2MailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextFAB.setOnClickListener {
            val navController = findNavController()
            //navController.navigate(Export_FragmentDirections.actionExportFragment2ToHomeFragment2())
            navController.navigate(R.id.home_Fragment)
        }
        binding.prevFAB.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.GEO_Fragment)
        }
        val data = viewModel.osmPlaceS.value
        logDebug("ufe-export", data.toString())

        binding.exportDataTV?.text = data.toString()

        try {
            val externalFilesDir =
                requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val gson = Gson()
            val myGson = gson.toJson(data.toString())
            binding.exportDataTV?.text = myGson
             //Export the data
              val jsonFile = createTempJsonFile(myGson)
            //  sendEmail(jsonFile)
            val myExport = ExportViaMail(data!!, requireContext())
            Toast.makeText(requireContext(), "myExport created", Toast.LENGTH_SHORT).show()
            Toast.makeText(requireContext(),externalFilesDir.toString(),Toast.LENGTH_LONG)
            val myIntent: Intent? = myExport.exportAll(externalFilesDir)
            Toast.makeText(requireContext(), "Intent created", Toast.LENGTH_SHORT).show()
            if (myIntent != null) {
                // Erfolgsfall
                startActivity(Intent.createChooser(myIntent, "Send Email"))
            } else {
                // Misserfolgsfall
            }
        } catch (e: Exception) {
            binding.exportDataTV?.text = e.toString()
            var myString: String
            myString = e.toString()
            Toast.makeText(requireContext(), myString, Toast.LENGTH_SHORT).show()
        }
    }


    private fun createTempJsonFile(jsonContent: String): Uri {
        val externalFilesDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val file = File(externalFilesDir, "temp_json_file.json")
        try {
            FileWriter(file).use { writer ->
                writer.write(jsonContent)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Use FileProvider to get a content URI
        return FileProvider.getUriForFile(
            requireContext(),
            "de.ticktrax.ticktrax_geo.fileprovider",
            file
        )
    }

    private fun sendEmail(jsonFileUri: Uri) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "application/json"

        // Add the email address you want to send the email to
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("ufe@hembach1.de"))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "JSON File Attachment")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Please find the attached JSON file.")

        // Add the JSON file as an attachment
        emailIntent.putExtra(Intent.EXTRA_STREAM, jsonFileUri)

        // Ensure that the app is selected to send the email
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        // Start the email activity
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }


}