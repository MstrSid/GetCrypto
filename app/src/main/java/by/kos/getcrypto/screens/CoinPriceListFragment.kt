package by.kos.getcrypto.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kos.getcrypto.databinding.FragmentCoinPriceListBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class CoinPriceListFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

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

        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        /*viewModel.priceList.observe(viewLifecycleOwner, Observer {
            Log.d("TEST_LOAD_DATA", "Success: $it")
        })*/
        viewModel.getDetailInfo("BTC").observe(viewLifecycleOwner, Observer {
            Log.d("TEST_LOAD_DATA", "Success: $it")
        })

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_coinPriceListFragment_to_coinDetailFragment)
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}