package development.dreamcatcher.nurvvplotter.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import development.dreamcatcher.nurvvplotter.R
import kotlinx.android.synthetic.main.graph_fragment_layout.*
import java.util.*
import android.graphics.Color


class UniversalGraph : Fragment() {

    private val seriesToLoad = LinkedList<LineGraphSeries<DataPoint>>()
    private val valueDescriptionsToLoad = LinkedList<Int>()
    private val verticalPointerLineSeries = LineGraphSeries<DataPoint>()

    private var verticalTopPoint: Double? = null
    private var verticalBottomPoint: Double? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.graph_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        seriesToLoad.forEachIndexed { index, dataSeries ->

            initializeVerticalPointerLine()

            // Upload data series into the graph.
            graph.addSeries(dataSeries)

            // Add TextView for current value displaying.
            val viewInflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val viewRow = viewInflater.inflate(R.layout.current_value_row, null)
            val currentValueText = viewRow.findViewById(R.id.current_value_text) as TextView
            val currentValueColorPreview = viewRow.findViewById(R.id.current_value_color_preview) as View
            val valueDescriptionId = valueDescriptionsToLoad[index]
            currentValueText.text = getString(valueDescriptionId, -1)
            this.current_values_section.addView(viewRow)

            // Set color of this data series.
            val colorId = getRandomColorNumber()
            dataSeries.color = colorId
            currentValueText.setTextColor(colorId)
            currentValueColorPreview.setBackgroundColor(colorId)

            // Set the listener that will be updating current value.
            dataSeries.setOnDataPointTapListener{ _, dataPoint ->
                val newDescriptiveValue = getString(valueDescriptionId, dataPoint.y.toInt())
                currentValueText.text = newDescriptiveValue

                // Update position of the vertical pointer line.
                updateVerticalPointerLine(dataPoint.x)
            }
        }

        // Set the viewport scrollable, if the data set is too big to be displayed entirely.
        graph.viewport.isScalable = true
    }

    fun addDataSeries(inputValuesX: String, inputValuesY: String, valueDescription: Int) {

        valueDescriptionsToLoad.add(valueDescription)

        val valuesX = inputValuesX.split(" ").toList()
        val valuesY = inputValuesY.split(" ").toList()
        val dataSeries = LineGraphSeries<DataPoint>()

        valuesX.forEachIndexed{ index, value ->

            // Prepare the points.
            val dataPoint = DataPoint(value.toDouble(), valuesY[index].toDouble())

            // Attach created points to the series list.
            dataSeries.appendData(dataPoint, true, 100, false)
        }

        // Add series into the loading list.
        seriesToLoad.add(dataSeries)
    }

    private fun initializeVerticalPointerLine() {
        verticalBottomPoint = graph.viewport.getMinY(true)
        verticalTopPoint = graph.viewport.getMaxY(true)
        verticalPointerLineSeries.thickness = 2
        graph.addSeries(verticalPointerLineSeries)
        val middlePoint = (graph.viewport.getMinX(true) + graph.viewport.getMaxX(true)) / 2.0
        updateVerticalPointerLine(middlePoint)
    }

    private fun updateVerticalPointerLine(position: Double) {
        val topPoint = DataPoint(position, verticalTopPoint!!)
        val bottomPoint = DataPoint(position, verticalBottomPoint!!)
        verticalPointerLineSeries.resetData(arrayOf(bottomPoint, topPoint))
    }

    private fun getRandomColorNumber() : Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(128), rnd.nextInt(128), rnd.nextInt(128))
    }
}

