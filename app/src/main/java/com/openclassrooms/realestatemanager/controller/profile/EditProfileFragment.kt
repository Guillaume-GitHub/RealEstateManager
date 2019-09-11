package com.openclassrooms.realestatemanager.controller.profile


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.model.entity.Agent
import com.openclassrooms.realestatemanager.viewModel.EstateViewModel
import kotlinx.android.synthetic.main.alert_dialog_pick_image_choice.view.*
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import java.io.File

class EditProfileFragment : Fragment(), View.OnClickListener {

    private lateinit var viewModel: EstateViewModel
    private lateinit var currentAgent: Agent
    private lateinit var photoPatch: String
    private var agentId: Long = -1
    private var isModification = false
    private var imageUri: Uri? = null

    companion object{
        const val ARG_UID = "uid"
        const val FRAGMENT_TAG = "edit_profile_frag_tag"
        private const val PERMISSION_REQUEST_CODE = 50
        private const val IMAGE_GALLERY_REQUEST_CODE = 100
        private const val CAMERA_REQUEST_CODE = 150
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        this.configureViewModel()
        this.getArgs(arguments)
        this.fragment_edit_profile_image_container.setOnClickListener(this)
    }

    //************************* MENU *****************************

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.valid_toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> activity?.onBackPressed()

            R.id.toolbar_valid_btn -> {
                if (isFormValid()) {
                    if (this.isModification) this.updateAgent()
                    else this.insertAgent()
                }
                else {
                    this.alertInvalidForm()
                }
            }
        }
        return true
    }

    //************************* DATABASE *************************

    // Insert New agent
    private fun insertAgent(){
        val agent = Agent(0, fragment_profile_first_name_text.text.toString(), fragment_profile_last_name_text.text.toString(), null)
        this.viewModel.insertAgent(agent)
    }

    // Update current agent
    private fun updateAgent(){
        val agent = Agent(this.currentAgent.uid, fragment_profile_first_name_text.text.toString(), fragment_profile_last_name_text.text.toString(), this.imageUri)

        if (this.currentAgent != agent) {
            this.currentAgent.name = fragment_profile_first_name_text.text.toString()
            this.currentAgent.surname = fragment_profile_last_name_text.text.toString()
            this.currentAgent.image = this.imageUri
            this.viewModel.updateAgent(agent)
        }
        activity?.onBackPressed()
    }

    // Get Agent From database
    private fun fetchAgent(id: Long){
        if (agentId != -1L) {
            this.viewModel.getAgent(id)?.observe(this, Observer { agent ->
                this.currentAgent = agent
                this.bind(this.currentAgent)
            })
        }
    }

    //Get view Model
    private fun configureViewModel() {
        val activity: ProfileActivity = activity as ProfileActivity
        this.viewModel = activity.viewModel
    }

    //************************* FROM VALIDATION *************************

    // Apply validations rules
    private fun checkForm(){
        fragment_profile_first_name_text.apply {
            if (text.isNullOrBlank()) error = getString(R.string.form_error_first_name)
            else error = null
        }

        fragment_profile_last_name_text.apply {
            if (text.isNullOrBlank()) error = getString(R.string.form_error_last_name)
            else error = null
        }
    }

    // Return true or false if form contain errors or not
     private fun isFormValid(): Boolean {
        this.checkForm()

        return  fragment_profile_first_name_text.error.isNullOrBlank() &&
                fragment_profile_last_name_text.error.isNullOrBlank()
    }

    private fun alertInvalidForm(){
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.dialog_invalid_form_agent_title))
                .setMessage(getString(R.string.dialog_invalid_form_message))
                .setPositiveButton("ok") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }

    //***************************************************************

    // BIND view if agent was fetched
    private fun bind(agent: Agent) {
        this.isModification = true
        fragment_profile_first_name_text.setText("${agent.name}")
        fragment_profile_last_name_text.setText("${agent.surname}")
        fragment_edit_profile_image.apply {
            if (agent.image != null) setImageURI(agent.image)
            else setImageResource(R.drawable.ic_account_circle_black_24dp)
        }
    }

    private fun getArgs(arguments: Bundle?){
        if (arguments != null){
            this.agentId = arguments.getLong(ARG_UID, -1)
            this.fetchAgent(this.agentId)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            fragment_edit_profile_image_container -> this.showAddingImageDialog()
            else -> { }
        }
    }

    //******************************* DIALOG ********************************

    private fun showAddingImageDialog() {
        if (this.checkPermission()) {
            // Create a builder
            val builder = AlertDialog.Builder(context!!)
            // Inflate custom alert dialog view
            val dialogLayout = LayoutInflater.from(context).inflate(R.layout.alert_dialog_pick_image_choice, null)
            // Set dialog Title and custom view
            builder.setTitle(getString(R.string.alert_dialog_pick_image_title)).setView(dialogLayout)
            // Create and Show dialog
            val dialog = builder.create()
            dialog.show()
            // Set Click listener on button's custom view
            // Start camera intent
            dialogLayout.alert_dialog_image_choice_camera.setOnClickListener {
                this.captureWithCamera()
                dialog.dismiss()
            }
            // Start Image Gallery intent
            dialogLayout.alert_dialog_image_choice_gallery.setOnClickListener {
                this.pickImageGallery()
                dialog.dismiss()
            }
        }
        else {
            this.requestPermission()
        }
    }

    //******************************* INTENT IMAGE/CAMERA  ********************************

    // Create intent and start activity to pick image from gallery
    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ("image/*")
        val mimeTypes: Array<String> = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        startActivityForResult(intent, IMAGE_GALLERY_REQUEST_CODE)
    }

    // Create intent and start activity to pick image from camera
    private fun captureWithCamera() {
        val uri = FileProvider.getUriForFile(context!!,"com.openclassrooms.realestatemanager.fileprovider", this.getFile())
        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE).putExtra(MediaStore.EXTRA_OUTPUT,uri), CAMERA_REQUEST_CODE)
    }

    private fun getFile(): File {
        return Utils.createFilePath(context).apply {
            photoPatch = absolutePath
        }
    }

    //******************************* PERMISSIONS REQUEST ********************************

    // Check Camera And Storage Permissions
    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context!!,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
    }

    // Display Permissions dialog to users
    private fun requestPermission() {
        requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
    }

    // Catch requestPermission() result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        &&grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    this.showAddingImageDialog()

                } else { Toast.makeText(context,getString(R.string.permission_denied), Toast.LENGTH_SHORT).show() }
                return
            }
            else -> { }
        }
    }

    //******************************* ACTIVITY RESULT ********************************

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {

            IMAGE_GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        val path = Utils.getRealPathFromURI(context, data.data)
                        val uri = Uri.parse(path)
                        imageUri = uri
                        fragment_edit_profile_image.setImageURI(this.imageUri)
                    }
                }
            }

            CAMERA_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = Uri.parse(photoPatch)
                    fragment_edit_profile_image.setImageURI(this.imageUri)
                }
            }
        }
    }
}
