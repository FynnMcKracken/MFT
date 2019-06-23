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
import com.github.mikephil.charting.utils.ColorTemplate.rgb
import com.github.mikephil.charting.utils.MPPointF
import de.mckracken.mft.MultinoxApplication
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
        val colors = mutableListOf<Int>()
        for (device in MultinoxApplication().devices.sortedBy { it.channelStart }) {
            if(pointer + device.channelStart > 0) {
                entries.add(
                    PieEntry((device.channelStart - pointer) / 512f,
                    "<empty>")
                )
                colors.add( rgb("#455A64"))
            }
            entries.add(
                PieEntry(device.channelWidth / 512f,
                    device.title)
            )
            colors.add(rgb("#CFD8DC"))
            pointer = device.channelStart + device.channelWidth
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)

        // add a lot of colors

        /*for (c in ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS)
            colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS)
            colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS)
            colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())*/

        dataSet.colors = colors

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
