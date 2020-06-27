package cash.z.ecc.android.ui.zircle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cash.z.ecc.android.databinding.FragmentZircleDetailBinding
import cash.z.ecc.android.ext.onClickNavBack
import cash.z.ecc.android.ui.base.BaseFragment
import cash.z.ecc.android.ui.detail.TransactionAdapter
import cash.z.ecc.android.ui.detail.TransactionsFooter


class ZircleDetailFragment : BaseFragment<FragmentZircleDetailBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentZircleDetailBinding =
        FragmentZircleDetailBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButtonHitArea.onClickNavBack()

        initRecycler()
    }

    private fun initRecycler() {
        binding.recyclerZirclers.apply {
            layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = TransactionAdapter()
            smoothScrollToPosition(0)
        }
    }

}