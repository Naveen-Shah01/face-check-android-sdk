package io.surepass.facescanner.onboarding

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import io.surepass.facescanner.CameraActivity
import io.surepass.facescanner.R
import io.surepass.facescanner.databinding.ActivityMainBinding
import kotlin.math.abs


//TODO handle the shared preference
class MainActivity : AppCompatActivity() {
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var indicatorContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for the shared preference
        if (onBoardingFinished()) {
            navigateToCameraActivity()
        }

        // Items for adapter that will be shown
        setOnboardingItems()

        //indicator when items changes in viewPager
        setupIndicators()
        setCurrentIndicator(0)

        // Set the PageTransformer for animations in viewpager
        binding.viewPager.setPageTransformer(MyPageTransformer())

    }

    private fun setOnboardingItems() {
        val onBoardingRulesProvider = OnBoardingRulesProvider(this)
        onBoardingAdapter = OnBoardingAdapter(onBoardingRulesProvider.getOnBoardingRules())

        val onBoardingViewPager = binding.viewPager
        onBoardingViewPager.adapter = onBoardingAdapter

        onBoardingViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER


        binding.nextbtn.setOnClickListener {
            if (onBoardingViewPager.currentItem + 1 < onBoardingAdapter.itemCount) {
                onBoardingViewPager.currentItem += 1
            } else {
                //if at last page of rules
                //onBoardingEnd()
                navigateToCameraActivity()
            }
        }

        //Directly navigate to home activity
        binding.skipBtn.setOnClickListener {
            //onBoardingEnd()
            //navigateToHomeActivity()

            //TODO fix home activity and change it to home activity
            navigateToCameraActivity()
        }

    }

    private fun navigateToCameraActivity() {
        startActivity(Intent(this, CameraActivity::class.java))
        //TODO if using shared preference then call finish
        //finish()
    }


    private fun setupIndicators() {
        indicatorContainer = binding.indicatorLayout
        val indicators = arrayOfNulls<ImageView>(onBoardingAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.onboarding_indicator_inactive
                    )
                )
                it.layoutParams = layoutParams
                indicatorContainer.addView(it)

            }
        }
    }

    //TODO fix the animation of indicators
//    private fun setCurrentIndicator(position: Int) {
//        val childCount = indicatorContainer.childCount
//        for (i in 0 until childCount) {
//            val imageView = indicatorContainer.getChildAt(i) as ImageView
//            if (i == position) {
//                imageView.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        applicationContext,
//                        R.drawable.onboarding_indicator_active
//                    )
//                )
//            } else {
//                imageView.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        applicationContext,
//                        R.drawable.onboarding_indicator_inactive
//                    )
//                )
//            }
//        }
//    }
//private fun setCurrentIndicator(position: Int) {
//    val childCount = indicatorContainer.childCount
//    for (i in 0 until childCount) {
//        val imageView = indicatorContainer.getChildAt(i) as ImageView
//
//        val drawableResId = if (i == position) R.drawable.onboarding_indicator_active
//        else R.drawable.onboarding_indicator_inactive
//
//        imageView.animate()
//            .scaleX(if (i == position) 1.2f else 1.0f) // Adjust scale for smooth animation
//            .scaleY(if (i == position) 1.2f else 1.0f) // Adjust scale for smooth animation
//            .setDuration(300) // Set the duration of the animation in milliseconds
//            .withStartAction {
//                imageView.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        applicationContext,
//                        drawableResId
//                    )
//                )
//            }
//            .start()
//    }
//}

    private fun setCurrentIndicator(position: Int) {
        val childCount = indicatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = indicatorContainer.getChildAt(i) as ImageView

            val drawableResId = if (i == position) R.drawable.onboarding_indicator_active
            else R.drawable.onboarding_indicator_inactive

            val scale = if (i == position) 1.2f else 1.0f
            val alpha = if (i == position) 1.0f else 0.5f

            // Create an ObjectAnimator for scaling
            val scaleAnimatorX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, scale)
            val scaleAnimatorY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, scale)

            // Create an ObjectAnimator for alpha fading
            val alphaAnimator = ObjectAnimator.ofFloat(imageView, View.ALPHA, alpha)

            // Set the duration for the animation
            val duration = 300L // You can adjust the duration as needed

            // Start the animations together
            scaleAnimatorX.duration = duration
            scaleAnimatorY.duration = duration
            alphaAnimator.duration = duration

            scaleAnimatorX.start()
            scaleAnimatorY.start()
            alphaAnimator.start()

            // Set the image resource after starting the animations
            imageView.setImageDrawable(ContextCompat.getDrawable(applicationContext, drawableResId))
        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    private fun onBoardingEnd() {
        val sharedPref = getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }

    private inner class MyPageTransformer : ViewPager2.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            val absPosition = abs(position)
            when {
                position < -1 -> {
                    page.alpha = 0f
                }
                position <= 1 -> {
                    page.alpha = 1 - absPosition
                    val scaleFactor = 0.75f + (1 - absPosition) * 0.25f
                    page.scaleX = scaleFactor
                    page.scaleY = scaleFactor
                }
                else -> {
                    page.alpha = 0f
                }
            }
        }
    }
}