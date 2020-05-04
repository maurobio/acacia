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
package net.metadiversity.diversity.navigator.db.delta_editor;

/*
 * @author Dieter Neubacher
 */
import java.util.HashSet;
import java.util.Iterator;

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------        
public class InapplicalibleTest
{
    boolean inapplicalibleFlag;
    HashSet< Inapplicalible > inapplicalible = null;
    StringBuffer sb = null;
    HashSet< Integer > usedCharacterStates = null;

    java.util.Vector< Dependencies > dependenciesVector;
    //----------------------------------------------------------------------
    // Init test for CharacterID cid
    //----------------------------------------------------------------------
    public InapplicalibleTest( java.util.Vector< Dependencies > dependenciesVector, int cid)
    {
        this.dependenciesVector = dependenciesVector;
        
        inapplicalible = new HashSet< Inapplicalible >();
        usedCharacterStates = new HashSet< Integer >();
        inapplicalibleFlag = true;
        testInapplicable( cid );
    }
    // Used by NaviKey
    public InapplicalibleTest( java.util.Vector< Dependencies > dependenciesVector )
    {
        this.dependenciesVector = dependenciesVector;
        usedCharacterStates = new HashSet< Integer >();
    }
    //----------------------------------------------------------------------
    private void addInapplicalible( Inapplicalible inappl )
    {
        inapplicalible.add( inappl );
    }
    //----------------------------------------------------------------------
    public boolean isInapplicalible()
    {            
        return inapplicalibleFlag;
    }
    //----------------------------------------------------------------------
    // 
    //----------------------------------------------------------------------
    public boolean isInapplicalible( int cid, String cs )
    {   
        // System.out.println( "isInapplicalible( CID: " + cid + " CS: " + cs + " )" );
        
        usedCharacterStates.remove( new Integer( cid ) );
        if( ! inapplicalible.contains( new Inapplicalible( cid, cs ) ) )
        {    
            inapplicalibleFlag = false;
        }
        if( usedCharacterStates.size() > 0 )
        {   
            return true;
        }
        return inapplicalibleFlag;
    }
    //----------------------------------------------------------------------
    public void testInapplicable( int inappl )
    {
        for( int i = 0; i < dependenciesVector.size(); i++ )
        {
            Dependencies dep = (Dependencies) dependenciesVector.get( i );

            if( dep.dep == inappl )
            {
                // System.out.println( dep.cid + " " + dep.cs + " " + dep.dep );
                usedCharacterStates.add( new Integer( dep.cid ) );                    
                addInapplicalible( new Inapplicalible( dep.cid, dep.cs ) );
                
                // ??? recursive ???
                // testInapplicable( dep.cid );
            }
        }
    }
    //----------------------------------------------------------------------
    // Used by NaviKey
    // usedCharacterAndStates is an Vector of (Inapplicalible) contains all 
    // selected CharacterAndStates
    //----------------------------------------------------------------------
    public boolean isApplicalible( int cid, java.util.Vector usedCharacterAndStates  )
    {
        // System.out.println( "isApplicalible( CID: " + cid + " )" );
        
        inapplicalible = new HashSet< Inapplicalible >();
        testInapplicable( cid );
        int size = usedCharacterAndStates.size();
        
        if( size == 0 )
        {
            return true;
        }
        
        for( int i = 0; i < size; i++ )
        {
            Inapplicalible test = (Inapplicalible) usedCharacterAndStates.get( i );
        
            if( inapplicalible.contains( test ) )
            {    
                return false;
            }
        }
        return true;
    }
    //----------------------------------------------------------------------
    public String getWhereClause()
    {
        Iterator iter = usedCharacterStates.iterator();
        boolean firstFlag = true;
        sb = new StringBuffer();
        while (iter.hasNext() )
        {    
            if( ! firstFlag )
            {
                sb.append( " OR " );
            }
            sb.append( "\"CID\"=" + (Integer) iter.next()  );
            firstFlag = false;
        }
        return sb.toString();
    }
}