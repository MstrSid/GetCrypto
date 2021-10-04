package by.kos.getcrypto.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kos.getcrypto.R
import by.kos.getcrypto.databinding.FragmentCoinDetailBinding
import com.squareup.picasso.Picasso

class CoinDetailFragment : Fragment() {

    private var _binding: FragmentCoinDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CoinViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        Log.d("LOG_APP",   viewModel.sendFSymbol.toString())
            viewModel.getDetailInfo().observe(viewLifecycleOwner, Observer {
                Log.d("LOG_APP",  it)
                with(binding) {
                    tvFSym.text = it.fromSymbol
                    tvTSym.text = it.toSymbol
                    Picasso.get().load(it.getFullImageUrl()).into(ivLogoCoin)
                    tvPrice.text = it.price.toString()
                    tvDayMin.text = it.lowDay.toString()
                    tvDayMax.text = it.highDay.toString()
                    tvLastDeal.text = it.lastMarket.toString()
                    tvUpdated.text = it.getFormattedTime()
                }
            })


        /*binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_coinDetailFragment_to_coinPriceListFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}