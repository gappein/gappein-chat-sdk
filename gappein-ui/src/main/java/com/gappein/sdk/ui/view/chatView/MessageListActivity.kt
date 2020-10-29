package com.gappein.sdk.ui.view.chatView

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.client.ChatClient
import com.gappein.sdk.model.Message
import com.gappein.sdk.model.User
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.base.ChatBaseView
import com.gappein.sdk.ui.view.chatView.adapter.MessageListAdapter
import com.gappein.sdk.ui.view.chatView.attachments.AttachmentDialogFragment
import com.gappein.sdk.ui.view.chatView.imageviewer.openImage
import com.gappein.sdk.ui.view.chatView.util.gifSelectionListener
import com.gappein.sdk.ui.view.chatView.util.giphySettings
import com.gappein.sdk.ui.view.util.*
import com.gappein.sdk.ui.view.util.AttachmentUtils.AttachmentOptions.*
import com.giphy.sdk.ui.views.GiphyDialogFragment
import kotlinx.android.synthetic.main.activity_message.*
import java.io.File
import java.io.IOException


class MessageListActivity : AppCompatActivity(), ChatBaseView {

    private var photoFile: File? = null
    private lateinit var adapter: MessageListAdapter
    private val chats = mutableListOf<Message>()

    companion object {
        private const val REQUEST_TAKE_PHOTO = 1
        private const val REQUEST_GALLERY_PHOTO = 2
        private const val CHANNEL_ID = "channelId"
        private const val RECEIVER = "receiver"
        private const val GIF_DIALOG = "gifs_dialog"
        private const val DEFAULT_STRING = ""
        private val EMPTY_USER = User()
        private const val CAMERA_PERMISSION_CODE = 100
        private const val GALLERY_PERMISSION_CODE = 101
        private const val TAG = "MessageActivity"

        /**
         * Returns intent of MessageListActivity
         *
         */
        @JvmStatic
        fun buildIntent(context: Context, channelId: String, receiver: User) =
            Intent(context, MessageListActivity::class.java).apply {
                putExtra(CHANNEL_ID, channelId)
                putExtra(RECEIVER, receiver)
            }
    }

    private val channelId by lazy { intent.getStringExtra(CHANNEL_ID) ?: DEFAULT_STRING }
    private val receiver by lazy { intent.getParcelableExtra(RECEIVER) ?: EMPTY_USER }
    private val currentUser by lazy { ChatClient.getInstance().getUser() }
    private val currentApiKey by lazy { ChatClient.getInstance().getApiKey() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        setupUI()
        setupRecyclerView()
        fetchMessages()
        setupGifMessageListener()
        setupSendMessageListener()
        setupTextChangeListener()
    }

    private fun setupTextChangeListener() {
        editTextChatBox.addTypeChangeListener { isUserTyping ->
            if (isUserTyping) {
                ChatClient.getInstance().setTypingStatus(channelId, currentUser.token, true) {
                    //handle onSuccess
                }
            } else {
                ChatClient.getInstance().setTypingStatus(channelId, currentUser.token, false) {
                    //handle onSuccess
                }
            }
        }
    }

    private fun setupUI() {
        toolbar.init {
            channelId
        }
    }

    private fun setupSendMessageListener() {
        buttonSend.setOnClickListener {
            val message = editTextChatBox.text.toString()
            if (message.isNotEmpty()) {
                ChatClient.getInstance().sendMessage(message, receiver.token, {
                    editTextChatBox.text.clear()
                }, {

                })
            }
        }
        toolbar.setOnBackPressed {
            onBackPressed()
        }

        imageButtonAttach.setOnClickListener {
            AttachmentDialogFragment { option ->
                onOptionSelected(option)
            }.show(supportFragmentManager, "AttachmentFragment")
        }
    }

    private fun onOptionSelected(option: String) {
        when (option) {
            CameraOption().optionName -> Manifest.permission.CAMERA.checkForPermission(CAMERA_PERMISSION_CODE)
            GalleryOption().optionName -> Manifest.permission.WRITE_EXTERNAL_STORAGE.checkForPermission(GALLERY_PERMISSION_CODE)
        }
    }

    private fun setupGifMessageListener() {
        if (currentApiKey.isNotEmpty()) {
            gifSend.setOnClickListener {
                val gifDialog = GiphyDialogFragment.newInstance(giphySettings)
                gifDialog.gifSelectionListener = gifSelectionListener {
                    ChatClient.getInstance().sendMessage("giphy $it", receiver.token, {
                        gifDialog.dismiss()
                    }, {

                    })
                }
                gifDialog.show(supportFragmentManager, GIF_DIALOG)
            }
        } else {
            gifSend.hide()
        }
    }

    private fun String.checkForPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MessageListActivity,
                this
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this@MessageListActivity, arrayOf(this), requestCode)
        } else {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> {
                    dispatchTakePictureIntent()
                }
                GALLERY_PERMISSION_CODE -> {
                    dispatchGalleryIntent()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = MessageListAdapter(chatClient = ChatClient.getInstance(), onImageClick = {
            openImage(this, it)
        }, onMessageClick = {
            ChatClient.getInstance().deleteMessage(channelId, it) {
                //Handle onSuccess of Delete
            }
        }, onMessageLike = {
            ChatClient.getInstance().likeMessage(channelId, it) {

            }
        })
        recyclerViewMessages.layoutManager = LinearLayoutManager(this@MessageListActivity)
        recyclerViewMessages.adapter = adapter

    }

    private fun fetchMessages() {
        ChatClient.getInstance().getMessages(channelId) {
            chats.run {
                clear()
                addAll(it)
                adapter.addAll(this)
                if (this.isNotEmpty()) {
                    recyclerViewMessages.smoothScrollToPosition(this.size - 1)
                }
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.permission_deined),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(packageManager)?.let {
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
            photoFile?.let { file ->
                val photoURI: Uri = FileProvider.getUriForFile(this, "Gappein.provider", file)
                this.photoFile = file
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun dispatchGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_GALLERY_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_TAKE_PHOTO) {
                sendImageMessage(photoFile)

            } else if (requestCode == REQUEST_GALLERY_PHOTO) {
                val selectedImage = data?.data
                photoFile = File(getRealPathFromUri(selectedImage))
                sendImageMessage(photoFile)
            }
        }
    }

    private fun sendImageMessage(photo: File?) {
        photo?.let { file ->
            ImageCompressor(this).compressToFile(file)?.toUri()?.let {
                ChatClient.getInstance().sendMessage(it, receiver.token, {
                    progress.hide()
                }, {
                    progress.show()
                }, {
                    progress.hide()
                })
            }
        }
    }

    override fun getClient() = ChatClient.getInstance()
}