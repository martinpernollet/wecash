package org.wecash.io.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jzy3d.io.FileReader;
import org.wecash.model.Category;
import org.wecash.model.matching.ValueEqualsMatcher;

public class ModelReader {
    public Set<Category> read(String file) throws IOException {
        Set<Category> categories = new TreeSet<Category>();
        List<String> lines = FileReader.read(file);

        String path = null;
        List<String> patterns = null;
        List<String> values = null;
        
        for (String line : lines) {
            // register a new category
            if (line.startsWith("category:")) {
                // append the previous collected category
                if (path != null) {
                    createAndCategoryWithParents(categories, path, patterns, values);
                }
                
                // start collecting a new category
                path = line.substring(9).trim();
                patterns = new ArrayList<String>();
                values = new ArrayList<String>();
            }
            // collect category patterns
            else if(line.startsWith("pattern:")){
                patterns.add(line.substring(8).trim());
            }
            else if(line.startsWith("value:")){
                values.add(line.substring(6).trim());
            }
        }
        createAndCategoryWithParents(categories, path, patterns, values);
        return categories;
    }

    protected void createAndCategoryWithParents(Set<Category> categories, String path, List<String> patterns, List<String> values) {
        // create category 
        Category category = new Category(path);
        for (String p : patterns)
            category.addLabelPattern(".*" + p + ".*");
        for(String p: values){
            category.addValueMatcher(new ValueEqualsMatcher(Double.parseDouble(p)));
        }
        
        // add category if not exist by path
        categories.add(category);
                
        // add any possible parent
        StringBuilder fullPath = new StringBuilder();
        int n = category.getSegments().length;
        for(int i=0; i<n-1;i++){
            String pathSegment = category.getSegments()[i];
            fullPath.append(pathSegment);
            Category parent = new Category(fullPath.toString());
            categories.add(parent);
            fullPath.append("/");
        }
    }
}
