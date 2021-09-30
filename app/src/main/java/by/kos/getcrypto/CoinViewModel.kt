package by.kos.getcrypto

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import by.kos.getcrypto.api.ApiFactory
import by.kos.getcrypto.database.AppDatabase
import by.kos.getcrypto.pojo.CoinPriceInfo
import by.kos.getcrypto.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getInstance(application)
    val priceList = db.coinPriceInfoDao().getPriceList()
    private val compositeDisposable = CompositeDisposable()

    fun loadData() {
        val disposable = ApiFactory.apiService.getTopCoinsInfo()
            .map {
                it.data?.map { it.coinInfo?.name }?.joinToString(",").toString()
            }.flatMap {
                ApiFactory.apiService.getFullPriceList(fSyms = it)
            }.map {
                getPriceListFromRawData(it)
            }
            .subscribeOn(Schedulers.io())
            .subscribe({
                db.coinPriceInfoDao().insertPriceList(it)
            }, {
                Log.d("TEST_LOAD_DATA", it.message.toString())
            })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(coinPriceInfoRawData: CoinPriceInfoRawData):
            List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}