package mrkinfotech.Grocio.ui.base

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Property
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import mrkinfotech.Grocio.R
import mrkinfotech.Grocio.databinding.ActivitySplashBinding
import mrkinfotech.Grocio.ui.home.HomeMainActivity
import mrkinfotech.Grocio.ui.login.LoginActivity
import mrkinfotech.Grocio.utils.PreferenceHelper

class SplashActivity : AppCompatActivity() {

    companion object {
        private const val SPLASH_DURATION_MS = 2100L
        private const val STATUS_START_DELAY_MS = 320L
        private const val STATUS_STEP_DELAY_MS = 460L
    }

    private lateinit var binding: ActivitySplashBinding
    private val handler = Handler(Looper.getMainLooper())
    private val runningAnimators = mutableListOf<Animator>()
    private val statusRunnables = mutableListOf<Runnable>()
    private var hasNavigated = false

    private val navigateRunnable = Runnable {
        navigateToNextScreen()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            navigateToNextScreen()
        }
        binding.cardSplash.setOnClickListener {
            navigateToNextScreen()
        }
    }

    override fun onResume() {
        super.onResume()
        prepareViews()
        startSplashExperience()
    }

    override fun onPause() {
        clearScheduledWork()
        stopAnimations()
        super.onPause()
    }

    override fun onDestroy() {
        clearScheduledWork()
        stopAnimations()
        super.onDestroy()
    }

    private fun prepareViews() {
        binding.cardSplash.alpha = 0f
        binding.cardSplash.translationY = resources.getDimension(R.dimen.margin_24)
        binding.textSplashTapHint.alpha = 0f
        binding.textSplashFooter.alpha = 0f
        binding.textSplashStatus.alpha = 0f
        binding.textSplashBadge.alpha = 0f
        binding.viewSplashRing.alpha = 0f
        binding.viewSplashProgressFill.scaleX = 0f
        binding.viewSplashProgressFill.alpha = 0.88f
        binding.viewSplashHalo.scaleX = 0.92f
        binding.viewSplashHalo.scaleY = 0.92f
        binding.viewSplashPulseDot.scaleX = 0.88f
        binding.viewSplashPulseDot.scaleY = 0.88f
        binding.textSplashStatus.text = getString(R.string.str_splash_status_one)

        listOf(
            binding.textSplashChipFast,
            binding.textSplashChipDaily,
            binding.textSplashChipLocal
        ).forEach {
            it.alpha = 0f
        }
    }

    private fun startSplashExperience() {
        clearScheduledWork()
        stopAnimations()
        startEntranceAnimation()
        startAmbientLoops()
        scheduleStatusUpdates()
        handler.postDelayed(navigateRunnable, SPLASH_DURATION_MS)
    }

    private fun startEntranceAnimation() {
        binding.viewSplashProgressFill.doOnLayout {
            binding.viewSplashProgressFill.pivotX = 0f
        }

        runningAnimators += AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(binding.cardSplash, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(
                    binding.cardSplash,
                    View.TRANSLATION_Y,
                    binding.cardSplash.translationY,
                    0f
                ),
                ObjectAnimator.ofFloat(binding.textSplashBadge, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(binding.viewSplashRing, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(binding.textSplashStatus, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(binding.textSplashFooter, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(binding.textSplashTapHint, View.ALPHA, 0f, 1f)
            )
            duration = 700L
            interpolator = OvershootInterpolator(0.9f)
            start()
        }

        animateChipEntry(binding.textSplashChipFast, 120L, 18f, -12f)
        animateChipEntry(binding.textSplashChipDaily, 220L, -18f, 14f)
        animateChipEntry(binding.textSplashChipLocal, 320L, -24f, -6f)

        runningAnimators += ObjectAnimator.ofFloat(binding.viewSplashProgressFill, View.SCALE_X, 0f, 1f).apply {
            duration = 1500L
            startDelay = 220L
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
    }

    private fun animateChipEntry(
        view: View,
        startDelay: Long,
        fromTranslationX: Float,
        fromTranslationY: Float
    ) {
        view.translationX = fromTranslationX
        view.translationY = fromTranslationY

        runningAnimators += AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_X, fromTranslationX, 0f),
                ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, fromTranslationY, 0f)
            )
            duration = 520L
            this.startDelay = startDelay
            interpolator = OvershootInterpolator(1f)
            start()
        }
    }

    private fun startAmbientLoops() {
        runningAnimators += AnimatorSet().apply {
            playTogether(
                loopingAnimator(binding.viewSplashHalo, View.SCALE_X, 0.92f, 1.08f, 1800L),
                loopingAnimator(binding.viewSplashHalo, View.SCALE_Y, 0.92f, 1.08f, 1800L)
            )
            start()
        }

        runningAnimators += loopingAnimator(
            binding.layoutSplashLogoPlate,
            View.TRANSLATION_Y,
            0f,
            -16f,
            1900L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashPulseDot,
            View.SCALE_X,
            0.88f,
            1.18f,
            820L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashPulseDot,
            View.SCALE_Y,
            0.88f,
            1.18f,
            820L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashPulseDot,
            View.ALPHA,
            0.6f,
            1f,
            820L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashOrbTop,
            View.ROTATION,
            -4f,
            8f,
            5600L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashOrbMiddle,
            View.TRANSLATION_Y,
            -10f,
            12f,
            2400L
        ).apply { start() }

        runningAnimators += loopingAnimator(
            binding.viewSplashOrbBottom,
            View.TRANSLATION_X,
            -12f,
            18f,
            3000L
        ).apply { start() }
    }

    private fun loopingAnimator(
        target: View,
        property: Property<View, Float>,
        startValue: Float,
        endValue: Float,
        duration: Long
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(target, property, startValue, endValue).apply {
            this.duration = duration
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = LinearInterpolator()
        }
    }

    private fun scheduleStatusUpdates() {
        val statuses = listOf(
            getString(R.string.str_splash_status_one),
            getString(R.string.str_splash_status_two),
            getString(R.string.str_splash_status_three)
        )

        statuses.forEachIndexed { index, status ->
            val runnable = Runnable {
                updateStatus(status)
            }
            statusRunnables += runnable
            handler.postDelayed(
                runnable,
                STATUS_START_DELAY_MS + (index * STATUS_STEP_DELAY_MS)
            )
        }
    }

    private fun updateStatus(status: String) {
        binding.textSplashStatus.animate().cancel()
        binding.textSplashStatus.animate()
            .alpha(0f)
            .setDuration(120L)
            .withEndAction {
                binding.textSplashStatus.text = status
                binding.textSplashStatus.animate()
                    .alpha(1f)
                    .setDuration(220L)
                    .start()
            }
            .start()
    }

    private fun navigateToNextScreen() {
        if (hasNavigated || isFinishing || isDestroyed) return
        hasNavigated = true
        clearScheduledWork()
        stopAnimations()

        val destination = when {
            PreferenceHelper.getOnBoardShow(this) && PreferenceHelper.isUserLoggedIn(this) ->
                Intent(this, HomeMainActivity::class.java)
            PreferenceHelper.getOnBoardShow(this) ->
                Intent(this, LoginActivity::class.java)
            else ->
                Intent(this, OnBoardingActivity::class.java)
        }

        startActivity(destination)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun clearScheduledWork() {
        handler.removeCallbacks(navigateRunnable)
        statusRunnables.forEach(handler::removeCallbacks)
        statusRunnables.clear()
    }

    private fun stopAnimations() {
        binding.root.animate().cancel()
        binding.cardSplash.animate().cancel()
        binding.textSplashStatus.animate().cancel()
        runningAnimators.forEach { it.cancel() }
        runningAnimators.clear()
    }
}
