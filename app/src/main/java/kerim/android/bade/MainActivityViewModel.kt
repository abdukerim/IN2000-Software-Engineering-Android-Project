package kerim.android.bade

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kerim.android.bade.api.DataSource
import kerim.android.bade.data.Tsery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val dataSource = DataSource()
    private val tseryData = MutableLiveData<List<Tsery>>()
    private val staticList = MutableLiveData<List<Tsery>>()

    fun getBadeDataObserver() : MutableLiveData<List<Tsery>> {
        return tseryData
    }

    fun loadTseryData() {
        viewModelScope.launch(Dispatchers.IO) {
            dataSource.fetchBadeInfo().also {
                if (it != null) {
                    tseryData.postValue(it)
                }
            }
            dataSource.fetchWeather().also {
                if (it.isNotEmpty()) {
                    tseryData.postValue(it)
                }
            }
            dataSource.getDistance().also {
                tseryData.postValue(it)
            }

            //clone of the data that will be constant
            staticList.postValue(tseryData.value)
        }

    }
    // updates the list of tsery data objects
    // also refreshes the list graphically

    fun updateTseryData(data: List<Tsery>) {
        tseryData.postValue(data)
    }

    fun getStaticList(): MutableLiveData<List<Tsery>> {
        return staticList
    }
}

