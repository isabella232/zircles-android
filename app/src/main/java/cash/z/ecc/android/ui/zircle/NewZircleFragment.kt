package cash.z.ecc.android.ui.zircle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import cash.z.ecc.android.R
import cash.z.ecc.android.databinding.FragmentZircleNewBinding
import cash.z.ecc.android.ext.onClickNavBack
import cash.z.ecc.android.ui.base.BaseFragment


class NewZircleFragment : BaseFragment<FragmentZircleNewBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentZircleNewBinding =
        FragmentZircleNewBinding.inflate(inflater)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.hitAreaScan.onClickNavBack()
        binding.buttonBottom.setOnClickListener {
            mainActivity?.safeNavigate(R.id.action_nav_new_to_nav_zircle)
        }
    }

}