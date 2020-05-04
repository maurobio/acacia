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
import java.util.Vector;

import net.metadiversity.diversity.navikey.bo.ItemCharacter;
import net.metadiversity.diversity.navikey.bo.MultiState;
import net.metadiversity.diversity.navikey.bo.NumericState;
import net.metadiversity.diversity.navikey.bo.TextState;
import net.metadiversity.diversity.navikey.util.NewStringTokenizer;
import net.metadiversity.diversity.navikey.util.DeltaDataString;
import net.metadiversity.diversity.navikey.util.DeltaFileUtils;


/**
 * <pre>
 * This class reads a DELTA chars file as described in
 * Definition of the Delta Format
 * 14 March, 1995
 * Dallwitz & Paine
 * </pre>
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 * @author Ben Richardson
 */

public class CharsFileReader
{
    private java.util.Hashtable<Integer, ItemCharacter> itemCharacters;    
    //this vector comes from parsing a Specs file with the SpecFileReader class.
    private Vector itemCharacterTypes;    

    private boolean setUnitFlag = true; 

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public CharsFileReader()
    {
        itemCharacters = new java.util.Hashtable<Integer, ItemCharacter>();
    }
    //mandatory!
    public void setItemCharacterTypes( Vector v )
    {
        itemCharacterTypes = v;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String lookupItemCharacterTypeAbbrev( Object o )
    {
        if( o instanceof Integer )
        {
            Integer id = ( Integer )o;
            
            String itemCharacterTypeAbbrev = "UM"; //default to Unordered Multitype
            for( int i = 0; i < itemCharacterTypes.size(); i++ )
            {
                ItemCharacterType ictype = ( ItemCharacterType )itemCharacterTypes.elementAt( i );
                if( ictype.getId().intValue() == id.intValue() )
                {
                    itemCharacterTypeAbbrev = ictype.getAbbrev();
                }
            }
            return itemCharacterTypeAbbrev;
        }
        return null;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public java.util.Hashtable getItemCharacters()
    {
        return itemCharacters;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    public void print()
    { 
        /* ??? todo: change to hashtable
        for( int i = 0; i < itemCharacters.size(); i++ )
        {
            ItemCharacter ic = ( ItemCharacter )itemCharacters.elementAt( i );
            ic.print();
        }
         */
    }
    
    /**
     * Read in the file
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.lang.String fileName, boolean removeCharsCommentFlag, boolean removeCharsStatesCommentFlag )
    {
        java.io.BufferedReader bufferdReader = null;
        try
        {
            System.out.println( "CharsFileReader(), read UTF-8 file: " + fileName );
            bufferdReader = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( fileName ), "UTF-8" ) );            
            read( bufferdReader, removeCharsCommentFlag, removeCharsStatesCommentFlag );
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
    public void read( java.io.BufferedReader bufferdReader, boolean removeCharsCommentFlag, boolean removeCharsStatesCommentFlag )
    {
        //read in the file a line at a time
        String line = "";        
        //We'll put the all the descriptions into a Vector
        Vector<StringBuffer> descriptions = new Vector<StringBuffer>();
        int count = 0;
        int descriptionCount = -1;
        
        boolean beginCharacterList = false;
        boolean endCharacterList = false;
        while( line != null )
        {
            count++;
            try
            {
                line = bufferdReader.readLine();
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
                    //look for the beginning of the character list
                    boolean here = DeltaFileUtils.containsDirective( line, "CHARACTER LIST" );
                    if( here )
                    {
                        beginCharacterList = true;
                    }
                    //flag the beginning of a new directive
                    else if( beginCharacterList && line.indexOf( "*" ) == 0 )
                    {
                        endCharacterList = true;
                    }
                    //put all description lines into a Vector for later processing
                    if( beginCharacterList && !endCharacterList )
                    {
                        if( isFeatureDescription( line ) )
                        {
                            //our description counts begin at 0
                            descriptionCount++;
                            descriptions.addElement( new StringBuffer( line.replace( '\t', ' ' ) ) );
                        }
                        else if ( descriptionCount > -1 )
                        {
                            //add the current line to the current description
                            descriptions.elementAt( descriptionCount ).append( " " + line.replace( '\t', ' ' ) );
                            /* // only for debug
                               StringBuffer sb = ( StringBuffer ) descriptions.elementAt( descriptionCount );
                               sb.append( " " + line.replace( '\t', ' ' ) );
                               System.out.println( sb.toString() );
                             */
                        }
                        else
                        {
                            //not a character description
                        }
                    }
                }
            }
            catch( IOException e )
            {
                //System.out.println( "Error reading file " + fileName + "at line " + count );
                System.out.println( e );
            }
        }
        System.out.println( "done reading file" );
        //Make ItemCharacter objects out of the vector of character descriptions
        //and add them to the vector of ItemChars
        
        System.out.println( "Processing " + descriptions.size() + " descriptions" );
        for( int i = 0; i < descriptions.size(); i++ )
        {
            /* // only for debug 
               StringBuffer sb = ( StringBuffer ) descriptions.elementAt( i );
               System.out.println( sb.toString() );
            */
            
            DeltaDataString dds = new DeltaDataString( (StringBuffer) descriptions.elementAt( i ), removeCharsCommentFlag, removeCharsStatesCommentFlag );
            
            ItemCharacter ic = makeItemCharacter( dds.getString() );
            itemCharacters.put( ic.getId(), ic );
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    /*
    public void read( String fileName )
    {
        //try to open the file
        DataInputStream dis;
        try
        {
            dis = new DataInputStream( new FileInputStream( fileName ) );
        }
        catch( Exception e )
        {
            System.out.println( fileName + " not found" );
            return;
        }
        read( dis );                       
    }
    */
    
    /**
     * Tests a line to see if it's the beginning of a feature description.
     * Doesn't check if the feature description is valid or not--on if
     * begins with something like "#1. " or "# 1. "
     */
    private boolean isFeatureDescription( String str )
    {
        // check for minimum length
        if( str.length() < 4 )
        {
            return false;
        }
        //check for numero ( # )
        if( str.charAt( 0 ) != '#' )
        {
            return false;
        }
        //Check for existence of a full stop
        int fullStop = str.indexOf( ". " );
    /*for( int i=1; i<fullStop; i++ ){
      if( !isDigit( str.charAt( i ) ) )
         //if( !( fullStop == 2 || fullStop == 3 ) ){
        return false;
        }*/
        int numberBegin = 1;
        //ignore spaces between numero and number
        while( str.charAt( numberBegin ) == ' ' )
        {
            numberBegin++;
        }
        String numberStr = str.substring( numberBegin, fullStop );
        //see if we have a number or not
        try
        {
            Integer charNumber = new Integer( numberStr );
        }
        catch( Exception e )
        {
            System.out.println( e );
            return false;
        }
        return true;
    }
    
    /**
     *  read in a the text of a Delta character description and
     *  construct a DELTA Character.
     */
    public ItemCharacter makeItemCharacter( String sb )
    {
        ItemCharacter ic = new ItemCharacter();
               
        NewStringTokenizer st = new NewStringTokenizer( sb, "/ " );
        
        int count = 0;
        String typeAbbrev = "";
        while( st.hasMoreTokens() )
        {
            count++;
            String feature = st.nextToken().trim();
            // The first token will be the feature description
            if( count == 1 )
            {
                parseFeatureDescription( feature, ic );
                if( ic.getId() == null )
                {
                    System.out.print( "failed to parse feature description: " + feature );
                    typeAbbrev = "";
                }
                else
                {
                    // look up the ItemCharacterType of this state
                    typeAbbrev = lookupItemCharacterTypeAbbrev( ic.getId() );
                    ic.setType( typeAbbrev );
                }
            }
            else
            {
                //ic have been set by parseFeatureDescription() above.
                if( ic.getType().equals( "UM" ) || ic.getType().equals( "OM" ) )
                {
                    MultiState s = parseMultiState( feature, typeAbbrev );
                    if( s != null )
                    {
                        s.setItemCharacter( ic );
                        ic.addState( s );
                    }
                }
                else if( ic.getType().equals( "IN" ) )
                {
                    NumericState s = new NumericState();
                    s.isInteger( true );
                    s.setName( feature );
                    s.setItemCharacter( ic );
                    ic.addState( s );
                    // setup unit
                    if( setUnitFlag && count == 2 )
                    {
                        if( ic.getFeature().indexOf( '[' ) < 0 )
                        {    
                            ic.setFeature( ic.getFeature() + " [" + feature + "]" );
                        }
                    }    
                }
                else if( ic.getType().equals( "RN" ) )
                {
                    NumericState s = new NumericState();
                    s.isReal( true );
                    s.setName( feature );
                    s.setItemCharacter( ic );
                    ic.addState( s );
                    
                    // setup unit
                    if( setUnitFlag && count == 2 )
                    {
                        if( ic.getFeature().indexOf( '[' ) < 0 )
                        {    
                            ic.setFeature( ic.getFeature() + " [" + feature + "]" );
                        }
                    }    
                    
                }
                else
                { //default to Text state
                    TextState s = new TextState();
                    s.setName( feature );
                    s.setItemCharacter( ic );
                    ic.addState( s );
                }
            }
        }
        return ic;
    }
    
    /**
     * Get the characterid, character name, and comment from the
     * feature description.
     * Returns a ItemCharacter with the id and name attributes set.
     */
    private ItemCharacter parseFeatureDescription( String feature, ItemCharacter ic )
    {
        //Check for position of a full stop
        int fullStop = feature.indexOf( ". " );
        if( fullStop == 0 )
        {
            return ic;
        }
        //skip past numero
        int numberBegin = 1;
        //ignore spaces between numero and number
        while( feature.charAt( numberBegin ) == ' ' )
        {
            numberBegin++;
        }
        String numberStr = ""; // feature.substring( numberBegin, fullStop );
        try
        {
            numberStr = feature.substring( numberBegin, fullStop );
        }
        catch( Exception e )
        {
            System.out.println( "Not a valid character number: " + numberStr );
        }
        //see if we have a number or not
        try
        {
            ic.setId( new Integer( numberStr ) );
        }
        catch( Exception e )
        {
            System.out.println( "Not a valid character number: " + numberStr );
        }
        // look for first description name
        int nameBegin = fullStop + 1;
        int nameEnd = feature.length();
        ic.setFeature( feature.substring( nameBegin, nameEnd ) );
        return ic;
    }
    
    public MultiState parseMultiState( String feature, String typeAbbrev )
    {
        if( feature.length()==0 )
        {
            return null;
        }
        MultiState ms = new MultiState();
        if( typeAbbrev.equals( "UM" ) )
        {
            ms.setUnOrdered();
        }
        else
        {
            ms.setOrdered();
        }
        
        //get the Item ID
        int i = 0;
        //this was added on problems reading 3 digit features
        while( java.lang.Character.isDigit( feature.charAt( i ) ) && i < feature.length() )
        {
            i++;
        }
        String numberStr = feature.substring( 0, i );
        //make sure if we have a number or not
        Integer id;
        try
        {
            id = new Integer( numberStr );
        }
        catch( Exception e )
        {
            id = new Integer( 0 );
            System.out.println( numberStr + "not a valid ID in feature: " + feature );
        }
        ms.setId( id );
        //move past the '.'
        i++;
        //get the State name
        String nameStr = feature.substring( i, feature.length() );
        ms.setName( nameStr );
        return ms;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static void main( String args[] )
    {
        boolean removeCharsCommentFlag = false;
        boolean removeCharsStatesCommentFlag = false;
        
        if( args.length != 2 )
        {
            System.out.println( "Usage: CharsFileReader <chars file name> <specs file name>" );
            System.exit( 0 );
        }
        SpecsFileReader sf = new SpecsFileReader();
        System.out.println( "reading specs file..." );
        sf.read( args[1] );
        sf.makeItemCharacterTypes();
        System.out.println( "done." );
        
        CharsFileReader cf = new CharsFileReader();
        cf.setItemCharacterTypes( sf.getItemCharacterTypes() );
        System.out.println( "reading chars file..." );
        cf.read( args[0], removeCharsCommentFlag, removeCharsStatesCommentFlag );
        cf.print();
    }
}
