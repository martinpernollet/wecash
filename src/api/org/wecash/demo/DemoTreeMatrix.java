package org.wecash.demo;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jzy3d.io.FileReader;
import org.jzy3d.utils.LoggerUtils;
import org.wecash.index.CategoryIndex;
import org.wecash.index.IndexIntersection;
import org.wecash.index.MonthIndex;
import org.wecash.io.accounts.AbstractAccountReader;
import org.wecash.io.accounts.CAAccountReader;
import org.wecash.io.accounts.SGAccountReader;
import org.wecash.io.model.ModelReader;
import org.wecash.model.Category;
import org.wecash.model.Expense;
import org.wecash.model.treematrix.CategoryDateTreeMatrix;
import org.wecash.model.treematrix.CategoryDateTreeMatrixItem;
import org.wecash.model.treematrix.TreeMatrixLayoutBuilder;

public class DemoTreeMatrix {
    public static String input = "data/input/";
    public static String model = "data/model/";
    public static String output = "data/output/";
    
    public static void main(String[] args) throws InvalidFormatException, IOException, ParseException {
        LoggerUtils.minimal();
        
        String model = "model.txt";
        
        analyze(new SGAccountReader(), "family.xls", model, "analysis-family.xls", "analysis-family.txt");
        analyze(new CAAccountReader(), "martin.xlsx", model, "analysis-martin.xls", "analysis-martin.txt");
        analyze(new SGAccountReader(), "manu.xls", model, "analysis-manu.xls", "analysis-manu.txt");
    }

    protected static void analyze(AbstractAccountReader reader, String owner, String rules, String xlMatrix, String textTree) throws InvalidFormatException, IOException, ParseException {
        owner = input + owner;
        rules = model + rules;
        xlMatrix = output + xlMatrix;
        textTree = output + textTree;
        // Load an account
        List<Expense> expenses = reader.read(new File(owner));
        ModelReader cr = new ModelReader();
        Set<Category> categories = cr.read(rules);

        CategoryDateTreeMatrix mat = makeTreeMatrix(categories, expenses);
        //mat.console();
          
        // Export excel
        TreeMatrixLayoutBuilder builder = new TreeMatrixLayoutBuilder(mat);
        builder.render(new File(xlMatrix));
        
        // Export text
        FileReader.write(textTree, mat.toString(new ArrayList<String>()));
    }

    protected static CategoryDateTreeMatrix makeTreeMatrix(Set<Category> categories, List<Expense> expenses) throws IOException {
        // Load and build index
        CategoryIndex categoryIndex = new CategoryIndex(categories);
        categoryIndex.index(expenses);

        MonthIndex monthIndex = new MonthIndex();
        monthIndex.index(expenses);

        // Intersect indexes
        IndexIntersection<Category, Date, Expense> intersect = new IndexIntersection<Category, Date, Expense>(categoryIndex, monthIndex);
        intersect.build();
        //intersect.console();

        // Build tree matrix items;
        TreeSet<CategoryDateTreeMatrixItem> tree = new  TreeSet<CategoryDateTreeMatrixItem>();
        for (Category c : intersect.getIndex1().getCategories()) {
            Map<Date,Set<Expense>> map = new HashMap<Date,Set<Expense>>();
            for (Date d : intersect.getIndex2().getCategories()) {
                map.put(d, intersect.getIndexedIntersection(c, d));
            }
            CategoryDateTreeMatrixItem item = new CategoryDateTreeMatrixItem(c,map);
            tree.add(item);
        }
        for(Category c: categories){
            // add categories not appearing in index intersection
            CategoryDateTreeMatrixItem item = new CategoryDateTreeMatrixItem(c);
            if(!tree.contains(item)){
                tree.add(item);
            }
        }
        
        // Build tree matrix
        List<Date> columns = new ArrayList<Date>(intersect.getIndex2().getCategories());
        CategoryDateTreeMatrix mat = new CategoryDateTreeMatrix(tree, columns);
        return mat;
    }
    
    /*
     * Set<Category> cs = new HashSet<Category>();
    Category cat1 = new Category("home", "home");
    cs.add(cat1);

    Category cat = new Category("home/food", "food");
    cat.addPattern(".*MCDONALDS.*");
    cat.addPattern(".*FRANPRIX.*");
    cat.addPattern(".*ALIMENTATION.*");
    cs.add(cat);*/
}
