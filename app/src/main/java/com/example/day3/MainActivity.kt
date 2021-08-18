package com.example.day3

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Property
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.day3.databinding.ActivityMainBinding
import kotlin.properties.Delegates
import kotlin.random.Random


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var imgCat: ImageView

    private var myAnimator = ObjectAnimator()
    private var myFirstTranslateX by Delegates.notNull<Float>()
    private var myFirstTranslateY by Delegates.notNull<Float>()
    private var myFirstRotation by Delegates.notNull<Float>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imgCat = binding.imgCat

        handleTasks()
    }

    private fun setMyImgToFirstPosition() {
        myFirstTranslateX = imgCat.translationX
        myFirstTranslateY = imgCat.translationY
        myFirstRotation = imgCat.rotation
    }

    private fun handleTasks() {
        setMyImgToFirstPosition()
        init()
    }

    private fun init() {
        initListener()
    }

    private fun initListener() {
        binding.btnRotate.setOnClickListener(this)
        binding.btnTranslate.setOnClickListener(this)
        binding.btnFade.setOnClickListener(this)
        binding.btnScale.setOnClickListener(this)
        binding.btnShower.setOnClickListener(this)
        binding.btnSkyColor.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRotate -> {
                //c1: Using ViewAnimation part 1 dynamic
                resetMyView(imgCat)
                val rotateAnimation = RotateAnimation(
                    0f,
                    360f,
                    Animation.RELATIVE_TO_SELF,
                   0.5f,
                    Animation.RELATIVE_TO_SELF,
                    0.5f
                ).apply {
                    duration = 1000
                    interpolator = AccelerateInterpolator()
                    imgCat.startAnimation(this)
                }

                //c2: Using ViewAnimation part 2 xml
//                resetMyView(imgCat)
//                AnimationUtils.loadAnimation(this, R.anim.rotate_anim).apply {
//                    imgCat.startAnimation(this)
//                }

                //c3 ObjectAnimator by static
//                AnimatorInflater.loadAnimator(this, R.animator.rotate_animator).apply {
//                    setTarget(imgCat)
//                    start()
//                }
                // c4 ObjectAnimator by dynamic
                resetMyView(imgCat)
                myAnimator = rotateAnimator(imgCat, LinearInterpolator(), 0f, 360f)
            }

            binding.btnTranslate -> {
                //c1: Using ViewAnimation part 1 dynamic
//                val translateAnim = TranslateAnimation(
//                    0f,
//                    0f,
//                    0f,
//                    300f).apply {
//                    duration = 5000
//                    interpolator = LinearInterpolator()
//                    fillAfter = true
//                }
//                imgCat.clearAnimation()
//                imgCat.startAnimation(translateAnim)

                //c2: Using ViewAnimation part 2 xml

//                    AnimationUtils.loadAnimation(
//                        this, R.anim.translate
//                    ).apply {
//                        binding.imgCat.startAnimation(this)
//                    }

                // c3 ObjectAnimator by static

//                val translateAnimator = AnimatorInflater.loadAnimator(this, R.animator.translate)
//                translateAnimator.setTarget(binding.imgCat)
//                translateAnimator.start()

                // c4 ObjectAnimator by dynamic
                resetMyView(imgCat)
                myAnimator =
                    translateAnimator(imgCat, LinearInterpolator(), View.TRANSLATION_Y, -800f, 800f)
            }

            binding.btnScale -> {
                resetMyView(imgCat)
                scaleAnimator(imgCat, AnticipateOvershootInterpolator(), 0.5f, 5f)
            }

            binding.btnFade -> {
                resetMyView(imgCat)
                fadeAnimator(imgCat, AnticipateOvershootInterpolator(), 1f, 0f)
            }

            binding.btnSkyColor -> {
                resetMyView(imgCat)
                changeColorAnimator(imgCat.parent, Color.BLACK, Color.RED)
            }

            binding.btnShower -> {
                createNewCatAnimation()
            }
        }
    }

    private fun createNewCatAnimation() {
        //create size of a cat
        val randomSize = Random.nextInt(200, 300)

        // create position to my cat in layout (with id: container 2) that holds it
        val startTranslateX = 0
        val endTranslateX = binding.container.width - 2 * randomSize
        val startTranslateY = binding.container.height
        val endTranslateY = binding.container.height + binding.container2.height - 3 * randomSize
        val newCat = ImageView(this).apply {
            layoutParams = ViewGroup.LayoutParams(randomSize, randomSize) // set size to cat
            setImageResource(R.drawable.ic_cat) //set image to cat's src
            //set position to cat
            translationX = Random.nextInt(startTranslateX, endTranslateX).toFloat()
            translationY = Random.nextInt(startTranslateY, endTranslateY).toFloat()
        }

        binding.container2.addView(newCat)
        // set animatior
        with(AnimatorSet()) {
            play(
                translateAnimator(
                    newCat,
                    BounceInterpolator(),
                    View.TRANSLATION_X,
                    newCat.translationX,
                    Random.nextInt(startTranslateX, endTranslateX).toFloat()
                )
            )
                .with(
                    translateAnimator(
                        newCat,
                        BounceInterpolator(),
                        View.TRANSLATION_Y,
                        newCat.translationY,
                        Random.nextInt(startTranslateY, endTranslateY).toFloat()
                    )
                )
                .before(scaleAnimator(newCat, LinearInterpolator(), 1f, 0.5f))
                .with(changeColorAnimator(newCat, Color.BLUE, Color.WHITE, Color.RED, Color.MAGENTA))
                .with(rotateAnimator(newCat, AccelerateDecelerateInterpolator(), 0f, 360f))
            start()
        }
        // Make View move in circle path
        RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0f,
            Animation.RELATIVE_TO_SELF,
            0f
        ).apply {
            duration = 3000
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
            interpolator = AccelerateInterpolator()
            newCat.startAnimation(this)
        }
    }

    private fun resetMyView(v: View) {
        v.translationX = myFirstTranslateX
        v.translationY = myFirstTranslateY
        v.rotation = myFirstRotation

        v.clearAnimation()
        myAnimator.cancel()
    }

    private fun translateAnimator(
        view: View,
        myInterpolator: BaseInterpolator,
        propertyView: Property<View, Float>,
        vararg values: Float
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, propertyView, *values).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            interpolator = myInterpolator
            start()
        }
    }

    private fun rotateAnimator(
        view: View,
        myInterpolator: BaseInterpolator,
        vararg values: Float
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ROTATION, *values).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            interpolator = myInterpolator
            start()
        }
    }

    private fun scaleAnimator(
        view: View,
        myInterpolator: BaseInterpolator,
        vararg values: Float
    ): ObjectAnimator {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, *values)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, *values)
        return ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY).apply {
            duration = 1000
            interpolator = myInterpolator
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun fadeAnimator(
        view: View,
        myInterpolator: BaseInterpolator,
        vararg values: Float
    ): ObjectAnimator {
        return ObjectAnimator.ofFloat(view, View.ALPHA, *values).apply {
            duration = 3000
            interpolator = myInterpolator
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun changeColorAnimator(
        view: View,
        vararg values: Int
    ): ObjectAnimator {
        return ObjectAnimator.ofArgb(view, "colorFilter", *values).apply {
            duration = 1000
            setEvaluator(ArgbEvaluator())
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }
}