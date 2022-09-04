package kerim.android.bade

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomnavigation.BottomNavigationView
import kerim.android.bade.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){


    //private lateinit var badestederAdapter: BadestederAdapter
    private val viewModel :MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        //AppCompatDelegate.MODE_NIGHT_NO
        setContentView(view)

        if (savedInstanceState == null) {
            viewModel.loadTseryData()
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out2,
                    R.anim.slide_in2,
                    R.anim.slide_out
                )
                .replace(R.id.container, ListFragment.newInstance())
                .commitNow()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.btn_list ->
                    if (supportFragmentManager.fragments.last() !is ListFragment){
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.slide_out2,
                                R.anim.slide_in2,
                                R.anim.slide_out
                            )
                            .replace(R.id.container, ListFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                R.id.btn_map ->
                    if (supportFragmentManager.fragments.last() !is MapsFragment) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.slide_in2,
                                R.anim.slide_out,
                                R.anim.slide_in,
                                R.anim.slide_out2
                            )
                            .replace(R.id.container, MapsFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
                R.id.btn_settings ->
                    if (supportFragmentManager.fragments.last() !is SettingsFragment) {
                        supportFragmentManager.beginTransaction()
                            .setCustomAnimations(
                                R.anim.slide_in2,
                                R.anim.slide_out,
                                R.anim.slide_in,
                                R.anim.slide_out2
                            )
                            .replace(R.id.container, SettingsFragment.newInstance())
                            .addToBackStack(null)
                            .commit()
                    }
            }
            true
        }
    }

    fun switchToInfoFragment(pos : LatLng){
        if (supportFragmentManager.fragments.last() !is DetailsFragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in2,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out2
                )
                .replace(R.id.container, DetailsFragment.newInstance(pos))
                .addToBackStack(null)
                .commit()
        }
    }
}
