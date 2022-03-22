package com.kemsky.dipaypokedex.helper

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .into(this)
}

fun ImageView.setImageSrcFromUrlWithLoader(url: String?, loader: LottieAnimationView) {
    loader.playAnimation()
    url?.let {
        Glide.with(this)
            .load(it)
            .placeholder(loader.drawable)
            .addListener(imageLoadingListener(loader))
            .centerCrop()
            .into(this)
    }
}

fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?> {
    return object : RequestListener<Drawable?> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            pendingImage.pauseAnimation()
            pendingImage.visibility = View.GONE
            return false
        }
    }
}

fun Number?.hectogramsToKilograms(): Double? {
    return this?.toDouble()?.div(10)
}

fun Number?.decimetersToMeters(): Double? {
    return this?.toDouble()?.div(10)
}