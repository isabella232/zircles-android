package cash.z.ecc.android.ui.zircle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import cash.z.ecc.android.databinding.FragmentZircleInviteBinding
import cash.z.ecc.android.ext.onClickNavBack
import cash.z.ecc.android.ui.base.BaseFragment


class InviteFragment : BaseFragment<FragmentZircleInviteBinding>() {

    override fun inflate(inflater: LayoutInflater): FragmentZircleInviteBinding =
        FragmentZircleInviteBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButtonHitArea.onClickNavBack()
    }
}