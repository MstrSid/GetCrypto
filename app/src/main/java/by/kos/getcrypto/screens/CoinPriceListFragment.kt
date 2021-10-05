package by.kos.getcrypto.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kos.getcrypto.R
import by.kos.getcrypto.adapters.CoinInfoAdapter
import by.kos.getcrypto.databinding.FragmentCoinPriceListBinding
import by.kos.getcrypto.pojo.CoinPriceInfo
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CoinPriceListFragment : Fragment() {

    private var _binding: FragmentCoinPriceListBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoinViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoinPriceListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CoinInfoAdapter(this.requireContext())
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.priceList.observe(viewLifecycleOwner, Observer {
            adapter.coinInfoList = it
        })
        binding.rvCoinsPriceList.adapter = adapter
        adapter.onCoinClickListener = object : CoinInfoAdapter.OnCoinClickListener {
            override fun onCoinClick(coinPriceInfo: CoinPriceInfo) {
                parentFragmentManager.setFragmentResult("result", bundleOf("fSym" to coinPriceInfo.fromSymbol))
                findNavController().navigate(R.id.action_coinPriceListFragment_to_coinDetailFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}