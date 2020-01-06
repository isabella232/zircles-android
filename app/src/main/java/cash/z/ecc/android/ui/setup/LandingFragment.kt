package cash.z.ecc.android.ui.setup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import cash.z.ecc.android.R
import cash.z.ecc.android.ZcashWalletApp
import cash.z.ecc.android.databinding.FragmentLandingBinding
import cash.z.ecc.android.di.viewmodel.viewModel
import cash.z.ecc.android.ui.base.BaseFragment
import cash.z.ecc.android.ui.setup.WalletSetupViewModel.WalletSetupState.SEED_WITHOUT_BACKUP
import cash.z.ecc.android.ui.setup.WalletSetupViewModel.WalletSetupState.SEED_WITH_BACKUP
import cash.z.wallet.sdk.Initializer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LandingFragment : BaseFragment<FragmentLandingBinding>() {

    val walletSetup: WalletSetupViewModel by viewModel()

    private var skipCount: Int = 0

    override fun inflate(inflater: LayoutInflater): FragmentLandingBinding =
        FragmentLandingBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonPositive.setOnClickListener {
            when (binding.buttonPositive.text.toString().toLowerCase()) {
                "new" -> onNewWallet()
                "backup" -> onBackupWallet()
            }
        }
        binding.buttonNegative.setOnLongClickListener {
            if (binding.buttonNegative.text.toString().toLowerCase() == "restore") {
                MaterialAlertDialogBuilder(activity)
                    .setMessage("Would you like to import the dev wallet?\n\nIf so, please only send 0.0001 ZEC at a time and return some later so that the account remains funded.")
                    .setTitle("Import Dev Wallet?")
                    .setCancelable(true)
                    .setPositiveButton("Import") { dialog, _ ->
                        dialog.dismiss()
                        onUseDevWallet()
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
                true
            }
            false
        }
        binding.buttonNegative.setOnClickListener {
            when (binding.buttonNegative.text.toString().toLowerCase()) {
                "restore" -> onRestoreWallet()
                else -> onSkip(++skipCount)
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)

        walletSetup.checkSeed().onEach {
            when(it) {
                SEED_WITHOUT_BACKUP, SEED_WITH_BACKUP -> {
                    mainActivity?.navController?.navigate(R.id.nav_backup)
                }
            }
        }.launchIn(lifecycleScope)
    }

    private fun onSkip(count: Int) {
        when (count) {
            1 -> {
                binding.textMessage.text =
                    "Are you sure? Without a backup, funds can be lost FOREVER!"
                binding.buttonNegative.text = "Later"
            }
            2 -> {
                binding.textMessage.text =
                    "You can't backup later. You're probably going to lose your funds!"
                binding.buttonNegative.text = "I've been warned"
            }
            else -> {
                onEnterWallet()
            }
        }
    }

    private fun onRestoreWallet() {
        Toast.makeText(activity, "Coming soon!", Toast.LENGTH_SHORT).show()
    }

    private fun onUseDevWallet() {
        val seedPhrase =
            "still champion voice habit trend flight survey between bitter process artefact blind carbon truly provide dizzy crush flush breeze blouse charge solid fish spread"
        val birthday = 663174//626599
        mainActivity?.apply {
            lifecycleScope.launch {
                initializeAccount(
                    walletSetup.importWallet(feedback, seedPhrase.toCharArray()),
                    Initializer.loadBirthdayFromAssets(ZcashWalletApp.instance, birthday)
                )
                initSync()
            }

            binding.buttonPositive.isEnabled = true
            binding.textMessage.text = "Wallet imported! Congratulations!"
            binding.buttonNegative.text = "Skip"
            binding.buttonPositive.text = "Backup"
            playSound("sound_receive_small.mp3")
            vibrateSuccess()
        }
    }

    // TODO: move this to the ViewModel but doing so requires fixing dagger so do that as a separate PR
    private fun onNewWallet() {
        lifecycleScope.launch {
            val ogText = binding.buttonPositive.text
            binding.buttonPositive.text = "creating"
            binding.buttonPositive.isEnabled = false

            mainActivity?.apply {
                initializeAccount(walletSetup.createWallet(feedback))
                initSync()
            }

            binding.buttonPositive.isEnabled = true
            binding.textMessage.text = "Wallet created! Congratulations!"
            binding.buttonNegative.text = "Skip"
            binding.buttonPositive.text = "Backup"
            mainActivity?.playSound("sound_receive_small.mp3")
            mainActivity?.vibrateSuccess()
        }
    }

    private fun onBackupWallet() {
        skipCount = 0
        mainActivity?.navController?.navigate(R.id.action_nav_landing_to_nav_backup)
    }

    private fun onEnterWallet() {
        skipCount = 0
        mainActivity?.navController?.popBackStack()
    }
}