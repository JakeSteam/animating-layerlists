package uk.co.jakelee.layerlists

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.RotateDrawable
import android.graphics.drawable.VectorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    findViewById<Button>(R.id.button).setOnClickListener {
      val target = findViewById<ImageView>(R.id.imageview)
      startAnimation(target)
    }
  }

  private var animatorSet: AnimatorSet? = null
  private fun startAnimation(target: ImageView) {
    val animationLayers = target.drawable as LayerDrawable
    val moveImageViewLeft = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, -200f).setDuration(6000)
    val moveImageViewRight = ObjectAnimator.ofFloat(target, View.TRANSLATION_X, 0f).setDuration(6000)

    val redCircle = animationLayers.findDrawableByLayerId(R.id.red_circle) as GradientDrawable
    val redCircleFadeOut = ObjectAnimator.ofInt(redCircle, "alpha", 255, 100).setDuration(1000)
    val redCircleFadeIn = ObjectAnimator.ofInt(redCircle, "alpha", 100, 255).setDuration(100)

    val icon = animationLayers.findDrawableByLayerId(R.id.icon) as RotateDrawable
    val iconRotateLeft = ObjectAnimator.ofInt(icon, "level", 10000, 1000).setDuration(3000)
    val iconRotateRight = ObjectAnimator.ofInt(icon, "level", 1000, 10000).setDuration(300)

    val animationReset = { target.translationX = 0f }
    animatorSet?.cancel()
    animatorSet = AnimatorSet().apply {
      play(moveImageViewLeft)
        .with(redCircleFadeOut)
        .with(iconRotateLeft)
      play(redCircleFadeIn)
        .with(iconRotateRight)
        .after(iconRotateLeft)
      play(moveImageViewRight)
        .after(moveImageViewLeft)
      doOnCancel { animationReset.invoke() }
      start()
    }
  }
}