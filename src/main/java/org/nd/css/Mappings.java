package org.nd.css;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.helger.css.property.ECSSProperty;


public class Mappings {
	
	public static final Map<String, String> PropertyName;
    static
    {
    	PropertyName = new HashMap<String, String>();
    	PropertyName.put( "margin-left", "margin-right");
    	PropertyName.put( "margin-right", "margin-left");
    	PropertyName.put( "padding-left", "padding-right");
    	PropertyName.put( "padding-right", "padding-left");
    	PropertyName.put( "border-left", "border-right");
    	PropertyName.put( "border-right", "border-left");
    	PropertyName.put( "border-left-color", "border-right-color");
    	PropertyName.put( "border-right-color", "border-left-color");
    	PropertyName.put( "border-left-width", "border-right-width");
    	PropertyName.put( "border-right-width", "border-left-width");
    	PropertyName.put( "border-radius-bottomleft", "border-radius-bottomright");
    	PropertyName.put( "border-radius-bottomright", "border-radius-bottomleft");
    	PropertyName.put( "border-bottom-right-radius", "border-bottom-left-radius");
    	PropertyName.put( "border-bottom-left-radius", "border-bottom-right-radius");
    	PropertyName.put( "-webkit-border-bottom-right-radius", "-webkit-border-bottom-left-radius");
    	PropertyName.put( "-webkit-border-bottom-left-radius", "-webkit-border-bottom-right-radius");
    	PropertyName.put( "-moz-border-radius-bottomright", "-moz-border-radius-bottomleft");
    	PropertyName.put( "-moz-border-radius-bottomleft", "-moz-border-radius-bottomright");
    	PropertyName.put( "border-radius-topleft", "border-radius-topright");
    	PropertyName.put( "border-radius-topright", "border-radius-topleft");
    	PropertyName.put( "border-top-right-radius", "border-top-left-radius");
    	PropertyName.put( "border-top-left-radius", "border-top-right-radius");
    	PropertyName.put( "-webkit-border-top-right-radius", "-webkit-border-top-left-radius");
    	PropertyName.put( "-webkit-border-top-left-radius", "-webkit-border-top-right-radius");
    	PropertyName.put( "-moz-border-radius-topright", "-moz-border-radius-topleft");
    	PropertyName.put( "-moz-border-radius-topleft", "-moz-border-radius-topright");
    	PropertyName.put( "left", "right");
    	PropertyName.put( "right", "left");
    }
    
    public static final List<String> LeftRight;
    static
    {
    	LeftRight = new ArrayList<String>();
    	LeftRight.add("float");
    	LeftRight.add("text-align");
    	LeftRight.add("clear");
    	LeftRight.add("float");
    }
    
    public static final List<String> Direction;
    static
    {
    	Direction = new ArrayList<String>();
    	Direction.add("direction");
    }
    
    public static final Map<String, ECSSProperty> Quad;
    static
    {
    	Quad = new HashMap<String, ECSSProperty>();
    	Quad.put("padding", ECSSProperty.PADDING);
    	Quad.put("margin", ECSSProperty.MARGIN);
    	Quad.put("border-color", ECSSProperty.BORDER_COLOR);
    	Quad.put("border-width", ECSSProperty.BORDER_WIDTH);
    	Quad.put("border-style", ECSSProperty.BORDER_STYLE);
    }
    
    public static final List<String> QuadRadius;
    static
    {
    	QuadRadius = new ArrayList<String>();
    	QuadRadius.add("border-radius");
    	QuadRadius.add("-moz-border-radius");
    	QuadRadius.add("-webkit-border-radius");
    }

}
