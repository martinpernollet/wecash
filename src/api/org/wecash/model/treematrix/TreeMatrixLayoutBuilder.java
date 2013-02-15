package org.wecash.model.treematrix;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.wecash.model.Expense;
import org.wecash.operators.IOperator;
import org.wecash.operators.OperatorCount;
import org.wecash.operators.OperatorSum;
import org.wecash.poi.xls.ExcelBuilder;
import org.wecash.poi.xls.ReportLayoutSettings;
import org.wecash.poi.xls.ReportLayoutSettings.SpanMode;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class TreeMatrixLayoutBuilder {
    ExcelBuilder excel = new ExcelBuilder();
    protected CategoryDateTreeMatrix matrix;

    public static int ROW_DATE = 0;

    protected static int CELL_WIDTH_UNIT = 256;
    protected ReportLayoutSettings settings;
    protected int maxDepth;
    protected int startMatrixColumn;
    protected int startTreeRow;
    protected BiMap<Date, Integer> columns;
    
    protected SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
    protected IOperator<Expense> opSum = new OperatorSum();
    protected IOperator<Expense> opCount = new OperatorCount();

    public TreeMatrixLayoutBuilder(CategoryDateTreeMatrix matrix) {
        this.matrix = matrix;
        this.settings = getDefaultSettings();
    }
    
    public ReportLayoutSettings getDefaultSettings() {
        ReportLayoutSettings s = new ReportLayoutSettings();
        s.setUserHeaderHeight(1000);
        s.setUserHeaderRotation(45);
        s.setFileTreeColumnWidth(2); // in number of char
        s.setAclColumnWidth(4);
        s.setDefaultRowHeight(100);
        s.setSplitPaneX(500);
        s.setSplitPaneY(1500);
        s.setFreezePaneRowSplit(1);
        s.setTreeLineCursorRowStart(1);
        s.setSpanMode(SpanMode.COLUMN_OVERFLOW_ON_NEXT_SHEETS);
        s.setZoomRatioDenominator(2);
        s.setZoomRatioNumerator(1);
        s.setPageSize(1000);
        return s;
    }

    public void render(File file) throws IOException {
        renderInit();
        renderHeader();
        renderTree();
        renderMatrix();
        //renderFinal();
        excel.save(file);
    }

    public void renderInit() {
        columns = HashBiMap.create();
        maxDepth = 0;
        for (CategoryDateTreeMatrixItem i : matrix.getTree()) {
            if (i.getDepth() > maxDepth)
                maxDepth = i.getDepth();
        }
        startMatrixColumn = maxDepth + 1;
        startTreeRow = 1;
    }

    public void renderHeader() {
        int c = 0;
        for (Date d : matrix.getColumns()) {
            String label = format.format(d);
            int column = startMatrixColumn + c;
            excel.setCell(ROW_DATE, column, label);
            columns.put(d, column); // index
            
            c++;
        }
    }

    public void renderTree() {
        int r = 0;
        for (CategoryDateTreeMatrixItem i : matrix.getTree()) {
            String label = i.getLabel();
            int depth = i.getDepth();
            int row = startTreeRow + r;
            excel.setCell(row, depth, label);

            // tree cell width
            if (depth < this.maxDepth) {
                excel.setColumnWidth(depth, (int) (settings.getFileTreeColumnWidth() * CELL_WIDTH_UNIT));
            }
            else{
                excel.setColumnWidthAuto(depth);
            }
            
            r++;
        }
    }

    public void renderMatrix() {
        int r = 0;
        for (CategoryDateTreeMatrixItem i : matrix.getTree()) {
            int row = startTreeRow + r;
            for (Date d : matrix.getColumns()) {
                int column = columns.get(d);
                Set<Expense> expenses = i.getValueFor(d);

                double count = opCount.apply(expenses);
                if(count>0){
                    double sum = opSum.apply(expenses);
                    //String label = opSum.apply(expenses) + " (" + opCount.apply(expenses) + ")";
                    Cell cell = excel.setCell(row, column, sum);
                    
                    // add all expense as comment
                    
                    if(expenses!=null){
                        //int maxLineLength = 0;
                        StringBuilder eList = new StringBuilder();
                        for(Expense e: expenses){
                            String line = e.getLabel() + "  " + e.getValue() + "\n";
                            //if(line.length()>maxLineLength)
                                
                            eList.append(line);
                        }
                        excel.addComment(cell, eList.toString(), row, column, 12, expenses.size()+1);
                    }
                }
            }
            r++;
        }
    }

    protected void formatFileTreeCellLayout(int maxDepth, int minDepth, int colStart) {
        int realMax = maxDepth - minDepth;
        for (int i = 0; i < realMax; i++) {
            excel.setColumnWidth(i, (int) (settings.getFileTreeColumnWidth() * CELL_WIDTH_UNIT));
        }
        excel.setColumnWidthAuto(realMax);
        excel.setFreezePane(colStart, settings.getFreezePaneRowSplit());
    }

}
