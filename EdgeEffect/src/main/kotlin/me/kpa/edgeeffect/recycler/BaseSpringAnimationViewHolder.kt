package me.kpa.edgeeffect.recycler

import android.view.View
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.recyclerview.widget.RecyclerView

/**
 * Description:
 * Created by kpa on 2024/6/18.
 */
abstract class BaseSpringAnimationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    @JvmField
    var springAnimation: SpringAnimation

    init {
        springAnimation = createSpringAnimation()
    }

    protected abstract fun createSpringAnimation(): SpringAnimation
}
