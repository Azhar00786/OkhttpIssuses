package com.example.okhttpissues.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.okhttpissues.data.api_model.CommentDataItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.CommentDataEntity
import com.example.okhttpissues.databinding.FragmentIssueDetailBinding
import com.example.okhttpissues.ui.adapters.CommentListAdapter
import com.example.okhttpissues.ui.viewmodel.IssueDetailVMFactory
import com.example.okhttpissues.ui.viewmodel.IssueDetailViewModel
import com.example.okhttpissues.utils.NetworkChecker

class IssueDetailFragment : Fragment() {
    private lateinit var viewModel: IssueDetailViewModel
    private lateinit var viewModelFactory: IssueDetailVMFactory
    private var finalList: MutableLiveData<List<CommentDataEntity>> = MutableLiveData()

    private val args: IssueDetailFragmentArgs by navArgs()
    private var _binding: FragmentIssueDetailBinding? = null
    private val binding get() = _binding!!
    private var position: Int? = null

    companion object {
        const val TAG = "IssueDetailFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            IssueDetailVMFactory(DatabaseGetter.getInstance(requireContext()))
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(IssueDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "userNumber from Screen 1: ${args.userNumber.toString()}")
        position = args.userNumber

        _binding = FragmentIssueDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (NetworkChecker.isOnline(requireContext())) {
            viewModel.getCommentTblRowCount().observe(viewLifecycleOwner, Observer {
                Log.d(TAG, "Table Row Count $it")
                if (it == 0) {
                    viewModel.getAllIssueId().observe(viewLifecycleOwner, Observer { idList ->
                        for (id in idList) {
                            viewModel.getSingleUrl(id).observe(viewLifecycleOwner, Observer { url ->
                                val modUrl = url.dropLast(8)
                                viewModel.getCommentApiData(modUrl)
                                viewModel.getCommentList()
                                    .observe(viewLifecycleOwner, Observer { commentList ->
                                        if (commentList != null) {
                                            viewModel.cacheApiData(
                                                commentList.convertToCommentEntity(
                                                    id
                                                )
                                            )
                                        }
                                    })
                            })
                        }
                    })
                } else {
                    viewModel.deleteCommentCachedData()
                    viewModel.getAllIssueId().observe(viewLifecycleOwner, Observer { idList ->
                        for (id in idList) {
                            viewModel.getSingleUrl(id).observe(viewLifecycleOwner, Observer { url ->
                                val modUrl = url.plus("/")
                                viewModel.getCommentApiData(modUrl)
                                viewModel.getCommentList()
                                    .observe(viewLifecycleOwner, Observer { commentList ->
                                        if (commentList != null) {
                                            viewModel.cacheApiData(
                                                commentList.convertToCommentEntity(
                                                    id
                                                )
                                            )
                                        }
                                    })
                            })
                        }
                    })
                }
            })
        }
        invokeListAdapter()
        finalList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                invokeListAdapter()
            } else {
                binding.commentList.visibility = View.VISIBLE
                binding.problemDescription.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                binding.commentList.adapter = CommentListAdapter(requireContext(), it)
            }
        })
    }

    private fun invokeListAdapter() {
        viewModel.getDataUsingIssueId(position!!).observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "commentCachedData: ${it.toString()}")
            if (it != null) {
                finalList.value = it
            }
        })
    }

    private fun ArrayList<CommentDataItem>.convertToCommentEntity(id: Int): List<CommentDataEntity> {
        return map {
            CommentDataEntity(
                issueId = id,
                body = it.body,
                userName = it.user!!.login
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}