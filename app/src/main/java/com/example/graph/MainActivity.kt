package com.example.graph

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.Utils

class MainActivity : AppCompatActivity() {
    private lateinit var chart: LineChart
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        run {
            // // Chart Style // //
            chart = findViewById(R.id.chart1)

            // background color
            chart.setBackgroundColor(Color.WHITE)

            // disable description text
            chart.description.isEnabled = false

            // enable touch gestures
            chart.setTouchEnabled(true)

            // set listeners
            val mv = MyMarkerView(this, R.layout.custom_marker_view)
            mv.chartView = chart
            chart.marker = mv

            // enable scaling and dragging
            chart.isDragEnabled = true
            chart.setScaleEnabled(true)
            chart.setPinchZoom(true)
        }

        val xAxis: XAxis = chart.xAxis

        var yAxis: YAxis
       run {   // // Y-Axis Style // //
            yAxis = chart.axisLeft

            // disable dual axis (only use LEFT axis)
            chart.axisRight.isEnabled = false

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f)

            // axis range
            yAxis.axisMaximum = 200f
            yAxis.axisMinimum = -10f
        }
        val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
        xAxis.labelCount = months.size // Set the label count to the number of months
        xAxis.setAvoidFirstLastClipping(true) // Avoid clipping of the first and last labels
        xAxis.position = XAxis.XAxisPosition.TOP

        chart.setExtraOffsets(10f, 0f, 10f, 10f) // Add extra offset to accommodate the labels
        chart.xAxis.textSize = 10f

        xAxis.setCenterAxisLabels(true) // Center the labels between the grid lines
        xAxis.granularity = 1f // Display one label per interval
        xAxis.labelRotationAngle = -45f // Rotate the labels by -45 degrees for better visibility

        setData(8,9f)
        chart.animateX(1500)

        val legend = chart.legend
        legend.form = LegendForm.LINE

        chart.invalidate()



    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setData(count: Int, range: Float) {
        val dataPoints = listOf(50f, 80f, 120f, 90f, 110f, 70f, 100f, 130f, 160f, 140f, 100f, 120f)
        val values = ArrayList<Entry>()
        for (i in dataPoints.indices) {
            val value = dataPoints[i]
            val entry = Entry(i.toFloat(), value,resources.getDrawable(R.drawable.fade_red))
            values.add(entry)
        }
        val set1: LineDataSet
        if (chart.data != null &&
            chart.data.dataSetCount > 0
        ) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = values
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set1 = LineDataSet(values, "DataSet 1")
            set1.setDrawIcons(false)

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f)

            // black lines and points
            set1.color = Color.BLACK
            set1.setCircleColor(Color.BLACK)

            // line thickness and point size
            set1.lineWidth = 1f
            set1.circleRadius = 3f

            // draw points as solid circles
            set1.setDrawCircleHole(false)

            // customize legend entry
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f

            // text size of values
            set1.valueTextSize = 9f

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            // set the filled area
            set1.setDrawFilled(true)
            set1.fillFormatter =
                IFillFormatter { dataSet, dataProvider -> chart.axisLeft.axisMinimum }

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(this, R.drawable.fade_red)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }
            val dataSets = ArrayList<ILineDataSet>()
            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }

}