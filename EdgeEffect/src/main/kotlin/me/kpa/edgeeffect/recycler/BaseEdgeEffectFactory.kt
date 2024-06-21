package me.kpa.edgeeffect.recycler

import android.graphics.Canvas
import android.widget.EdgeEffect
import androidx.recyclerview.widget.RecyclerView

/**
 * @author kpa
 */
abstract class BaseEdgeEffectFactory<T : BaseSpringAnimationViewHolder> :
    RecyclerView.EdgeEffectFactory() {
    @JvmField
    protected val ratio: Float = 0.3f


    override fun createEdgeEffect(recyclerView: RecyclerView, direction: Int): EdgeEffect {
        return object : EdgeEffect(recyclerView.context) {
            private fun handlePull(delta: Float) {
                val direct = if (direction == currentDirection()) 1 else -1
                applyPullEffect(recyclerView, direct, delta)
            }

            override fun onPull(deltaDistance: Float, displacement: Float) {
                super.onPull(deltaDistance, displacement)
                handlePull(deltaDistance)
            }

            override fun onPull(deltaDistance: Float) {
                super.onPull(deltaDistance)
                handlePull(deltaDistance)
            }

            override fun onRelease() {
                super.onRelease()
                finish()
                if (isFinished) {
                    for (i in 0 until recyclerView.childCount) {
                        val vh = recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as T
                        vh!!.springAnimation.start()
                    }
                }
            }

            override fun onAbsorb(velocity: Int) {
                super.onAbsorb(velocity)
                val direct = if (direction == currentDirection()) 1 else -1
                val velocityX = velocity * ratio * direct
                for (i in 0 until recyclerView.childCount) {
                    val vh = recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as T
                    vh!!.springAnimation
                        .setStartVelocity(velocityX)
                        .start()
                }
            }

            override fun draw(canvas: Canvas): Boolean {
                setSize(0, 0)
                return super.draw(canvas)
            }
        }
    }

    protected abstract fun applyPullEffect(recyclerView: RecyclerView, direct: Int, delta: Float)

    protected abstract fun currentDirection(): Int
}