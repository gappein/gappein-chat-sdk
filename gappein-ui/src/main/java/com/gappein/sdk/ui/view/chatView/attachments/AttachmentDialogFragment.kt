package com.gappein.sdk.ui.view.chatView.attachments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gappein.sdk.ui.R
import com.gappein.sdk.ui.databinding.BottomSheetModalBinding
import com.gappein.sdk.ui.view.util.CameraOption
import com.gappein.sdk.ui.view.util.GalleryOption
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * An Attachment DialogFragment to provide the user various Attachment Options on a bottom sheet
 */

class AttachmentDialogFragment(private val onOptionClick: (String) -> Unit) :
    BottomSheetDialogFragment() {

    private var _binding: BottomSheetModalBinding? = null
    val binding: BottomSheetModalBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setStyle(DialogFragment.STYLE_NORMAL, R.style.OptionSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetModalBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.attachmentOptions.apply {
            adapter = AttachmentOptionsAdapter(options) {
                dismiss()
                onOptionClick(it)
            }
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }
}