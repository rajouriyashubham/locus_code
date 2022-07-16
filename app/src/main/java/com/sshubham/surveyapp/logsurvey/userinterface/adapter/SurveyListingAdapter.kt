package com.sshubham.surveyapp.logsurvey.userinterface.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.sshubham.surveyapp.R
import com.sshubham.surveyapp.databinding.CommentItemCellBinding
import com.sshubham.surveyapp.databinding.OptionItemCellBinding
import com.sshubham.surveyapp.databinding.PhotoItemCellBinding
import com.sshubham.surveyapp.logsurvey.model.ItemType
import com.sshubham.surveyapp.logsurvey.model.SurveyData
import java.util.*


/**
 * Created by Shubham Rajouriya on 7/11/2022.
 * all dev modification @Copyrigthts reserved to code owner/Shubham Rajouriya
 */
class SurveyListingAdapter(private val context: Context, val resposneLisnter: SurveyDataListner): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val PHOTO_TYPE = 1
    private val CHOICE_TYPE = 2
    private val COMMENT_TYPE = 3
    private val TAG = "SurveyListingAdapter"

    var surveyDataList = LinkedList<SurveyData>()

    fun updateData(dataList: List<SurveyData>){
        surveyDataList.clear()
        dataList.forEach {
            surveyDataList.add(it)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val viewHolder = when (viewType) {
            PHOTO_TYPE -> {
                val binding = PhotoItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                PhotoItemViewHolder(binding)
            }

            COMMENT_TYPE -> {
                val binding = CommentItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CommentItemViewHolder(binding)
            }

            else -> {
                val binding = CommentItemCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CommentItemViewHolder(binding)
            }
        }

        return viewHolder

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val surveyData = surveyDataList.get(position)

        Log.d(TAG, surveyData.itemType.toString())
        when{
            holder is PhotoItemViewHolder -> {

                Log.d(TAG, "PhotoItemViewHolder for $position")
                val photoViewHolder = holder as PhotoItemViewHolder
                photoViewHolder.title.text = surveyData.title

               if(surveyData.imageBitmap != null){
                   holder.photoImage.setImageBitmap(surveyData.imageBitmap)
               }else{
                   holder.photoImage.setImageDrawable(context.getDrawable(R.drawable.palin_bg))
               }
               photoViewHolder.photoImage.setOnClickListener {
                   photoViewHolder.photoImage.tag = position
                   resposneLisnter.onPhotoTouchEvent(false, holder.photoImage, surveyData.id?: "", position)
                   surveyData.selectedResponse = "Image captured for ${surveyData.title}"
               }
               photoViewHolder.clearImage.setOnClickListener {
                   resposneLisnter.onPhotoTouchEvent(true, holder.photoImage, surveyData.id?: "", position)
                   surveyData.selectedResponse = ""
               }
            }

            holder is CommentItemViewHolder -> {
                Log.d(TAG, "CommentItemViewHolder for $position")
                val commentViewHolder = holder as CommentItemViewHolder
                commentViewHolder.title.text = surveyData.title
                commentViewHolder.toggleBtn.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        holder.commentBox.visibility = View.VISIBLE
                    } else {
                        surveyDataList.get(position).selectedResponse = ""
                        holder.commentBox.visibility = View.GONE
                    }
                }
                holder.commentBox.addTextChangedListener(object: TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun afterTextChanged(p0: Editable?) {
                        surveyDataList.get(position).selectedResponse = p0.toString()
                        resposneLisnter.onCommentCapture(p0.toString(), surveyData.id?: "", position)
                    }

                })
            }

            holder is OptionItemViewHolder -> {
                val optionItemViewHolder = holder as OptionItemViewHolder
                val listData = surveyData.dataMap?.options
                if (!listData.isNullOrEmpty()) {
                    val radioButtons = arrayOfNulls<RadioButton>(listData.size)
                    val radioGroup = optionItemViewHolder.optionsRadioGoup
                    if (radioGroup.childCount == 0) {
                        for (i in listData.indices) {
                            val radioBtn = RadioButton(context)
                            radioBtn.text = listData.get(i)
                            radioBtn.id = i + 50
                            radioButtons[i] = radioBtn
                            radioBtn.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
                                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                                    if(p1){
                                        surveyDataList.get(position).selectedResponse = listData.get(i)
                                        resposneLisnter.onOptionSelection(listData.get(i), surveyData.id?: "", position)
                                    }
                                }

                            })
                            radioGroup.addView(radioBtn)
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = surveyDataList.get(position)
       return when(data.itemType){
            ItemType.PHOTO -> PHOTO_TYPE
            ItemType.COMMENT -> COMMENT_TYPE
            ItemType.OPTION -> CHOICE_TYPE
           else -> -1
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return surveyDataList.size
    }

    class PhotoItemViewHolder(view: PhotoItemCellBinding): RecyclerView.ViewHolder(view.root){
        val title = view.photoItemTitleId
        val photoImage = view.photoItemIvId
        val clearImage = view.closeImageIcon
    }

    class CommentItemViewHolder(view: CommentItemCellBinding): RecyclerView.ViewHolder(view.root){
        val title = view.commentItemTitleId
        val toggleBtn = view.commentToggleButton
        val commentBox = view.commentEditText
    }

    class OptionItemViewHolder(view: OptionItemCellBinding): RecyclerView.ViewHolder(view.root){
        val title = view.optionItemTitleId
        val optionsRadioGoup = view.optionRadioGroup
    }


    interface SurveyDataListner{
        fun onCommentCapture(content: String, commentId: String, position: Int)
        fun onOptionSelection(optionContent: String, optionId: String, position: Int)
        fun onPhotoTouchEvent(isForDeletion: Boolean, selectedImageView: ImageView, itemId: String, itemPosition: Int)
    }

}