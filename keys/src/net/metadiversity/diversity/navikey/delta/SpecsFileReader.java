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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import net.metadiversity.diversity.navikey.util.DeltaFileUtils;
import net.metadiversity.diversity.navigator.db.delta_editor.Dependencies;

/**
 * <pre>
 * This class reads parts of a DELTA specs file as described in
 * Definition of the Delta Format
 * 14 March, 1995
 * Dallwitz & Paine
 *
 * Currently deals with:
 *   *CHARACTER TYPES
 *
 * Still need to parse:
 *  *IMPLICIT VALUES
 *  *DEPENDENT CHARACTERS
 *
 * The following can be calculated from the Chars and Items files:
 *  *NUMBER OF CHARACTERS
 *  *MAXIMUM NUMBER OF STATES
 *  *MAXIMUM NUMBER OF ITEMS
 *  *NUMBERS OF STATES
 *
 * Not sure what to do with:
 *  *SHOW <name>-- specifications. #TIME#DATE
 *  *SPECIAL STORAGE
 *  *MANDATORY CHARACTERS  (Not needed for reading purposes)
 * </pre>
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 * @author Ben Richardson
 */

public class SpecsFileReader
{
    StringBuffer    itemCharacterTypeLines;
    StringBuffer    itemCharacterDependencyLines;
    StringBuffer    itemImpliciteValuesLines;
    String          itemImpliciteValues = null;
    //
    java.util.Vector<ItemCharacterType>        itemCharacterTypes;
    java.util.Vector<Dependencies>             dependenciesVector;
     
    //                                *23456789012345678901234567890
    String CHARACTER_TYPES          = "CHARACTER TYPES";
    String DEPENDENT_CHARACTERS     = "DEPENDENT CHARACTERS";
    String MANDATORY                = "MANDATORY";    
    // 29.10.2007
    //
    // IMPLICIT VALUES 49,1 71,2 169,1 174,1 176,1 180,2
    //
    String IMPLICIT_VALUES = "IMPLICIT VALUES";
    
    
    public SpecsFileReader()
    {
        itemCharacterDependencyLines = new StringBuffer();
        itemCharacterTypeLines       = new StringBuffer();
        itemImpliciteValuesLines     = new StringBuffer();
        itemCharacterTypes           = new Vector<ItemCharacterType>();        
    }
    public Vector getItemCharacterTypes()
    {
        return itemCharacterTypes;
    }
    public Vector<Dependencies> getDependenciesVector()
    {
        return dependenciesVector;
    }
    public String getImplicideValues()
    {
        return itemImpliciteValues;
    }
    
    /**
     * Read in the file
     */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void read( java.lang.String fileName )
    {
        java.io.BufferedReader bufferdReader = null;
        try
        {
            System.out.println( "SpecsFileReader(), read UTF-8 file: " + fileName );
            // bufferdReader = new java.io.BufferedReader( new java.io.FileReader( fileName ) );
            bufferdReader = new java.io.BufferedReader( new java.io.InputStreamReader( new java.io.FileInputStream( fileName ), "UTF-8" ) );            

            read( bufferdReader );
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
    public void read( java.io.BufferedReader specsReader )
    {
        read( specsReader, false, false );
    }
    void read( java.io.BufferedReader specsReader, boolean removeSpecsCommentFlag, boolean removeSpecsStatesCommentFlag )
    {
        //read in the file a line at a time
        String line = "";
        
        int count = 0;
        
        boolean newSectionFlag;
        boolean itemCharacterTypesFlag      = false;
        boolean itemCharacterDependencyFlag = false;
        boolean itemImpliciteValuesFlag     = false;
        
        while( line != null )
        {
            count++;
            try
            {
                line = specsReader.readLine();
                if( line != null )
                {
                    line = line.trim();
                    if( line.indexOf( "*" ) == 0 )
                    {
                        newSectionFlag = true;
                        itemCharacterTypesFlag = false;
                    }
                    else
                    {
                        newSectionFlag = false;
                    }
                    // Descriptor definition type lines look like this:
                    //    *CHARACTER TYPES 1,TE 4,RN 6,RN
                    //    25,RN 27,RN 28,RN 29,RN
                    
                    // Check for itemCharacter type header
                    if( DeltaFileUtils.containsDirective( line, CHARACTER_TYPES ) )  
                    {
                        itemCharacterTypesFlag      = true;
                        itemCharacterDependencyFlag = false;
                        itemImpliciteValuesFlag     = false;
                    }
                    // Descriptor definition  dependency lines look like this:
                    //   *DEPENDENT CHARACTERS 5,2:6 20,2:21-22                    
                    // Check for itemCharacter dependency header
                    if( DeltaFileUtils.containsDirective( line, DEPENDENT_CHARACTERS ) )
                    {
                        itemCharacterDependencyFlag = true;
                        itemCharacterTypesFlag      = false;
                        itemImpliciteValuesFlag     = false;
                    }
                    // Descriptor definition  dependency lines look like this:
                    // *IMPLICIT VALUES 49,1 71,2 169,1 174,1 176,1 180,2
                    // Check for IMPLICIT VALUES header
                    //
                    if( DeltaFileUtils.containsDirective( line, IMPLICIT_VALUES ) )
                    {
                        itemImpliciteValuesFlag      = true;
                        itemCharacterDependencyFlag = false;
                        itemCharacterTypesFlag      = false;
                    }
                    else
                    {
                        itemCharacterDependencyFlag = false;
                    }
                       
                        
                    if( DeltaFileUtils.containsDirective( line, MANDATORY ) )
                    {
                        itemCharacterDependencyFlag = false;
                        itemCharacterTypesFlag      = false;
                        itemImpliciteValuesFlag     = false;
                    }
                    if( itemCharacterTypesFlag )
                    {
                        itemCharacterTypeLines.append( " " ).append( line );
                    }
                    if( itemCharacterDependencyFlag )
                    {
                        itemCharacterDependencyLines.append( " " ).append( line );
                    }                    
                    if( itemImpliciteValuesFlag  )
                    {
                        itemImpliciteValuesLines.append( " " ).append( line );
                    }                    
                }
            } 
            catch( IOException e )
            {
                //System.out.println("Error reading file " + fileName + "at line " + count);
                System.out.println(e);
            }
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void makeItemCharacterTypes()
    {        
        if( itemCharacterTypeLines == null )
            return;
        if( itemCharacterTypeLines.length() < CHARACTER_TYPES.length() + 3 )
            return;
       
        String allLines = itemCharacterTypeLines.substring( CHARACTER_TYPES.length() + 3 );
        
        // split the rest into itemCharacterId,itemCharacterType pairs
        
        StringTokenizer stAll = new StringTokenizer(allLines);
        while(stAll.hasMoreTokens())
        {
            String str = stAll.nextToken();
            //split up each pair into id, name values
            StringTokenizer st = new StringTokenizer(str, ",");
            String idStr = st.nextToken();
            String type = st.nextToken();
            //System.out.println("Id: " + idStr + " type: " + type);
            //hendle ranges
            StringTokenizer st2 = new StringTokenizer(idStr, "-");
            if(st2.countTokens() == 2)
            {
                Integer start = new Integer(st2.nextToken());
                Integer end = new Integer(st2.nextToken());
                System.out.println("Start:"+start+" End:"+end +" Type:"+type);
                for(int j=start.intValue(); j<=end.intValue(); j++)
                {
                    itemCharacterTypes.addElement( new ItemCharacterType( new Integer(j), type) );
                }
            }
            else
            {
                Integer id = new Integer(st2.nextToken());
                itemCharacterTypes.addElement( new ItemCharacterType(id, type));
            }
        }
    }
    //--------------------------------------------------------------------------
    /*
     *   http://delta-intkey.com/www/standard.htm
     *
     *   Examples
     *
     * If the directive
     *
     *   *DEPENDENT CHARACTERS 4,2:16 9,1:20 10,1/3:12–13:20:30–33
     *
     *   is in force, the following combinations of attributes in an item are permitted.
     *
     *   4,2 9,1 10,3 12,– 13,– 16,– 20,– 30,– 31,– 32,–
     *
     *   4,2 9,1 10,3 (equivalent to the above)
     *
     *   4,1 16,1
     *
     *   10,1/2 12,1/–
     *
     *   10,1/2 12,1
     *
     *   9,2 10,2 20,1
     *
     *   The following combinations of attributes in an item are not permitted.
     *
     *   4,2 16,1
     *
     *   16,1
     *
     *   9,2 10,3 20,1
     */
    //
    // Format:
    //
    // character,state{[-]|[/]state]}...
    // :
    // ...
    //--------------------------------------------------------------------------
    public void makeItemCharacterDependencies()
    {
        
        boolean debug = false;

        dependenciesVector = new java.util.Vector<Dependencies>();        
        //chop off the "*DEPENDENT CHARACTERS" text        
        if( itemCharacterDependencyLines.length() < DEPENDENT_CHARACTERS.length() + 3 )
        {
            return;
        }        
        String allLines = itemCharacterDependencyLines.substring( DEPENDENT_CHARACTERS.length() + 3 );
        if( debug ) System.out.println( "allLines: " + allLines );
        
        // split the rest into itemCharacterId,itemCharacterDependency pairs
        
        Vector characterStates = new Vector();
                
        StringTokenizer stAll = new StringTokenizer( allLines );

        while( stAll.hasMoreTokens() ) 
        {
            String str = stAll.nextToken();
            if( debug ) System.out.println( "Unparsed String: " + str);
            
            // split up each pair into id and states
            
            StringTokenizer st = new StringTokenizer( str, "," );
            
            String idStr    = st.nextToken();
            String states   = st.nextToken();

            
            // followed by ":"
            
            int splitPos = states.indexOf( ':' );
            
            String deps = states.substring( ++splitPos );
            states = states.substring( 0, --splitPos );

            if( debug) 
            {    
                System.out.println( idStr );
                System.out.println( states );
                System.out.println( deps );
            }
            // split states:  like:  1/3-5
            DeltaRange statesRange = new DeltaRange( states , '/', '-' );
            DeltaRange depRange   = new DeltaRange( deps , ':', '-' );
            
            try 
            {
                        
                for( int s = 0; s < statesRange.v.size(); s++ )
                {
                    for( int d = 0; d < depRange.v.size(); d++ )
                    {
                        
                        if( debug ) System.out.println( "DEPENDENT " + idStr + " " + statesRange.v.get( s ) + " " + depRange.v.get( d ) );
                        
                        int cid = Integer.parseInt( idStr ); 
                        int cs  =  ((Integer) statesRange.getVector().get( s ) ).intValue(); 
                        int dep =  ((Integer) depRange.getVector().get( d ) ).intValue();
                                
                        dependenciesVector.add( new Dependencies( cid, cs, dep ) );
                    }                        
                }    
            } 
            catch (NumberFormatException ex) 
            {
                ex.printStackTrace();
            }                
/*            
            
            StringTokenizer st = new StringTokenizer( str, "," );
            String idStr = st.nextToken();
            String type = st.nextToken();
            System.out.println("Id: " + idStr + " type: " + type);
            
            Integer id = new Integer( idStr );
            itemCharacterTypes.addElement( new ItemCharacterType( id, type ) );
*/            
        }
    }
    
    class DeltaRange
    {
        java.util.Vector<Integer> v;
        String delim;
        String range;
        
        boolean debug = false;
        
        public DeltaRange( String definition, char delimiterCharacter, char rangeCharacter )
        {
            this.delim  = String.valueOf( delimiterCharacter );
            this.range  = String.valueOf( rangeCharacter );
            
            String p = delim + range;
            
            StringTokenizer split = new StringTokenizer( definition, p, true );
            
            v = new java.util.Vector<Integer>();
            
            boolean firstLoop = true;
            String  lastToken = null;
            String  delimiter = null;
            String  token     = null;
            
            while( split.hasMoreElements() )
            {
                lastToken = token;
                token = split.nextToken();
                if( ! firstLoop )
                {
                    if( debug ) System.out.println( definition + " " + delimiter + " " + token + " " + lastToken );
                    
                    if( delimiter.compareTo( range ) == 0 )
                    {
                        for( int i = Integer.parseInt( lastToken ); i <= Integer.parseInt( token ); i++ )
                        {
                            if( debug) System.out.println( "add: " + i );
                            v.add( new Integer( i ) );
                        }
                    }
                    else
                    {
                        if( debug) System.out.println( "add: " + Integer.parseInt( token ) );
                        v.add( new Integer( Integer.parseInt( token ) ) );
                    }
                }
                else
                {
                    // always add the first value.
                    
                    if( debug) System.out.println( "add: " + Integer.parseInt( token ) );
                    v.add( new Integer( Integer.parseInt( token ) ) );
                    
                    // if the next element is a range, we nead this value, becouse "token" already added.
                    token = String.valueOf( Integer.parseInt( token ) + 1 );
                    firstLoop = false;
                }               
                if( split.hasMoreElements() )
                {
                    delimiter = split.nextToken();
                }                                        
            }
            
        }        
        public boolean inRange( int i )
        {
            return v.contains( new java.lang.Integer( i ) );
        }
        public java.util.Vector getVector()
        {
            return v;
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void makeImpliciteValues()
    {   
        itemImpliciteValues = null;
        if( itemImpliciteValuesLines == null )
            return;
        if( itemImpliciteValuesLines.length() < IMPLICIT_VALUES.length() + 3 )
            return;       
        itemImpliciteValues = itemImpliciteValuesLines.substring( IMPLICIT_VALUES.length() + 3 );              
        return;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println("itemCharacter Types:");
        for(int i = 0; i < itemCharacterTypes.size(); i++)
        {
            ItemCharacterType ict = itemCharacterTypes.elementAt(i);
            ict.print();
        }
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public static void main(String args[])
    {
        if(args.length != 1)
        {
            System.out.println("Usage: SpecsFileReader <specs file name>");
            System.exit(0);
        }
        SpecsFileReader sf = new SpecsFileReader();
        sf.read(args[0]);
        sf.makeItemCharacterTypes();
        sf.makeItemCharacterDependencies();
        sf.print();
    }
}
