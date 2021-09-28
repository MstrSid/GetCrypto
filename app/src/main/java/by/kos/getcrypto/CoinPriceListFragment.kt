package by.kos.getcrypto

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import by.kos.getcrypto.api.ApiFactory
import by.kos.getcrypto.api.ApiService
import by.kos.getcrypto.databinding.FragmentCoinPriceListBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinPriceListFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()

    private var _binding: FragmentCoinPriceListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCoinPriceListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val disposable = ApiFactory.apiService.getFullPriceList(fSyms = "BTC,ETH,EOS")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                    Log.d("TEST_LOAD_DATA", it.toString())
            }, {
                Log.d("TEST_LOAD_DATA", it.message.toString())
            })
        compositeDisposable.add(disposable)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_coinPriceListFragment_to_coinDetailFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.dispose()
        _binding = null
    }
}