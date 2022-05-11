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
import androidx.navigation.fragment.findNavController
import com.example.okhttpissues.data.api.ApiConnectorSingelton
import com.example.okhttpissues.data.api_model.IssuesItem
import com.example.okhttpissues.data.database.DatabaseGetter
import com.example.okhttpissues.data.database_entities.IssuesEntity
import com.example.okhttpissues.databinding.FragmentOkhttpIssuesBinding
import com.example.okhttpissues.ui.adapters.IssuesListAdapter
import com.example.okhttpissues.ui.viewmodel.OkhttpIssuesVMFactory
import com.example.okhttpissues.ui.viewmodel.OkhttpIssuesViewModel
import com.example.okhttpissues.utils.NetworkChecker

class OkhttpIssuesFragment : Fragment() {
    private lateinit var viewModel: OkhttpIssuesViewModel
    private lateinit var viewModelFactory: OkhttpIssuesVMFactory

    private var finalDataList: MutableLiveData<List<IssuesEntity>> = MutableLiveData()

    private var _binding: FragmentOkhttpIssuesBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "OkhttpIssuesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory =
            OkhttpIssuesVMFactory(
                ApiConnectorSingelton,
                DatabaseGetter.getInstance(requireContext())
            )
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(OkhttpIssuesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOkhttpIssuesBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (NetworkChecker.isOnline(requireContext())) {
            viewModel.getTableRowCount().observe(viewLifecycleOwner, Observer {
                if (it == 0) {
                    viewModel.getApiData().observe(viewLifecycleOwner, Observer { arrayList ->
                        if (arrayList != null) {
                            viewModel.cacheApiData(arrayList.convertToIssueEntity())
                        }
                    })
                } else {
                    viewModel.deleteCachedData()
                    viewModel.getApiData().observe(viewLifecycleOwner, Observer { arrayList ->
                        if (arrayList != null) {
                            viewModel.cacheApiData(arrayList.convertToIssueEntity())
                        }
                    })
                }
            })
        }

        invokeListAdapter()
        finalDataList.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                invokeListAdapter()
            } else {
                binding.IssueList.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE

                binding.IssueList.adapter = IssuesListAdapter(requireContext(), it)
                binding.IssueList.isClickable = true
                binding.IssueList.setOnItemClickListener { parent, view, position, id ->
                    Log.d(TAG, "Clicked Item position: ${position.toString()}")
                    val action =
                        OkhttpIssuesFragmentDirections.actionOkhttpIssuesFragmentToIssueDetailFragment(
                            position
                        )
                    findNavController().navigate(action)
                }
            }
        })
    }

    private fun invokeListAdapter() {
        viewModel.getCachedData().observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "cachedData: ${it.toString()}")
            if (it != null) {
                finalDataList.value = it
            }
        })
    }

    private fun ArrayList<IssuesItem>.convertToIssueEntity(): List<IssuesEntity> {
        return map {
            val splitOne = it.updated_at.split("T")
            val splitTwo = splitOne[0].split("-")
            val finalData = "${splitTwo[1]}-${splitTwo[2]}-${splitTwo[0]}"

            IssuesEntity(
                title = it.title,
                issueDescription = it.body,
                username = it.user.login,
                userAvatar = it.user.avatar_url,
                updatedOn = finalData,
                commentsUrl = it.comments_url,
                commentCount = it.comments,
                userNumber = it.number
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}