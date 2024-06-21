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
class RecyclerViewVerticaEdgeEffect : BaseEdgeEffectFactory<RecyclerViewVerticaEdgeEffect.SpringVerticaAnimationViewHolder>() {
    override fun applyPullEffect(recyclerView: RecyclerView, direct: Int, delta: Float) {
        val transY: Float = recyclerView.height * delta * ratio * direct
        for (i in 0 until recyclerView.childCount) {
            val vh =
                recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as SpringVerticaAnimationViewHolder
            if (vh.itemView.isShown) {
                vh.springAnimation.cancel()
                var y = vh.itemView.translationY
                y += transY
                vh.itemView.translationY = y
            }
        }
    }

    override fun currentDirection(): Int {
        return DIRECTION_TOP
    }

    class SpringVerticaAnimationViewHolder(itemView: View) :
        BaseSpringAnimationViewHolder(itemView) {
        override fun createSpringAnimation(): SpringAnimation {
            return SpringAnimation(itemView, SpringAnimation.TRANSLATION_Y)
                .setSpring(
                    SpringForce().setFinalPosition(0f).setDampingRatio(0.8f)
                        .setStiffness(SpringForce.STIFFNESS_LOW)
                )
        }
    }
}
