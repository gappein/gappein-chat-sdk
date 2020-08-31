package com.gappein.sdk.ui.chatView.bottompicker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gappein.sdk.ui.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.item_picker.view.*


class ImagePicker : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ImagePicker"

        @JvmStatic
        fun newInstance() = ImagePicker()
    }

    private lateinit var itemClickListener: ItemClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            itemClickListener = context as ItemClickListener
        } else {
            throw RuntimeException(
                context.toString().toString() + " must implement ItemClickListener"
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.item_picker, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.cardCameraPicker.setOnClickListener {
            itemClickListener.onCameraClick()
            dismiss()
        }

        view.cardGalleryPicker.setOnClickListener {
            itemClickListener.onGalleryClick()
            dismiss()
        }
    }

    interface ItemClickListener {
        fun onCameraClick()
        fun onGalleryClick()
    }
}