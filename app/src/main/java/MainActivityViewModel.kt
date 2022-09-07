import androidx.lifecycle.ViewModel
import androidx.lifecycle.Observer
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData


class MainActivityViewModel : ViewModel() {
    val mediaImage = MutableLiveData<String>()

}