/*
  Copyright 2006 by Sean Luke and George Mason University
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/

package sim.util;

/** Defines an inclusive (closed) interval between two numerical values MIN and MAX.  
    Beware that if you pass in an integer to Interval, it is converted internally to a Long, 
    and not to a Double.  */

public class Interval
    {
    public Interval(long min, long max)
        {
        this.min = Long.valueOf(min);
        this.max = Long.valueOf(max);
        isDouble = false;
        }
    
    public Interval(double min, double max)
        {
        //this.min = new Double(min);
        this.min = Double.valueOf(min);
        //this.max = new Double(max);
        this.max = Double.valueOf(max);
        isDouble = true;
        }
            
    Number min;
    Number max;
    boolean isDouble;
    
    public Number getMin() { return min; }
    public Number getMax() { return max; }
    public boolean isDouble() { return isDouble; }
    
    public boolean contains(Number val) { return contains(val.doubleValue()); }
    public boolean contains(double val) { return (val >= min.doubleValue() && val <= max.doubleValue()); }

    }
