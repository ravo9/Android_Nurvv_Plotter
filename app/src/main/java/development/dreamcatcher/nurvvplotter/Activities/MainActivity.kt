package development.dreamcatcher.nurvvplotter.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import development.dreamcatcher.nurvvplotter.Fragments.UniversalGraph
import development.dreamcatcher.nurvvplotter.R


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Add fragment with the Universal Graph
        val universalGraph = UniversalGraph()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_container, universalGraph).commitNow()

        // Add data series
        universalGraph.addDataSeries(
            getString(R.string.time_values),
            getString(R.string.y_values),
            R.string.y_value_description)

        universalGraph.addDataSeries(
            getString(R.string.time_values),
            getString(R.string.deviation_values),
            R.string.deviation_value_description)
    }
}