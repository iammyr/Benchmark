/**
* OWASP Benchmark Project
*
* This file is part of the Open Web Application Security Project (OWASP)
* Benchmark Project For details, please see
* <a href="https://www.owasp.org/index.php/Benchmark">https://www.owasp.org/index.php/Benchmark</a>.
*
* The OWASP Benchmark is free software: you can redistribute it and/or modify it under the terms
* of the GNU General Public License as published by the Free Software Foundation, version 2.
*
* The OWASP Benchmark is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details
*
* @author Dave Wichers <a href="https://www.aspectsecurity.com">Aspect Security</a>
* @created 2015
*/

package org.owasp.benchmark.score.report;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYLineAnnotation;
import org.jfree.chart.annotations.XYPointerAnnotation;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.owasp.benchmark.score.parsers.OverallResult;
import org.owasp.benchmark.score.parsers.OverallResults;

public class Scatter {

    JFreeChart chart = null;
    StandardChartTheme theme = null;
    
    public Scatter(String title, int height, int width, OverallResults or) {
        display("          " + title, height, width, or);
    }

    private JFreeChart display(String title, int height, int width, OverallResults or) {
        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Note: this is a little weird, since each point is a separate series
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Scores");
        for (OverallResult r : or.getResults()) {
            series.add(r.fpr * 100, r.tpr * 100);
        }
        dataset.addSeries(series);

        chart = ChartFactory.createScatterPlot(title, "False Positive Rate", "True Positive Rate", dataset, PlotOrientation.VERTICAL, true, true, false);
        String fontName = "Arial";
        DecimalFormat pctFormat = new DecimalFormat("0'%'");

        theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();
        theme.setExtraLargeFont(new Font(fontName, Font.PLAIN, 24)); // title
        theme.setLargeFont(new Font(fontName, Font.PLAIN, 20)); // axis-title
        theme.setRegularFont(new Font(fontName, Font.PLAIN, 16));
        theme.setSmallFont(new Font(fontName, Font.PLAIN, 12));
        theme.setRangeGridlinePaint(Color.decode("#C0C0C0"));
        theme.setPlotBackgroundPaint(Color.white);
        theme.setChartBackgroundPaint(Color.white);
        theme.setGridBandPaint(Color.red);
        theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
        theme.setBarPainter(new StandardBarPainter());
        theme.setAxisLabelPaint(Color.decode("#666666"));
        theme.apply(chart);

        XYPlot xyplot = chart.getXYPlot();
        NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
        NumberAxis domainAxis = (NumberAxis) xyplot.getDomainAxis();

        xyplot.setOutlineVisible(true);

        rangeAxis.setRange(-9.99, 109.99);
        rangeAxis.setNumberFormatOverride(pctFormat);
        rangeAxis.setTickLabelPaint(Color.decode("#666666"));
        rangeAxis.setMinorTickCount(5);
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        rangeAxis.setAxisLineVisible(true);
        rangeAxis.setMinorTickMarksVisible(true);
        rangeAxis.setTickMarksVisible(true);
        rangeAxis.setLowerMargin(10);
        rangeAxis.setUpperMargin(10);
        xyplot.setRangeGridlineStroke(new BasicStroke());
        xyplot.setRangeGridlinePaint(Color.lightGray);
        xyplot.setRangeMinorGridlinePaint(Color.decode("#DDDDDD"));
        xyplot.setRangeMinorGridlinesVisible(true);        
        
        domainAxis.setRange(-5, 105);
        domainAxis.setNumberFormatOverride(pctFormat);
        domainAxis.setTickLabelPaint(Color.decode("#666666"));
        domainAxis.setMinorTickCount(5);
        domainAxis.setTickUnit(new NumberTickUnit(10));
        domainAxis.setAxisLineVisible(true);
        domainAxis.setTickMarksVisible(true);
        domainAxis.setMinorTickMarksVisible(true);
        domainAxis.setLowerMargin(10);
        domainAxis.setUpperMargin(10);
        xyplot.setDomainGridlineStroke(new BasicStroke());
        xyplot.setDomainGridlinePaint(Color.lightGray);
        xyplot.setDomainMinorGridlinePaint(Color.decode("#DDDDDD"));
        xyplot.setDomainMinorGridlinesVisible(true);

        chart.setTextAntiAlias(true);
        chart.setAntiAlias(true);
        chart.removeLegend();
        chart.setPadding(new RectangleInsets(20, 20, 20, 20));
        xyplot.getRenderer().setSeriesPaint(0, Color.decode("#4572a7"));

//        // setup item labels
//        XYItemRenderer renderer = xyplot.getRenderer();
//        Shape circle = new Ellipse2D.Float(-2.0f, -2.0f, 7.0f, 7.0f);
//        for ( int i = 0; i < dataset.getSeriesCount(); i++ ) {
//            renderer.setSeriesShape(i, circle);
//            renderer.setSeriesPaint(i, Color.blue);
//            String letter = "" + ((String)dataset.getSeries(i).getKey()).charAt(0);
//            StandardXYItemLabelGenerator generator = new StandardXYItemLabelGenerator(letter); 
//            renderer.setSeriesItemLabelGenerator(i, generator); 
//            renderer.setSeriesItemLabelsVisible(i, true);
//            ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER );
//            renderer.setSeriesPositiveItemLabelPosition(i, position);
//        }
     
        makeDataLabels( or, xyplot );
        makeLegend( or, 57, 48, dataset, xyplot );
        
        // put legend inside plot
//        LegendTitle lt = new LegendTitle(xyplot);
//        lt.setItemFont(theme.getSmallFont());
//        lt.setPosition(RectangleEdge.RIGHT);
//        lt.setItemFont(theme.getSmallFont());
//        XYTitleAnnotation ta = new XYTitleAnnotation(.7, .55, lt, RectangleAnchor.TOP_LEFT);
//        ta.setMaxWidth(0.48);
//        xyplot.addAnnotation(ta);

        // draw guessing line
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 6, 3 }, 0);
        XYLineAnnotation guessing = new XYLineAnnotation(-5, -5, 105, 105, dashed, Color.red);
        xyplot.addAnnotation(guessing);

        XYPointerAnnotation worse = makePointer( 75, 0, "Worse than guessing", TextAnchor.TOP_CENTER, 90);
        xyplot.addAnnotation(worse);

        XYPointerAnnotation better = makePointer( 25, 100,"Better than guessing", TextAnchor.BOTTOM_CENTER, 270);
        xyplot.addAnnotation(better);

        XYTextAnnotation time = new XYTextAnnotation("Tool run time: " + or.getTime(), 12, -5.6);
        time.setTextAnchor( TextAnchor.TOP_LEFT);
        time.setFont( theme.getRegularFont() );
        time.setPaint( Color.red );
        xyplot.addAnnotation( time );

        XYTextAnnotation stroketext = new XYTextAnnotation("                     Random Guess", 88, 107);
        stroketext.setTextAnchor(TextAnchor.CENTER_RIGHT);
        stroketext.setBackgroundPaint(Color.white);
        stroketext.setPaint(Color.red);
        stroketext.setFont(theme.getRegularFont());
        xyplot.addAnnotation(stroketext);

        XYLineAnnotation strokekey = new XYLineAnnotation(58, 107, 68, 107, dashed, Color.red);
        xyplot.setBackgroundPaint(Color.white);
        xyplot.addAnnotation(strokekey);

        ChartPanel cp = new ChartPanel(chart, height, width, 400, 400, 1200, 1200, false, false, false, false, false, false);
        f.add(cp);
        f.pack();
        f.setLocationRelativeTo(null);
//      f.setVisible(true);
        return chart;
    }

    
    private void makeDataLabels( OverallResults or, XYPlot xyplot ) {        
        HashMap<Point2D,String> map = makePointList( or );
        for (Entry<Point2D,String> e : map.entrySet() ) {
            if ( e.getValue() != null ) {
                Point2D p = e.getKey();
                String label = sort( e.getValue() );
                XYTextAnnotation annotation = new XYTextAnnotation( label, p.getX(), p.getY());
                annotation.setTextAnchor( p.getX() < 3 ? TextAnchor.TOP_LEFT : TextAnchor.TOP_CENTER);
                annotation.setBackgroundPaint(Color.white);
                annotation.setPaint(Color.blue);
                annotation.setFont(theme.getRegularFont());
                xyplot.addAnnotation(annotation);
            }
        }
    }

    private String sort(String value) {
        String[] parts = value.split( "," );
        Arrays.sort( parts );
        StringBuilder sb = new StringBuilder();
        for ( int i=0; i < parts.length; i++ ) {
            sb.append( parts[i] );
            if ( i < parts.length-1 ) sb.append( "," );
        }
        return sb.toString();
    }
    
    SecureRandom sr = new SecureRandom();
    private HashMap<Point2D, String> makePointList( OverallResults or ) {  
        HashMap<Point2D,String> map = new HashMap<Point2D, String>();
        char ch = 'A';
        
        // make a list of all points.  Add in a tiny random to prevent exact duplicate coordinates in map
        for (OverallResult r : or.getResults()) {
            double x = r.fpr * 100 + sr.nextDouble() * .000001;
            double y = r.tpr * 100 + sr.nextDouble() * .000001 - 1;   // this puts the label just below the point
            Point2D p = new Point2D.Double(x,y);
            String label = ""+ch ;
            map.put( p, label );
            ch++;
        }
        dedupify( map );
        return map;
    }

    private void dedupify(HashMap<Point2D, String> map) {
        for (Entry<Point2D,String> e1 : map.entrySet() ) {
            Entry<Point2D, String> e2 = getMatch( map, e1 );
            while ( e2 != null ) {
                StringBuilder label = new StringBuilder();
                if ( e1.getValue() != null ) label.append( e1.getValue() );
                if ( e1.getValue() != null && e2.getValue() != null ) label.append( "," );
                if ( e2.getValue() != null ) label.append( e2.getValue() );
                e1.setValue( label.toString() );
                e2.setValue(null);
                e2 = getMatch( map, e1 );
            }
        }
    }

    private Entry<Point2D, String> getMatch(HashMap<Point2D, String> map, Entry<Point2D,String> e1) {
        for (Entry<Point2D,String> e2 : map.entrySet() ) {
            Double xd = Math.abs( e1.getKey().getX() - e2.getKey().getX() );
            Double yd = Math.abs( e1.getKey().getY() - e2.getKey().getY() );
            boolean close = xd < 1 && yd < 3;
            if ( e1 != e2 && e1.getValue() != null && e2.getValue() != null && close ) {
                return e2;
            }
        }
        return null;
    }

    private void makeLegend( OverallResults or, int x, int y, XYSeriesCollection dataset, XYPlot xyplot ) {
        char ch = 'A';
        int i = 0;
        for (OverallResult r : or.getResults()) {
            String label = ( ch == 'I' ? ch + ":  " : ""+ch + ": " );
            int score = (int)(100 * ( r.tpr - r.fpr ));
            String msg = "\u25A0 " + label + r.category + " (" + score + "%)";
            XYTextAnnotation stroketext = new XYTextAnnotation(msg, x, y + i * -3.3);
            stroketext.setTextAnchor(TextAnchor.CENTER_LEFT);
            stroketext.setBackgroundPaint(Color.white);
            stroketext.setPaint(Color.blue);
            stroketext.setFont(theme.getRegularFont());
            xyplot.addAnnotation(stroketext);
            i++;
            ch++;
        }
    }
    
    private XYPointerAnnotation makePointer(int x, int y, String msg, TextAnchor anchor, int angle ) {
        XYPointerAnnotation pointer = new XYPointerAnnotation(msg, x, y, Math.toRadians(angle));
        pointer.setBackgroundPaint(Color.white);
        pointer.setTextAnchor(anchor);
        pointer.setArrowWidth(4);
        pointer.setArrowLength(8);
        pointer.setArrowPaint(Color.red);
        pointer.setLabelOffset(2);
        pointer.setPaint(Color.red);
        pointer.setFont(theme.getRegularFont());
        return pointer;
    }
    
    public void writeChartToFile(File f, int height, int width) throws IOException {
        FileOutputStream stream = new FileOutputStream(f);
        ChartUtilities.writeChartAsPNG(stream, chart, height, width);
        stream.close();
    }

    public static void main(String[] args) throws IOException {
        OverallResults or = new OverallResults();
        or.add("XSS1", .62, .2, 12, 5);
        or.add("XSS2", .64, .2, 12, 5);
        
        or.add("XSS25", .5, .3, 12, 5);
        or.add("XSS26", .5, .3, 12, 5);
        
        or.add("XSS31", .34, .2, 12, 5);
        or.add("XSS32", .36, .2, 12, 5);
        or.add("XSS33", .38, .2, 12, 5);
        
        or.add("XSS4", .72, .22, 12, 5);
        or.add("XSS5", .72, .18, 12, 5);
        or.add("XSS6", .28, .19, 12, 5);
        or.add("XSS7", .28, .21, 12, 5);
        or.add("SQL Injection", 1, .5, 1000, 5);
        or.add("Header Injection", 0, .5, 4, 5);
        or.add("Reflection Injection", 0, 0, 300, 5);
        or.add("LDAP Injection", .5, 1, 6, 5);
        or.add("Weak Encryption", .2, .9, 600, 5);
        Scatter scatter = new Scatter("OWASP Benchmark Results for SomeTool", 800, 800, or);
        scatter.writeChartToFile(new File("test.png"), 800, 800);
        System.exit(0);
    }
}