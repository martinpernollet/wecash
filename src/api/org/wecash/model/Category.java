package org.wecash.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wecash.model.matching.IValueMatcher;

/**
 * A category has a name, a hierarchical path, and a set of
 * regexp {@link Pattern}s
 * 
 * @author Martin
 */
public class Category implements Comparable<Category>{
    public Category(String path) {
        setPath(path);
        setName(getLastSegment());
        this.labelMatchers = new ArrayList<Pattern>();
        this.valueMatchers = new ArrayList<IValueMatcher>();
    }

    public Category(String path, String name) {
        setPath(path);
        this.name = name;
        this.labelMatchers = new ArrayList<Pattern>();
        this.valueMatchers = new ArrayList<IValueMatcher>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.segments = path.split("/");
    }
    
    public String[] getSegments(){
        return segments;
    }
    
    public String getLastSegment(){
        return segments[segments.length-1];
    }
    
    /* MATCHERS */
    
    public boolean accepts(Expense e) {
        for(IValueMatcher m: valueMatchers){
            if(m.accepts(e))
                return true;
        }
        for (Pattern p : labelMatchers) {
            Matcher m = p.matcher(e.getLabel());
            if(m.matches())
                return true;
        }
        return false;
    }

    public List<Pattern> getLabelPatterns() {
        return labelMatchers;
    }

    public void setLabelPatterns(List<Pattern> patterns) {
        this.labelMatchers = patterns;
    }
    
    public void addLabelPattern(Pattern pattern) {
        this.labelMatchers.add(pattern);
    }

    public void addLabelPattern(String pattern) {
        addLabelPattern(pattern, true);
    }
    
    public void addLabelPattern(String pattern, boolean caseInsensitive) {
        if(caseInsensitive)
            addLabelPattern(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
        else
            addLabelPattern(Pattern.compile(pattern));
    }
    
    public void addValueMatcher(IValueMatcher matcher){
        valueMatchers.add(matcher);
    }
    
    /* */
    
    @Override
    public int compareTo(Category arg0) {
        return path.compareTo(arg0.getPath());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((path == null) ? 0 : path.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        if (path == null) {
            if (other.path != null)
                return false;
        } else if (!path.equals(other.path))
            return false;
        return true;
    }

    public String toString(){
        return "Category " + path;
    }
    
    /* */

    protected String name;
    protected String path;
    protected String[] segments;
    
    protected List<Pattern> labelMatchers;
    protected List<IValueMatcher> valueMatchers;
}
