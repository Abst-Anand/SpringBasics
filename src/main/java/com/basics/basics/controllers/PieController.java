package com.basics.basics.controllers;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import jakarta.servlet.http.HttpServletResponse;


import java.io.IOException;
import java.text.AttributedString;
import java.text.DecimalFormat;

@Controller
public class PieController {

    @GetMapping("/pie")
    public void generatePieChart(HttpServletResponse response) throws IOException {
        // Create dataset
        DefaultPieDataset dataset = createDataset();

        // Create chart
        JFreeChart pieChart = createChart(dataset);

        // Set the content type for the response
        response.setContentType("image/png");

        // Write the chart as a PNG image
        ChartUtils.writeChartAsPNG(response.getOutputStream(), pieChart, 800, 600);
    }

    /**
     * Private method to create the dataset for the pie chart.
     */
    private DefaultPieDataset createDataset() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Category A", 20);
        dataset.setValue("Category B", 30);
        dataset.setValue("Category C", 20);
        dataset.setValue("Category D", 10);
        return dataset;
    }

    /**
     * Private method to create the pie chart based on the dataset.
     */
    private JFreeChart createChart(DefaultPieDataset dataset) {
        JFreeChart pieChart = ChartFactory.createPieChart(
                "Sample Pie Chart", // Chart title
                dataset,            // Data
                true,               // Include legend
                true,
                false
        );

        // Customize the chart plot
        customizePlot((PiePlot) pieChart.getPlot(), dataset);

        return pieChart;
    }

    /**
     * Private method to customize the plot by adding label generator.
     */
    private void customizePlot(PiePlot plot, PieDataset dataset) {
        plot.setSimpleLabels(false);  // Set to 'false' to enable rich labels

        // Set custom label generator to show percentage and values
        plot.setLabelGenerator(createLabelGenerator(dataset));
    }

    /**
     * Private method to create a custom label generator to show value and percentage.
     */
    private PieSectionLabelGenerator createLabelGenerator(PieDataset dataset) {
        return new PieSectionLabelGenerator() {
            private final DecimalFormat percentageFormat = new DecimalFormat("0.00%");

            @Override
            public String generateSectionLabel(PieDataset dataset, Comparable key) {
                // Value and percentage calculation
                Number value = dataset.getValue(key);
                double total = dataset.getValue("Category A").doubleValue() + dataset.getValue("Category B").doubleValue() +
                        dataset.getValue("Category C").doubleValue() + dataset.getValue("Category D").doubleValue();
                String percentage = percentageFormat.format(value.doubleValue() / total);
                return key + ": " + value + " (" + percentage + ")";
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable key) {
                return null;  // You can return null here if no attributed label is needed.
            }
        };
    }


}
