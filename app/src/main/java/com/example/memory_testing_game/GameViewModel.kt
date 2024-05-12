import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    // LiveData to hold the score
    val scoreLiveData: MutableLiveData<Int> = MutableLiveData()

    // LiveData to hold the high score
    val highScoreLiveData: MutableLiveData<Int> = MutableLiveData()

    // Function to update the score
    fun updateScore(score: Int) {
        scoreLiveData.value = score
    }

    // Function to update the high score
    fun updateHighScore(highScore: Int) {
        highScoreLiveData.value = highScore
    }
}
