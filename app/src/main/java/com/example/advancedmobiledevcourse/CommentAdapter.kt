package com.example.advancedmobiledevcourse

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.advancedmobiledevcourse.databinding.RecyclerviewItemRowBinding
import com.example.advancedmobiledevcourse.dataclass.Comment

class CommentAdapter(private val comments: List<Comment>) : RecyclerView.Adapter<CommentAdapter.CommentHolder>() {

    private var _binding: RecyclerviewItemRowBinding? = null
    private val binding get() = _binding!!

    override fun getItemCount() = comments.size

    override fun onBindViewHolder(holder: CommentAdapter.CommentHolder, position: Int) {
        val itemComment = comments[position]
        holder.bindComment(itemComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentHolder {
        _binding = RecyclerviewItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    class CommentHolder(v: RecyclerviewItemRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        private var view: RecyclerviewItemRowBinding = v
        private var comment: Comment? = null


        init {
            v.root.setOnClickListener(this)
        }

        fun bindComment(comment: Comment)
        {
            this.comment = comment

            var commentName = comment.name as String

            if(commentName.length > 20)
            {
                commentName = commentName.substring(0, 20) + "..."
            }

            view.textViewCommentName.text = comment.name

            view.textViewCommentEmail.text = comment.email
            view.textViewCommentBody.text = comment.body

        }

        override fun onClick(v: View) {
            Log.d("RecyclerView", "CLICK!")
            val action = DataFragmentDirections.actionDataFragmentToDetailFragment(comment?.id as Int)
            v.findNavController().navigate(action)
        }
    }
}