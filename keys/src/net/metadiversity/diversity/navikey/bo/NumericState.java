/*
 * Copyright (C) 2001-2006 University Bayreuth, BayCEER, Dept. of Mycology 
 * 
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.metadiversity.diversity.navikey.bo;
import java.util.StringTokenizer;
import java.util.Vector;

import net.metadiversity.diversity.navikey.delta.DeltaData;

/**
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
public class NumericState extends State
{
    private boolean isReal;
    
    private boolean extremeUpperFlag;
    private boolean upperFlag;
    private boolean medianFlag;
    private boolean lowerFlag;
    private boolean extremeLowerFlag;
    
    private boolean initFlag;
    
    private double upper;
    private double lower;
    private double median;
    private double extremeUpper;
    private double extremeLower;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public NumericState()
    {
        super();

        extremeUpperFlag    = false;
        upperFlag           = false;
        medianFlag          = false;
        lowerFlag           = false;
        extremeLowerFlag    = false;
        
        initFlag            = false;
        /*
        extremeLower = lower = java.lang.Double.MAX_VALUE;
        extremeUpper = upper = java.lang.Double.MIN_VALUE;
         */
    }
    //--------------------------------------------------------------------------
    // DIDI: ??? can't used with neg. values ???
    //--------------------------------------------------------------------------
    public NumericState( BasicState bs, DeltaData dd )
    {
        super();
        setCharacterId( bs.getCharacterId() );
// ???        setItemCharacter( ItemCharacter.restore( getCharacterId() ) );
        setItemCharacter( (ItemCharacter) dd.getItemCharacters().get( getCharacterId() ) );
        
        String str = bs.getName();        
        StringBuffer sb = new StringBuffer();        
        //filter out unwanted characters
        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt(i);
            if(c == '(' ||  c == ')' || c == '-' || c == '.' || Character.isDigit(c))
            {
                sb.append(c);
            }
        }
        //if nothing is left then get out of here.
        if( sb.length() == 0 )
        {
            return;
        }
        //create a position marker
        int position = 0;
        
        StringBuffer extremeLower = new StringBuffer();
        //if there's a lower extreme range
        if( sb.charAt(0) == '(' )
        {
            position++; //move ahead
            //keep going until there's a close parantheses
            while(sb.charAt(position) != ')' && position < sb.length())
            {
                char c = sb.charAt(position);
                if( Character.isDigit(c) || c == '.')
                {
                    extremeLower.append( c );
                }
                position++;
            }
        }        
        // set the value for extreme lower
        if(extremeLower.length() > 0)
        {
            Double d = new Double(new String(extremeLower));
            setExtremeLower( d.doubleValue() );
        }
        
        //move past the closed paretheses, if it exists
        if(sb.charAt(position) == ')')
        {
            position++; //move ahead
        }        
        //now get lower-median-upper set.        
        StringBuffer lowerMedianUpper = new StringBuffer();
        
        while( position < sb.length() && sb.charAt(position) != '(' )
        {
            lowerMedianUpper.append( sb.charAt(position) );
            position++;
        }
        //split into lower, median, upper
        StringTokenizer st = new StringTokenizer(new String(lowerMedianUpper), "-");
        int count = 0;
        String typeAbbrev = "";
        Vector<String> values = new Vector<String>();
        while(st.hasMoreTokens())
        {
            count++;
            values.addElement( st.nextToken() );
        }
        if( values.size() == 1 )
        {
            Double d = new Double(values.elementAt(0));
            setMedian( d.doubleValue() );
        }
        else if(values.size() == 2)
        {
            Double d = new Double(values.elementAt(0));
            setLower(d.doubleValue());
            
            d = new Double(values.elementAt(1));
            setUpper(d.doubleValue());
        }
        else if( values.size() == 3 )
        {
            
            Double d = new Double(values.elementAt(0));
            setLower( d.doubleValue() );
            
            d = new Double(values.elementAt(1));
            setMedian( d.doubleValue() );
            
            d = new Double(values.elementAt(2));
            setUpper( d.doubleValue() );
        }        
        StringBuffer extremeUpper = new StringBuffer();
        while( position < sb.length() && sb.charAt(position) != ')' )
        {
            char c = sb.charAt(position);
            if( Character.isDigit(c) || c == '.')
            {
                extremeUpper.append(c);
            }
            position++;
        }
        // set the value for extreme upper
        if( extremeUpper.length() > 0 )
        {
            Double d = new Double(new String(extremeUpper));
            setExtremeUpper( d.doubleValue() );
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public boolean containedIn(Vector v)
    {
        for( int i = 0; i < v.size(); i++ )
        {
            NumericState ns = (NumericState) v.elementAt( i );
            if( fallsWithinExtremeRange( ns ) )
            {
                return true;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void initValues( double d )
    {
        initFlag = true;

        extremeLower    = d;
        lower           = d;
        median          = d;
        upper           = d;
        extremeUpper    = d;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setLower(double d)
    {
        lowerFlag = true;
        
        if( ! initFlag ) 
        { 
            initValues( d );
            return;
        }
        lower = d; 
        
        if( extremeLower > d )  extremeLower = d;

        if( median < d )        median = d;
        if( upper < d )         upper = d;
        if( extremeUpper < d )  extremeUpper = d;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setUpper(double d)
    {
        upperFlag = true;

        if( ! initFlag ) 
        { 
            initValues( d );
            return;
        }
        
        upper = d;
        
        if( lower > d )         lower = d;
        if( median > d )        median = d;
        if( extremeLower > d )  extremeLower = d;
        
        if( extremeUpper < d )  extremeUpper = d;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setMedian(double d)
    {
        medianFlag = true;

        if( ! initFlag ) 
        { 
            initValues( d );
            return;
        }
        
        median = d;
        
        if( lower > d )         lower = d;
        if( extremeLower > d )  extremeLower = d;
        
        if( upper < d )         upper = d;
        if( extremeUpper < d )  extremeUpper = d;
     }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setExtremeLower(double d)
    {
        extremeLowerFlag = true;

        if( ! initFlag ) 
        { 
            initValues( d );
            return;
        }
        
        extremeLower = d;

        if( lower < d )         lower = d;
        if( median < d )        median = d;
        if( upper < d )         upper = d;
        if( extremeUpper < d )  extremeUpper = d;

    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setExtremeUpper(double d)
    {
        extremeUpperFlag = true;

        if( ! initFlag ) 
        { 
            initValues( d );
            return;
        }
        
        extremeUpper = d;
        
        if( extremeLower > d )  extremeLower = d;
        if( lower > d )         lower = d;
        if( median > d )        median = d;
        if( upper > d )         upper = d;
        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public double getMedian()
    {
        return median;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public double getLower()
    {
        return lower;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public double getUpper()
    {
        return upper;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public double getExtremeLower()
    {
        return extremeLower;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public double getExtremeUpper()
    {
        return extremeUpper;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void isReal( boolean b )
    {
        isReal = b;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean isReal()
    {
        return isReal;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void isInteger( boolean b )
    {  
        isReal = ! b;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean isInteger()
    {
        return ! isReal;
    }
    //--------------------------------------------------------------------------
    //-------------------------------------------------------------------------- 
    public boolean fallsWithinNormalRange(NumericState s)
    {
        if( s.getUpper() <= upper && s.getLower() >= lower )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //--------------------------------------------------------------------------
    //-------------------------------------------------------------------------- 
    public boolean overlappingNormalRange(NumericState s)
    {
        if( s.getUpper() >= lower && s.getLower() <= upper )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //--------------------------------------------------------------------------
    // evaluation interval is s.lower and s.upper
    //--------------------------------------------------------------------------
    public boolean fallsWithinExtremeRange(NumericState s)
    {
        if( s.getUpper() <= extremeUpper && s.getLower() >= extremeLower )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //--------------------------------------------------------------------------
    //-------------------------------------------------------------------------- 
    public boolean overlappingExtremeRange(NumericState s)
    {
        if( s.getUpper() >= extremeLower && s.getLower() <= extremeUpper 
          )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    //Returns a string representation for the values of this state
    public String getStringValue()
    {
        StringBuffer sb = new StringBuffer();
        boolean firstFlag = true;
        
        if( extremeLowerFlag )
        {
            if( firstFlag ) firstFlag = false; else sb.append( " " );
            sb.append( "(min) " + extremeLower );
        }
        if( lowerFlag )
        {
            if( firstFlag ) firstFlag = false; else sb.append( " " );
            sb.append( "(low) " + lower );
        }
        if( medianFlag )
        {
            sb.append( "(median) " + median );
            if( firstFlag ) firstFlag = false; else sb.append( " " );
        }
        if( upperFlag )
        {
            if( firstFlag ) firstFlag = false; else sb.append( " " );
            sb.append( "(high) " + upper );
        }
        if( extremeUpperFlag )
        {
            if( firstFlag ) firstFlag = false; else sb.append( " " );
            sb.append( "(max) " + extremeUpper );
        }
        
        return new String(sb);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println( toString() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private void printAll()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "name: " + getName() );
        
        sb.append(" extremeLower: " + extremeLowerFlag + " " + extremeLower );
        sb.append(" lower: " + lowerFlag + " " + lower);
        sb.append(" median: "+ medianFlag + " " + median);
        sb.append(" upper: " + upperFlag + " " + upper);
        sb.append(" extremeUpper: " + extremeUpperFlag + extremeUpper);
        System.out.println( sb.toString() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "name: " + getName() );
        sb.append( getStringValue() );
        /*
        sb.append("upper: " + upper);
        sb.append("lower: " + lower);
        sb.append("median: " + median);
        if(max != null)
        {
            sb.append("max: " + max);
        }
        else if(min != null)
        {
            sb.append("min: " + min);
        }
        sb.append("extremeUpper: " + extremeUpper);
        sb.append("extremeLower: " + extremeLower);
        
        if(extremeMax != null)
        {
            sb.append("extremeMax: " + extremeMax);
        }
        else if(extremeMin != null)
        {
            sb.append("extremeMin: " + extremeMin);
        }
         */
        return new String(sb);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static void main(String args[])
    {
        
        NumericState ns1 = new NumericState();
        ns1.setName("mm");
        ns1.setUpper(7.6);
        ns1.setMedian(5);
        ns1.setLower(4.2);
        ns1.printAll();
        System.out.println("-----");
        
        NumericState ns2 = new NumericState();
        ns2.setName("mm");
        ns2.setUpper(8.6);
        ns2.setLower(3.9);
        ns2.printAll();
        System.out.println("-----");
        
        NumericState ns3 = new NumericState();
        ns3.setName("mm");
        ns3.setExtremeUpper( -12.6);
        ns3.printAll();
        System.out.println("-----");
        
        NumericState ns4 = new NumericState();
        ns4.setName("mm");
        ns4.setExtremeLower( -18.6 );
        ns4.printAll();
        System.out.println("-----");



        // if(ns2.fallsWithinNormalRange(ns1))
        if(ns2.fallsWithinExtremeRange(ns1))
        {
            System.out.println("ns2 falls within ns1");
        }
        // if(ns1.fallsWithinNormalRange(ns2))
        if(ns1.fallsWithinExtremeRange(ns2))
        {
            System.out.println("ns1 falls within ns2");
        }
        System.out.println(ns1.getStringValue());
    }
}
