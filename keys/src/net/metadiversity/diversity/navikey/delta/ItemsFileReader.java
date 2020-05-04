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
package net.metadiversity.diversity.navikey.delta;

import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.HashMap;

import net.metadiversity.diversity.navikey.bo.Attribute;
import net.metadiversity.diversity.navikey.bo.Item;
import net.metadiversity.diversity.navikey.bo.ItemCharacter;
import net.metadiversity.diversity.navikey.bo.MultiState;
import net.metadiversity.diversity.navikey.bo.MultiStateAttribute;
import net.metadiversity.diversity.navikey.bo.NumericAttribute;
import net.metadiversity.diversity.navikey.bo.NumericState;
import net.metadiversity.diversity.navikey.bo.ResourceAttribute;
import net.metadiversity.diversity.navikey.bo.State;
import net.metadiversity.diversity.navikey.bo.TextAttribute;
import net.metadiversity.diversity.navikey.bo.TextState;
import net.metadiversity.diversity.navikey.resource.ImageResource;
import net.metadiversity.diversity.navikey.util.NewStringTokenizer;
import net.metadiversity.diversity.navikey.util.DeltaDataString;
import net.metadiversity.diversity.navikey.util.DeltaFileUtils;
/**
 * <pre>
 * This class reads an items file as described in
 * Definition of the Delta Format
 * 14 March, 1995
 * Dallwitz & Paine
 *
 * </pre>
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 * @author Ben Richardson
 */

public class ItemsFileReader
{
    private Vector<Item> items = null;
    
    private HashMap<String,String> implicide = null; 
    Object [] implicideCharacters = null;
    
    private DeltaData dd;
    
    public ItemsFileReader()
    {
        items = new Vector<Item>();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getItems()
    {
        return items;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        for( int i = 0; i < items.size(); i++ )
        {
            System.out.println( "--------------" );
            Item item = items.elementAt(i);
            item.print();
        }
    }
    /** Set a reference to the ItemCharacter list */
/*    
    public void setItemCharacters( Vector v )
    {
        itemCharacters = v;
    }
    */
    /**
     * Read in the file store the item lines a a Vector of Vectors.
     * After that, parse these vectors of strings into Item objects
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.lang.String fileName, DeltaData dd, String implicideValues )
    {
        java.io.BufferedReader bufferdReader = null;
        try
        {   
            System.out.println( "ItemsFileReader(), read UTF-8 file: " + fileName );
            // bufferdReader = new java.io.BufferedReader( new java.io.FileReader( fileName ) );
            bufferdReader = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( fileName ), "UTF-8" ) );            
            read( bufferdReader, false, false, dd, implicideValues );
            bufferdReader.close();
        }
        catch( java.io.IOException ex )
        {
            System.out.println( fileName + " not found" );
            return;
        }        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.io.BufferedReader itemsReader, boolean removeItemsCommentFlag, boolean removeItemsStatesCommentFlag, DeltaData dd, String implicideValues )
    {
        this.dd = dd;
        
        Vector<StringBuffer> rawItems = new Vector<StringBuffer>();
        String line = "";
        int count = 0;
        int itemCount = 0;
        boolean beginItems = false;
        
        //read in the file a line at a time
        while( line != null )
        {
            count++;
            try
            {                
                // read a line
                line = itemsReader.readLine();                
                //the end of file will return a null line
                if( line != null )
                {
                            
                    // Remove formating characters Like: \i{}, \i0{}, ...

                    /*        
                    \i      Start italics   \i{}Agrostis\i0{}
                    \i0     Stop italics
                    \b      Start bold      \b{}Habit\b0{}
                    \b0     Stop bold
                    \sub    Start subscript C\sub{}4\nosupersub{}
                    \nosupersub Stop super- or subscript
                    \fsN    Font size       \fs16{}eight point
                    \plain  Set default font attributes
                    \endash En dash         4\endash{}10mm
                    \lquote                 \lquote{}normal\rquote{}
                    \rquote Right quote
                    */
                
                    // Start with \, then not {, end with {}                           
                    
                    line = line.replaceAll( "\\\\[^\\{]*\\{\\}", "" ); 
                    
                    line = line.trim();
                    if( beginItems )
                    {  //if we're past the "*ITEMS DESCRIPTION" line
                        if( isNewItem( line ) )
                        {  //start of a new item ( like "# olivacea <Rick>/"
                            itemCount++;
/*
                            // if in configuration "remove comment" is defined, remove comments
                            if( removeItemsCommentFlag )
                            {
                                DeltaDataString dds = new DeltaDataString( new StringBuffer( line ), removeItemsCommentFlag, removeItemsStatesCommentFlag );
                                line = dds.getString();
                            }
*/
                            //make a new StringBuffer to the the attribute lines into for later processing
                            
                            rawItems.addElement( new StringBuffer( line ) );
                        }
                        else
                        {  //add this line to the current item
                            if( rawItems.size() != 0 )
                            {
                                rawItems.elementAt(itemCount - 1).append( " " + line );
                            }
                        }
                    }
                    if ( DeltaFileUtils.containsDirective(line, "ITEM DESCRIPTIONS"))
                    {
                        beginItems = true;
                    }
                }
            }
            catch( IOException e )
            {
                //System.out.println( "Error reading file " + fileName + "at line " + count );
                System.out.println( e );
            }
        }
        //
        // 29.10.2007 *IMPLICIT VALUES
        //
        // if not defined: add the IMPLICIT VALUES
        
        // String implicideValues = "49,1 71,2 169,1 174,1 176,1 180,2";
        
        if( implicideValues != null )
        {
            StringTokenizer st = new StringTokenizer( implicideValues );
            implicide = new HashMap<String,String>(); 
            // Add implicide values
            while( st.hasMoreElements() )
            {
                String e = (String) st.nextElement();
                
                String c = "";
                
                int index = e.indexOf( "," );
                if( index > 0 )
                {
                    c = e.substring( 0, index );
                }
                implicide.put( c, e );   
                
            }    
            implicideCharacters = implicide.keySet().toArray();            
        }
        
        
        //create the item objects
        for( int i = 0; i < rawItems.size(); i++ )
        { 
            StringBuffer data;
                                
            data = rawItems.elementAt(i);
            // Add implicide values
            if( implicide != null && implicideCharacters != null )
            {
                int size = implicideCharacters.length;
                for( int ii = 0; ii < size; ii++ )
                {
                    String c = (String) implicideCharacters[ ii ];
                    if( data.indexOf( " " + c + "," ) < 0 )
                    {    
                        data.append( " " + implicide.get( c ) );
                    }
                }                    
            }
            
            // if in configuration "remove comment" is defined, remove comments
            if( removeItemsCommentFlag || removeItemsStatesCommentFlag )
            {
                DeltaDataString dds = new DeltaDataString( data, removeItemsCommentFlag, removeItemsStatesCommentFlag );
                data = new StringBuffer( dds.getString() );
            }
            Item item = makeItem( data, i );
            items.addElement( item );
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /** convert a DELTA representation of an Item to an Item object */
    private Item makeItem( StringBuffer sb, int id )
    {
        Item item = new Item();                
        String all = sb.toString().trim();        
        //get location of name
        String name = all.substring( 0, indexOutOfComment( all, "/ " ) );        
        //get rid of beginning '#' symbol
        if( name.charAt( 0 ) == '#' )
        {
            name = name.substring( 1 ).trim();
        }
        else
        {
            System.out.println( "beginning '#' symbol missing" );
        }        
        item.setName( name );
        item.setId( new Integer( id ) );

        String attributes = all.substring( all.indexOf( "/ " ) + 2 );
        
        Vector attribs = splitOnSpaces( attributes );
        
        for( int i = 0; i < attribs.size(); i++ )
        {
            String str = ( String )attribs.elementAt( i );
            
            // is a range defined ?
            
            str = str.trim();
            
            // if a range is specified: 71-72,2
            
            int p1 = str.indexOf( '-' );
            int p2 = str.indexOf( ',' );
            if( p1 > 0 && p1 < p2 )
            {
                // remove comments
                DeltaDataString dds = new DeltaDataString( new StringBuffer( str ), true, true );
                str = dds.getString();
                
                p1 = str.indexOf( '-' );
                p2 = str.indexOf( ',' );
                
                if( p1 > 0 && p1 < p2 )
                {    
                    // System.out.println( str );
                    int minIndex = 0;
                    int maxIndex = 0;

                    try
                    {    
                        minIndex = Integer.parseInt( str.substring( 0, p1  ) );
                        maxIndex = Integer.parseInt( str.substring( p1 + 1, p2 ) );
                    }
                    catch( java.lang.NumberFormatException ex )
                    {
                        System.out.println( str );
                    }    
                    for( int j = minIndex; j <= maxIndex; j++ )
                    {    

                        String attrib = Integer.toString( j ).concat( str.substring( p2 ) );
                        Attribute a = makeAttribute( attrib );
                        if( a != null )
                        {
                            item.addAttribute( a );
                        }                
                    }
                }
            }    
            else
            {    
                Attribute a = makeAttribute( str.trim()  );
                if( a != null )
                {
                    item.addAttribute( a );
                }
            }
        }
        return item;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    private int indexOutOfComment( String s, String target )
    {
        int attempt = s.indexOf( target );
        int lbrace = 0;
        int rbrace = 0;
        for( int i=0; i<attempt; i++ )
        {
            if( s.charAt( i ) == '<' )
                lbrace++;
            else if( s.charAt( i ) == '>' )
                rbrace++;
        }
        while( attempt >= 0 && lbrace != rbrace )
        {
            s = s.substring( attempt );
            attempt = s.indexOf( target );
            for( int i=0; i<attempt; i++ )
            {
                if( s.charAt( i ) == '<' )
                    lbrace++;
                else if( s.charAt( i ) == '>' )
                    rbrace++;
            }
        }
        return attempt;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private Vector splitOnSpaces( String str )
    {
        // Didi: attributes must end with a BLANK, otherwise "splitOnSpaces( attributes )" don't work well
        str = str.trim() + " ";
        Vector<String> attribs = new Vector<String>();
        int lbrace = 0;
        int rbrace = 0;
        StringBuffer sb = new StringBuffer();
        
        //loop through each character in string
        for( int i=0; i < str.length(); i++ )
        {            
            //if out of comment and at space, add string to vector, reset string
            if( ( str.charAt( i ) == ' ' || 
                  str.charAt( i ) == '\t' ) &&
                  lbrace == rbrace &&
                  sb.length() > 1 
              )
            {
                attribs.addElement( new String( sb ) );
                //System.out.println( new String( sb ) );
                lbrace = 0;
                rbrace = 0;
                sb = new StringBuffer();
            }                        
            else //otherwise check for comment status and add character to string
            {
                if( str.charAt( i ) == '<' )
                {
                    lbrace++;
                }
                else if( str.charAt( i ) == '>' )
                {
                    rbrace++;
                }
                sb.append( str.charAt( i ) );
            }
        }        
        return attribs;
    }    
    /**  Attributes strings look like this:
     *    27,4-6-8
     *    40,3
     *    41<Brazil, Venezuela>
     *	13<A comment>,3<another comment, and another>/4
     */
    private Attribute makeAttribute( String str )
    {
        //a little defensive programming:
        if( str == null )
        {
            return null;
        }
        else if( str.length() == 0 )
        {
            return null;
        }
        
        // We need to find the ID of the ItemCharacter of this
        // Attribute.
        // First locate the divider between Attribute id and value
        // this can be either a '<' or a ','
        
        String itemCharacterIdStr = "";
        String valueStr = "";
        
        int divider;
        for( divider = 0; divider < str.length(); divider++ )
        {
            if( str.charAt( divider ) == ',' )
            {
                itemCharacterIdStr = str.substring( 0, divider ).trim();
                valueStr = str.substring( divider + 1, str.length() );
                break;
            }
            else if( str.charAt( divider ) == '<' )
            {
                itemCharacterIdStr = str.substring( 0, divider ).trim();
                valueStr = str.substring( divider );
                break;
            }
        }
        
        //convert the ID string to an Integer
        Integer itemCharacterId = new Integer( 0 );
        try
        {
            itemCharacterId = new Integer( itemCharacterIdStr );
        }
        catch( NumberFormatException e )
        {
            System.err.println( "can't seem to make an integer out of \"" + itemCharacterIdStr + "\"" );
            System.err.println( "String is " + str +"\n " + valueStr );
        }        
        valueStr = valueStr.trim();        
        ItemCharacter ic = new ItemCharacter();
        
        //look up the ItemCharacter
    /*for ( int i = 0; i < itemCharacters.size(); i++ ) {
      ItemCharacter icRef = ( ItemCharacter )itemCharacters.elementAt( i );
      if( icRef.getId().equals( itemCharacterId ) ) {
        //ic now points to the right ItemCharacter
        ic = icRef;
      }
      }*/
        ic = (ItemCharacter) dd.getItemCharacters().get( itemCharacterId );
        
        // Instantiate  the Attribute.  Have to instantiate NumericAttribute
        // since Attribute is abstract.
        Attribute a = new NumericAttribute();
        
        if( ic.getType().equals( "UM" ) || ic.getType().equals( "OM" ) )
        {
            a = makeMultiStateAttribute( valueStr, ic );
        }
        else if( ic.getType().equals( "RN" ) || ic.getType().equals( "IN" ) )
        {
            a = makeNumericAttribute( valueStr );
        }
        else if( ic.getType().equals( "TE" ) )
        {
            a = makeTextAttribute( valueStr );
        }
        if( a != null )
        {    
            a.setItemCharacter( ic );
        }    
        return a;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private Attribute makeTextAttribute( String valueStr )
    {
        Attribute ta1 = makeResourceAttribute( valueStr );
        if( ta1 == null )
        {
            TextAttribute ta = new TextAttribute();
            TextState ts = new TextState();
            ts.setValue( valueStr );
            ta.setTextState( ts );
            return ta;
        }
        return ta1;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private Attribute makeResourceAttribute( String valueStr )
    {
        String s = valueStr;
        String s2 = getPrecedingComment( valueStr );
        if( s2 != null  &&
        ! ( s2.indexOf( ".jpg" ) > 0 ||
        s2.indexOf( ".gif" ) > 0 ||
        s2.indexOf( ".jpeg" )>0 ) )
        {
            s = valueStr.substring( s2.length() );
        }
        if( s == null || s.length() < 1 ||
        ! ( s.indexOf( ".jpg" ) > 0 ||
        s.indexOf( ".gif" ) > 0 ||
        s.indexOf( ".jpeg" )>0 ) )
        {
            return null;
        }
        if( s.charAt( 0 ) == ',' )
            s = s.substring( 1 );
        ResourceAttribute ra = new ResourceAttribute();
        StringTokenizer st = new StringTokenizer( s, " " );
        while( st.hasMoreTokens() )
        {
            String name = st.nextToken();
            if( name.charAt( 0 ) == '<' )
                name = name.substring( 1 );
            if( name.charAt( name.length()-1 ) == '>' )
                name = name.substring( 0, name.length()-1 );
            URL url = null;
            ImageResource ir;
            try
            {
// ???
// DIDI                url = new URL( name );
// ???
//                url = new URL( "http://geo.cbs.umn.edu/treekey/images/" + name );
                
                url = new URL( name );
                ir = new ImageResource( url );
            }
            catch( Exception e )
            {
                ir = new ImageResource( name );
                //System.out.println( "Unable to form image url." + e );
            }
//            ra.addResource( ir );
        }
        return ra;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private String getPrecedingComment( String str )
    {
        if( str.charAt( 0 ) != '<' )
        {
            return null;
        }        
        int lbrace = 0;
        int rbrace = 0;
        StringBuffer sb = new StringBuffer();
        String rv = null;        
        //loop through each character in string
        for( int i = 0; i < str.length(); i++ )
        {            
            //if out of comment and at comma, add string to vector, reset string
            if( str.charAt( i ) == ',' &&
            lbrace == rbrace )
            {
                rv = sb.toString();
                break;
            }            
            //otherwise check for comment status and add character to string
            else
            {
                if( str.charAt( i ) == '<' )
                {
                    lbrace++;
                }
                else if( str.charAt( i ) == '>' )
                {
                    rbrace++;
                }
            }
            sb.append( str.charAt( i ) );
        }
        return rv;
    }       
    //numeric attributes may look like:
    //	<comment>,4
    //	( 1- )2-3-4( -5 )<comment>/2-3-4<...>/...
    //	3
    private Attribute makeNumericAttribute( String valueStr )
    {
        if( valueStr.charAt( 0 ) == 'U' )
        {
            return null;
        }
        NumericAttribute na = new NumericAttribute();
        String firstComment = getPrecedingComment( valueStr );
        if( firstComment != null )
        {
            //remove first comment plus comma from string
            valueStr = valueStr.substring( firstComment.length()+1 );
            na.setComment( firstComment );
        }        
        //SPLIT ON / ( OR's ) THAT AREN'T IN COMMENTS.
        NewStringTokenizer mwst = new NewStringTokenizer( valueStr, "/", '<', '>' );
        while( mwst.hasMoreTokens() )
        {
            String current = mwst.nextToken();
            
            String[] strings = divideAt( current, '<' );
            String numericStr = strings[0];
            String comment = strings[1];
            
            NumericState ns = makeNumericState( numericStr );
            
            na.addCommentedState( ns, comment );
        }
        return na;
    }    
    /**
     *  Numeric strings look like:
     *  ( 2- )3-5-8( -9 )
     *  representing:
     * extreme lower value = 2,
     * lower value =  3,
     * median = 5,
     * upper = 8,
     * extreme upper = 9
     *
     * any of these can be present or absent
     */    
    private NumericState makeNumericState( String str )
    {
        NumericState ns = new NumericState();
        
        if( str == null )
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        
        //filter out unwanted characters
        for( int i = 0; i < str.length(); i++ )
        {
            char c = str.charAt( i );
            if( c == '(' ||  c == ')' || c == '-' || c == '.' || Character.isDigit( c ) )
            {
                sb.append( c );
            }
        }
        //if nothing is left then get out of here.
        if( sb.length() == 0 )
        {
            return null;
        }
        //create a position marker
        int position = 0;
        
        StringBuffer extremeLower = new StringBuffer();
        //if there's a lower extreme range
        if( sb.charAt( 0 ) == '(' )
        {
            position++; //move ahead
            //keep going until there's a close parantheses
            while( sb.charAt( position ) != ')' && position < sb.length() )
            {
                char c = sb.charAt( position );
                if( Character.isDigit( c ) || c == '.' )
                {
                    extremeLower.append( c );
                }
                position++;
            }
        }
        
        // set the value for extreme lower
        if( extremeLower.length() > 0 )
        {
            Double d = new Double( new String( extremeLower ) );
            ns.setExtremeLower( d.doubleValue() );
        }
        
        //move past the closed paretheses, if it exists
        if( sb.charAt( position ) == ')' )
        {
            position++; //move ahead
        }
        
        //now get lower-median-upper set.
        
        StringBuffer lowerMedianUpper = new StringBuffer();
        
        while( position < sb.length() && sb.charAt( position ) != '(' )
        {
            lowerMedianUpper.append( sb.charAt( position ) );
            position++;
        }
        //split into lower, median, upper
        StringTokenizer st = new StringTokenizer( new String( lowerMedianUpper ), "-" );
        int count = 0;
        String typeAbbrev = "";
        Vector<String> values = new Vector<String>();
        while( st.hasMoreTokens() )
        {
            count++;
            values.addElement( st.nextToken() );
        }
        if( values.size() == 1 )
        {
            Double d = new Double( values.elementAt(0) );
            ns.setMedian( d.doubleValue() );
        }
        else if( values.size() == 2 )
        {
            Double d = new Double( values.elementAt(0) );
            ns.setLower( d.doubleValue() );
            
            d = new Double( values.elementAt(1) );
            ns.setUpper( d.doubleValue() );
        }
        else if( values.size() == 3 )
        {
            
            Double d = new Double( values.elementAt(0) );
            ns.setLower( d.doubleValue() );
            
            d = new Double( values.elementAt(1) );
            ns.setMedian( d.doubleValue() );
            
            d = new Double( values.elementAt(2) );
            ns.setUpper( d.doubleValue() );
        }
        
        StringBuffer extremeUpper = new StringBuffer();
        while( position < sb.length() && sb.charAt( position ) != ')' )
        {
            char c = sb.charAt( position );
            if( Character.isDigit( c ) || c == '.' )
            {
                extremeUpper.append( c );
            }
            position++;
        }
        // set the value for extreme upper
        if( extremeUpper.length() > 0 )
        {
            Double d = new Double( new String( extremeUpper ) );
            ns.setExtremeUpper( d.doubleValue() );
        }
        return ns;
        
    }    
    /**
     *   deal with things like:
     *   8<some with 5>
     */
    public Attribute makeMultiStateAttribute( String valueStr, ItemCharacter ic )
    {
        MultiStateAttribute msa = new MultiStateAttribute();
        
        String firstComment = getPrecedingComment( valueStr );
        if( firstComment != null )
        {
            //remove first comment plus comma from string
            valueStr = valueStr.substring( firstComment.length()+1 );
            msa.setComment( firstComment );
        }
        if( valueStr.charAt( 0 ) == 'U' )
            return null;
        
        //SPLIT ON / ( OR's ) THAT AREN'T IN COMMENTS.
        NewStringTokenizer mwst = new NewStringTokenizer( valueStr, "/", '<', '>' );
        while( mwst.hasMoreTokens() )
        {
            String current = mwst.nextToken();
            
            String[] strings = divideAt( current, '<' );
            String numericStr = strings[0];
            String comment = strings[1];
            
            StringTokenizer totokens = new StringTokenizer( numericStr, "-" );
            if( totokens.countTokens() == 1 )
            {
                totokens = new StringTokenizer( numericStr, "&" );
            }
            while( totokens.hasMoreTokens() )
            {
                String numericStrings = totokens.nextToken();
                if( numericStrings.charAt( 0 ) == 'U' )
                    return null;
                MultiState thisState = null;
                Integer stateId;
                try
                {
                    stateId = new Integer( numericStrings );
                    
          /* The ItemCharacter class should really do the following work.
           * that would probably mean making ItemCharacter virtual though.
           * Maybe next time.
           */
                    Vector states = ic.getStates();
                    for( int i = 0; i < states.size(); i++ )
                    {
                        MultiState s = ( MultiState )states.elementAt( i );
                        if( ( ( Integer )s.getId() ).intValue() == stateId.intValue() )
                        {
                            thisState = s;
                        }
                    }
                }
                catch( NumberFormatException e )
                {
                    System.err.println( "\"" + numericStrings + "\" is not a number " +ic );
                }
                if( thisState != null )
                {
                    msa.addCommentedState( ( State )thisState, comment );
                }
            }
        }
        return msa;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private boolean isNewItem( String line )
    {
        if( line == null )
        {
            return false;
        }
        else if( line.length() == 0 )
        {
            return false;
        }
        else if( line.charAt( 0 ) == '#' )
        { //start of a new item
            return true;
        }
        else
        {
            return false;
        }
    }       
    /** Return an array of two strings are the result of
     *  dividing String str at the first occurence of 'divideChar'.
     */
    private String[] divideAt( String str, char divideChar )
    {
        int divider;
        String[] strings  = new String[2];
        
        strings[0] = str;
        strings[1] = null;
        
        for( divider = 0; divider < str.length(); divider++ )
        {
            if( str.charAt( divider ) == divideChar )
            {
                strings[0] = str.substring( 0, divider );
                strings[1] = str.substring( divider );
                break;
            }
        }
        return strings;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public static void main( String args[] )
    {
        boolean removeItemsCommentFlag = false;
        boolean removeItemsStatesCommentFlag = false;
         
        DeltaData dd = new DeltaData();
        
        if( args.length != 3 )
        {
            System.out.println( "Usage: ItemsFile <specs file name> <chars file name> <items file name>" );
            System.exit( 0 );
        }
        
        SpecsFileReader sfr = new SpecsFileReader();
        System.out.println( "reading specs file..." );
        
        sfr.read( args[0] );
        sfr.makeItemCharacterTypes();
        System.out.println( "done." );
        
        CharsFileReader cfr = new CharsFileReader();
        cfr.setItemCharacterTypes( sfr.getItemCharacterTypes() );
        System.out.println( "reading chars file..." );
        cfr.read( args[1], removeItemsCommentFlag, removeItemsStatesCommentFlag );
        
        ItemsFileReader ifr = new ItemsFileReader();
        // ??? ifr.setItemCharacters( cfr.getItemCharacters() );
        System.out.println( "reading items file..." );
        ifr.read( args[2], dd, null );
        System.out.println( "done." );
        //    ifr.print();
    }
}
