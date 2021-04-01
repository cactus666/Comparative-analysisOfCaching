package com.crafttalk.chat.presentation.holders

import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.crafttalk.chat.R
import com.crafttalk.chat.presentation.base.BaseViewHolder
import com.crafttalk.chat.presentation.model.ActionItem
import com.crafttalk.chat.utils.ChatAttr

class HolderAction(
    view: View,
    private val clickHandler: (actionId: String) -> Unit
) : BaseViewHolder<ActionItem>(view), View.OnClickListener {
    private val itemAction: ViewGroup? = view.findViewById(R.id.item_action)
    private val actionText: TextView? = view.findViewById(R.id.action_text)

    private var actionId: String? = null

    init {
        view.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        actionId?.let(clickHandler)
    }

    override fun bindTo(item: ActionItem) {
        actionId = item.id
        // set width item
        itemAction?.apply {
            ChatAttr.getInstance().widthItemOperatorTextMessage?.let {
                layoutParams.width = it
            }
        }
        actionText?.apply {
            // set content
            text = item.actionText
            // set color
            setTextColor(ChatAttr.getInstance().colorTextOperatorAction)
            // set dimension
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ChatAttr.getInstance().sizeTextOperatorAction)
            // set font
            ChatAttr.getInstance().resFontFamilyOperatorAction?.let {
                typeface = ResourcesCompat.getFont(context, it)
            }
        }
        // set bg
        itemView.setBackgroundResource(item.backgroundRes)
    }
}