package me.kpa.edgeeffect.recycler

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 *
 * @author kpa
 * @date 2024/6/18
 */
class RecyclerViewHorizontalEdgeEffect :
    BaseEdgeEffectFactory<RecyclerViewHorizontalEdgeEffect.SpringHorizontalAnimationViewHolder>() {
    override fun applyPullEffect(recyclerView: RecyclerView, direct: Int, delta: Float) {
        val transX: Float = recyclerView.width * delta * ratio * direct
        for (i in 0 until recyclerView.childCount) {
            val vh =
                recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as SpringHorizontalAnimationViewHolder
            if (vh.itemView.isShown) {
                vh.springAnimation.cancel()
                vh.itemView.translationX += transX
            }
        }
    }

    override fun currentDirection(): Int {
        return DIRECTION_LEFT
    }

    class SpringHorizontalAnimationViewHolder(itemView: View) :
        BaseSpringAnimationViewHolder(itemView) {
        override fun createSpringAnimation(): SpringAnimation {
            return SpringAnimation(itemView, SpringAnimation.TRANSLATION_X)
                .setSpring(
                    SpringForce().setFinalPosition(0f).setDampingRatio(0.8f)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                )
        }
    }
}
