package com.gappein.sdk.ui.view.chatView.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.view.util.AttachmentUtils.AttachmentOptions.*
import com.gappein.sdk.ui.view.util.CameraOption
import com.gappein.sdk.ui.view.util.GalleryOption
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_modal.*

/**
 * An Attachment DialogFragment to provide the user various Attachment Options on a bottom sheet
 */

class AttachmentDialogFragment(private val onOptionClick: (String) -> Unit) :
    BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_modal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val options = arrayListOf(
            CameraOption().optionName,
            GalleryOption().optionName
        )

        attachmentOptions.apply {
            adapter = AttachmentOptionsAdapter(options, onOptionClick)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}