package me.kpa.edgeeffect

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import android.widget.EdgeEffect
import androidx.annotation.IntDef
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.EdgeEffectCompat
import androidx.core.widget.NestedScrollView
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlin.math.max

/**
 * Description:
 * Created by kpa on 2024/6/19.
 */
class EdgeEffectScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : NestedScrollView(context, attrs, 0) {
    private var mEdgeEffectFactory: EdgeEffectFactory =
        EdgeEffectFactory()

    private var mLastTouchY = 0.0f
    private var mTopGlow: EdgeEffect? = null
    private var mBottomGlow: EdgeEffect? = null

    init {
        overScrollMode = View.OVER_SCROLL_NEVER
        isFillViewport = true
        setEdgeEffectFactory(buildDefaultEdgeEffect())
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_MOVE -> {
                val deltaY = (motionEvent.y - mLastTouchY).toInt()
                scrollByInternal(0, -deltaY, motionEvent)
                mLastTouchY = motionEvent.y
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                resetScroll();
            }

            MotionEvent.ACTION_DOWN -> {
                mLastTouchY = motionEvent.y
            }
        }
        return super.onTouchEvent(motionEvent)
    }

    private fun resetScroll() {
        stopNestedScroll(ViewCompat.TYPE_TOUCH)
        releaseGlows()
    }

    private fun releaseGlows() {
        var needsInvalidate = false
        if (mTopGlow != null) {
            mTopGlow!!.onRelease()
            needsInvalidate = needsInvalidate or mTopGlow!!.isFinished
        }
        if (mBottomGlow != null) {
            mBottomGlow!!.onRelease()
            needsInvalidate = needsInvalidate or mBottomGlow!!.isFinished
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


    fun innerComputeVerticalScrollRange(): Int {
        val count = childCount
        val parentSpace = height - paddingBottom - paddingTop
        if (count == 0) {
            return parentSpace
        }

        val child = getChildAt(0)
        val lp = child.layoutParams as LayoutParams
        var scrollRange = child.bottom + lp.bottomMargin
        val scrollY = scrollY
        val overscrollBottom =
            max(0.0, (scrollRange - parentSpace).toDouble()).toInt()
        if (scrollY < 0) {
            scrollRange -= scrollY
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom
        }

        return scrollRange
    }

    private fun scrollByInternal(x: Int, y: Int, motionEvent: MotionEvent) {
        var unconsumedY = 0
        var consumedY = 0
        if (y != 0) {
            val scrollRange = innerComputeVerticalScrollRange() - height
            val currentScrollY = scrollY
            val newScrollY = currentScrollY + y


            // 限制滚动范围
            if (newScrollY < 0) {
                consumedY = -currentScrollY
                unconsumedY = y - consumedY
            } else if (newScrollY > scrollRange) {
                consumedY = scrollRange - currentScrollY
                unconsumedY = y - consumedY
            } else {
                consumedY = y
            }
        }
        if (motionEvent != null && !MotionEventCompat.isFromSource(
                motionEvent,
                InputDevice.SOURCE_MOUSE
            )
        ) {
            pullGlows(motionEvent.getX(), motionEvent.getY(), unconsumedY.toFloat())
        }
        considerReleasingGlowsOnScroll(x, y)
    }

    private fun considerReleasingGlowsOnScroll(dx: Int, dy: Int) {
        var needsInvalidate = false
        if (mTopGlow != null && !mTopGlow!!.isFinished() && dy > 0) {
            mTopGlow!!.onRelease()
            needsInvalidate = needsInvalidate or mTopGlow!!.isFinished()
        }
        if (mBottomGlow != null && !mBottomGlow!!.isFinished() && dy < 0) {
            mBottomGlow!!.onRelease()
            needsInvalidate = needsInvalidate or mBottomGlow!!.isFinished()
        }
        if (needsInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


    fun ensureTopGlow() {
        if (mTopGlow != null) {
            return
        }
        mTopGlow = mEdgeEffectFactory.createEdgeEffect(this, EdgeEffectFactory.DIRECTION_TOP)
        mTopGlow?.setSize(measuredWidth, measuredHeight)
    }

    fun ensureBottomGlow() {
        if (mBottomGlow != null) {
            return
        }
        mBottomGlow = mEdgeEffectFactory.createEdgeEffect(this, EdgeEffectFactory.DIRECTION_BOTTOM)
        mBottomGlow?.setSize(measuredWidth, measuredHeight)
    }

    private fun pullGlows(x: Float, y: Float, overscrollY: Float) {
        var invalidate = false
        if (overscrollY < 0) {
            ensureTopGlow()
            EdgeEffectCompat.onPull(mTopGlow!!, -overscrollY / height, x / width)
            invalidate = true
        } else if (overscrollY > 0) {
            ensureBottomGlow()
            EdgeEffectCompat.onPull(mBottomGlow!!, overscrollY / height, 1f - x / width)
            invalidate = true
        }
        if (invalidate) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        absorbGlows(velocityY)
    }

    fun absorbGlows(velocityY: Int) {
        if (velocityY < 0) {
            ensureTopGlow()
            if (mTopGlow!!.isFinished) {
                mTopGlow!!.onAbsorb(-velocityY)
            }
        } else if (velocityY > 0) {
            ensureBottomGlow()
            if (mBottomGlow!!.isFinished) {
                mBottomGlow!!.onAbsorb(velocityY)
            }
        }

        if (velocityY != 0) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }


    fun setEdgeEffectFactory(edgeEffectFactory: EdgeEffectFactory) {
        mEdgeEffectFactory = edgeEffectFactory
        invalidateGlows()
    }

    private fun invalidateGlows() {
        mBottomGlow = null
        mTopGlow = null
    }

    open class EdgeEffectFactory {
        companion object {
            @Retention(AnnotationRetention.SOURCE)
            @IntDef(DIRECTION_TOP, DIRECTION_BOTTOM)
            annotation class EdgeDirection

            /**
             * Direction constant for the top edge
             */
            const val DIRECTION_TOP: Int = 1

            /**
             * Direction constant for the bottom edge
             */
            const val DIRECTION_BOTTOM: Int = 3

        }

        /**
         * Create a new EdgeEffect for the provided direction.
         */
        open fun createEdgeEffect(
            view: NestedScrollView,
            @EdgeDirection direction: Int
        ): EdgeEffect {
            return EdgeEffect(view.context)
        }
    }

    private fun buildDefaultEdgeEffect(): EdgeEffectFactory {
        return object : EdgeEffectFactory() {
            private val ratio = 0.3f
            override fun createEdgeEffect(view: NestedScrollView, direction: Int): EdgeEffect {
                return object : EdgeEffect(view.context) {
                    private val springAnimation: SpringAnimation by lazy {
                        SpringAnimation(view, SpringAnimation.TRANSLATION_Y)
                            .setSpring(
                                SpringForce().setFinalPosition(0f).setDampingRatio(0.8f)
                                    .setStiffness(SpringForce.STIFFNESS_LOW)
                            )
                    }

                    override fun onPull(deltaDistance: Float) {
                        super.onPull(deltaDistance)
                        handlePull(deltaDistance)
                    }

                    override fun onPull(deltaDistance: Float, displacement: Float) {
                        super.onPull(deltaDistance, displacement)
                        handlePull(deltaDistance)
                    }

                    private fun handlePull(delta: Float) {
                        val direct =
                            if (direction == DIRECTION_TOP) 1 else -1
                        val transY = view.height * delta * ratio * direct
                        springAnimation.cancel()
                        var y = view.translationY
                        y += transY
                        view.translationY = y
                    }

                    override fun onRelease() {
                        super.onRelease()
                        finish()
                        if (isFinished) {
                            springAnimation.start()
                        }
                    }

                    override fun onAbsorb(velocity: Int) {
                        super.onAbsorb(velocity)
                        val direct =
                            if (direction == DIRECTION_TOP) 1 else -1
                        val velocityX = velocity * ratio * direct
                        springAnimation
                            .setStartVelocity(velocityX)
                            .start()
                    }

                    override fun draw(canvas: Canvas): Boolean {
                        setSize(0, 0)
                        return super.draw(canvas)
                    }
                }
            }
        }
    }
}