package ntou.android2024.ntou_credit_calculation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ntou.android2024.ntou_credit_calculation.databinding.ActivityMainBinding
import java.io.FileNotFoundException
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        val pdf:View = findViewById(R.id.navigation_notifications)
        pdf.setOnClickListener{
            try {
                val inputStream = openFileInput("save.txt")
                val bytes = ByteArray(inputStream.available())
                val sb = StringBuffer()
                while (inputStream.read(bytes) != -1 && inputStream.read(bytes) != 0) {
                    sb.append(String(bytes))
                }
                if (sb.length > 1){
                    navController.navigate(R.id.navigation_notifications)
                }
                else{
                    val toast = Toast.makeText(this , "記得先點擊右下存檔喔", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            catch(e: FileNotFoundException) {
                e.printStackTrace()
            }
            catch(e:NumberFormatException) {
                e.printStackTrace()
            }
            catch(e: IOException) {
                e.printStackTrace()
            }
            catch(e:Exception) {
                e.printStackTrace()
            }
        }
        val home:View = findViewById(R.id.navigation_home)
        home.setOnClickListener{
            navController.navigate(R.id.navigation_home)
        }
        val dashboard:View = findViewById(R.id.navigation_dashboard)
        dashboard.setOnClickListener{
            navController.navigate(R.id.navigation_dashboard)
        }
    }

}