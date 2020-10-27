package com.gappein.sdk.ui.view.util

sealed class Options

class CameraOption(val optionName: String = "Camera") : Options()
class GalleryOption(val optionName: String = "Gallery") : Options()