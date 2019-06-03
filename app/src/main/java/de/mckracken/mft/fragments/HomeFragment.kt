package de.mckracken.mft.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import de.mckracken.mft.model.TestData
import de.mckracken.mft.viewmodel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var chart : PieChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(de.mckracken.mft.R.layout.fragment_home, container, false)
        chart = view.channelChart
        chart.description.isEnabled = false
        chart.setExtraOffsets(5f, 10f, 5f, 5f)

        chart.dragDecelerationFrictionCoef = 0.95f

        //chart.setCenterTextTypeface(tfLight)
        chart.centerText = ""

        chart.isDrawHoleEnabled = true
        chart.setHoleColor(Color.TRANSPARENT)

        chart.setTransparentCircleColor(Color.WHITE)
        chart.setTransparentCircleAlpha(50)

        chart.holeRadius = 35f
        chart.transparentCircleRadius = 40f

        chart.setDrawCenterText(true)

        chart.rotationAngle = 0f
        // enable rotation of the chart by touch
        chart.isRotationEnabled = true
        chart.isHighlightPerTapEnabled = true

        chart.animateY(1400, Easing.EaseInOutQuad)

        // chart.setUnit(" €")
        // chart.setDrawUnitsInchart(true)

        // add a selection listener
        //chart.setOnchartValueSelectedListener(this)

        setData()
        
        
        return view
        
    }

    fun setData() {
        val entries : MutableList<PieEntry> = mutableListOf()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.

        var pointer = 0
        for (device in TestData.Devices.sortedBy { it.channelStart }) {
            if(pointer + device.channelStart > 0) {
                Log.d("SET CHART DATA", "Adding <empty> data with $pointer and ${device.channelStart - pointer}")
                entries.add(
                    PieEntry((device.channelStart - pointer) / 512f,
                    "<empty>")
                )
            }
            Log.d("SET CHART DATA", "Adding device data with $pointer")
            entries.add(
                PieEntry(device.channelWidth / 512f,
                    device.title)
            )
            pointer = device.channelStart + device.channelWidth
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors

        val colors = mutableListOf<Int>()

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        dataSet.selectionShift = 0f;

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(chart))
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight)
        chart.data = data
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
