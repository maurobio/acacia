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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import net.metadiversity.diversity.navigator.db.delta_editor.InapplicalibleTest;
import net.metadiversity.diversity.navigator.db.delta_editor.Inapplicalible;

import net.metadiversity.diversity.navikey.ui.NaviKey;
import net.metadiversity.diversity.navikey.delta.DeltaData;
/**
 *
 * Class Delta:
 * This class models a DELTA data provider.
 *
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
public class Delta implements DeltaInterface
{
    private boolean debug = false;    
    private NaviKey navikey;

    // Dalta Data
    
    DeltaData dd;
    
    
    /*************************************************************************
     *
     * Delta
     *     public constructor
     *
     ************************************************************************/
    public Delta( NaviKey navikey )
    {
        this.navikey = navikey;  // for get NaviKey Options (not static)
        
        if( debug ) System.out.println( "Delta()" );
    }
    /*************************************************************************
     *
     * getItemCharacterList
     *    public method
     * input:   Vector  should hold elements of BasicState objects
     *          boolean sets use of intelligent aspects
     * output:  Vector  contains elements of BasicItemCharacter objects
     *
     ************************************************************************/
    public Vector getItemCharacterList( Vector<BasicState> basicStates, boolean intelligent, boolean depentenciesFlag )
    {
        if( debug ) System.out.println( "getItemCharacterList( " + basicStates + " , " + intelligent + " )" );
        Vector<ItemCharacter> v = new Vector<ItemCharacter>();
        
        InapplicalibleTest it = null;
        java.util.Vector<Inapplicalible>   usedCharacterAndStates = new java.util.Vector<Inapplicalible>();

        if( depentenciesFlag )
        {
            it = new InapplicalibleTest( dd.getDependenciesVector() );
        }

        Hashtable<State, ItemCharacter> charsUsed = null;

        if( basicStates != null )
        {
            charsUsed = new Hashtable<State, ItemCharacter>( (int) (10 + 1.4 * basicStates.size()) );  //key = state, object = char
            //first put all selected characters in a lookup hash
            //anything already used will not be offered for future selections
            
            for( int i = 0; i < basicStates.size(); i++ )
            {
                State s = State.getState( basicStates.elementAt(i), dd  );
                charsUsed.put( s, s.getItemCharacter() );
                
                // System.out.println( s.getClass().getName() );
                if( s.getItemCharacter().isMultiState() || s.getItemCharacter().isNumeric() )
                {    
                    // This data is selected for searching 
                    MultiState ms = (MultiState) s;
                    int cid    = s.getItemCharacter().getId().intValue();
                    String cs  = ((Integer) ms.getId()).toString();

                    usedCharacterAndStates.add( new Inapplicalible( cid, cs ) );
                }
            }            
        }
        
        if( intelligent && basicStates != null )
        {
            Vector<Item> items = getItemList( basicStates );
            if( items.size() > 1 )
            {
                Enumeration allChars = dd.getItemCharacters().elements();
                Hashtable<State, ItemCharacter> chars = new Hashtable<State, ItemCharacter>();  //key = state, object = char
                while( allChars.hasMoreElements() )
                {
                    ItemCharacter ic = ( ItemCharacter )allChars.nextElement();
                                        
                    // Don't add Inapplicalible Characters
                    if( depentenciesFlag )
                    {
                        if( ! it.isApplicalible( ic.getId().intValue(), usedCharacterAndStates  ) )
                        {
                            /*
                            System.out.println( "Skip Character " + ic.getId().intValue() );
                             */
                            continue;
                        }    
                    }                    
                    if( charsUsed.contains( ic ) )
                    {
                        continue;
                    }
                    //note, we haven't checked to see if this will actually be
                    //a useful state, only that its a number and used by at
                    //least two items.  Ergo this call can be improved.                    
                    else if( ic.isNumeric() )
                    {
                        int count = 0;
                        for( int i = 0; i < items.size(); i++ )
                        {
                            Attribute a = ( items.elementAt(i) ).getAttributeWithItemCharacter( ic );
                            if( a != null )
                            {
                                count++;
                            }
                            if( count > 1 )
                            {
                                v.addElement( ic );
                                break;
                            }
                        }
                        continue;
                    }                    
                    //don't show text characters as choices
                    else if( ic.isText() )
                    {
                        continue;
                    }
                    //got here, so must be multi-state
                    int stateCount = 0;
                    count_states:
                    for( int i = 0; i < items.size(); i++ )
                    {
                        Attribute a = ( items.elementAt(i) ).getAttributeWithItemCharacter( ic );
                        //if we have an attribute, check it for states
                        if( a != null )
                        {
                            Vector<State> states = a.getStates();
                            for( int j =0 ; j < states.size(); j++ )
                            {
                                State s = states.elementAt( j );
                                //if a state hasn't been used to id a char
                                //before, add it to hash and up state count.
                                //The hash is neccessary so we don't up
                                //state count on the same state.

                                if( !chars.containsKey( s ) )
                                {
                                    chars.put( s, ic );
                                    stateCount++;
                                }
                            }                                
                        /*we have multiple choices, so char is important
                          NOTE that we haven't checked to see if the state
                          will reduce our items list, only if multiple
                          states are selected.  i.e., we could have two
                          items, one has this character and two state
                          values, the other doesn't have this character,
                          then this character will be listed along with
                          two states, but no selection will reduce item
                          list.  Ergo this call can be improved.
                         */
                            if( stateCount > 1 )
                            {
                                v.addElement( ic );
                                //should end for loop, since we've
                                //justified adding a character
                                break count_states;
                            }
                        }
                    }
                }
            }
        }
        //END OF SHRINKING LOGIC
        else
        {
            //System.out.println( "\nNO BASIC STATES TO USE FOR SHRINKING!\n" );
            Enumeration e = dd.getItemCharacters().elements();
            while( e.hasMoreElements() )
            {
                BasicItemCharacter ic = ( ItemCharacter ) e.nextElement();
                if( !ic.isText() )
                {
                    // Don't add Inapplicalible Characters
                    if( depentenciesFlag )
                    {
                        if( ! it.isApplicalible( ic.getId().intValue(), usedCharacterAndStates  ) )
                        {
                            /*
                            System.out.println( "Skip Character " + ic.getId().intValue() );
                             */
                            continue;
                        }    
                        v.addElement( ( ItemCharacter ) ic );
                    }
                    else
                    {
                        v.addElement( ( ItemCharacter ) ic );                        
                    }
                }
            }
        }
        java.util.Collections.<ItemCharacter>sort( v );
        return v;        
    }        
    /*************************************************************************
     *
     * getStateList
     *    public method
     * input:   Vector, Object;  Vector should contain elements of type
     *                    BasicState, Object should be an Integer
     *          boolean  identifies use of intelligent features
     * output:  Vector   will contain elements of type BasicState, null
     *                    if Object does not identify a multi-state character
     *
     ************************************************************************/
    public Vector<State> getStateList( Vector<BasicState> basicStates, Object characterId, boolean intelligent )
    {
        if( debug ) System.out.println( "getStateList( " + basicStates + " , " + characterId + " , " + intelligent + " )" );
        Vector<State> v = new Vector<State>();
        ItemCharacter ic = (ItemCharacter) dd.getItemCharacters().get( characterId );
        if( !ic.isMultiState() )
        {
            return null;
        }
        //SHRINKING LOGIC REMOVED FOR SPEED ISSUES
        //WILL BE RE-IMPLEMENTED AT A LATER DATE AND TIME
        //MIKE, 2/9/99
        //PUT BACK IN, MIKE -- 6/7/99
        //if( false ){
        if( intelligent && basicStates != null )
        {
            //key = state, object = char
            Hashtable<State, ItemCharacter> charsUsed = new Hashtable<State, ItemCharacter>( (int) (10 + 1.4 * basicStates.size()) );
            
            for( int i = 0; i < basicStates.size(); i++ )
            {
                State s =State.getState( basicStates.elementAt(i), dd  );
                charsUsed.put( s, s.getItemCharacter() );
            }
            Vector<Item> items = getItemList( basicStates );
            for( int i=0; i<items.size(); i++ )
            {
                Attribute a = ( items.elementAt(i) ).getAttributeWithItemCharacter( ic );
                if( a != null )
                {
                    Vector<State> states = a.getStates();
                    for( int j=0; j<states.size(); j++ )
                    {
                        State s = states.elementAt(j);
                        if( !v.contains( s ) )
                        {
                            v.addElement( s );
                        }
                    }
                }
            }
        }
        //END SHRINKING LOGIC
        else
        {
            Vector<State> states = ic.getStates();
            for( int i = 0; i < states.size(); i++ )
            {
                BasicState bs = ( BasicState )states.elementAt( i );
                v.addElement( (State) bs );
            }
        }
        return v;
    }        
    /*************************************************************************
     *
     * getItemList
     *    public method
     * input:   Vector   should contain elements of type BasicState
     * output:  Vector   contains elements of type BasicItem
     *
     ************************************************************************/
    public Vector<Item> getItemList( Vector<BasicState> basicStates )
    {
        if( debug ) System.out.println( "getItemList( " + basicStates + " )" );
        Vector<Item> v = new Vector<Item>();
        Enumeration e = dd.getItemsVector().elements();
        int i=0;
        while( e.hasMoreElements() )
        {
            //System.out.println( "Added element number " + ++i );
            v.addElement( ( Item )e.nextElement() );
        }
        //System.out.println( "Done generating item list" );
        if( basicStates != null )
        {
            Item item = buildItem( basicStates );
            item.initCannotBe(); // multible "OR" selection.
            v = getBasicPossibleMatches( item, v );
        }
        else
        {
            // Display "allItems" and "resultItems" on GUI        
            navikey.setAllItemCount( v.size() );
            navikey.setFoundItemCount( v.size() );
            // System.out.println( "returning " + v.size() + " items" );           
        }
        java.util.Collections.<Item>sort( v );
        return v;        
    }        
    /*************************************************************************
     *
     * buildItem
     *     private method
     * input:   Vector   should contain elements of type BasicState
     * output:  Item     item based on give states
     *
     ************************************************************************/
    private Item buildItem( Vector<BasicState> basicStates )
    {
        if( debug ) System.out.println( "buildItem( " + basicStates + " )" );
        Item item = new Item();
        for( int i = 0; i < basicStates.size(); i++ )
        {
            //NOTE: could redo Attribute.java and its subclasses to make this nicer
            // already have State.getState( BasicState bs ) to help!
            BasicState bs = basicStates.elementAt( i );
            ItemCharacter ic = (ItemCharacter) dd.getItemCharacters().get( bs.getCharacterId() );
            if( ic.isMultiState() )
            {
                Vector<State> states = ic.getStates();
                for( int j = 0; j < states.size(); j++ )
                {
                    State s = states.elementAt(j);
                    if( s.getName().compareTo( bs.getName() ) == 0 )
                    {
                        Attribute a = new MultiStateAttribute();
                        a.setItemCharacter( ic );
                        a.addCommentedState( s, "" );
                        item.addAttribute( a );
                        break;
                    }
                }
            }
            else if( ic.isNumeric() )
            {
                Attribute a = new NumericAttribute();
                a.setItemCharacter( ic );
                NumericState ns = new NumericState( bs, dd );
// ???                ns.setItemCharacter( ItemCharacter.restore( bs.getCharacterId() ) );
                ns.setItemCharacter( ic );
                a.addCommentedState( ns, "" );
                item.addAttribute( a );
            }
        }
        return item;
    }
    /*************************************************************************
     *
     * getPossibleMatches
     *   input:   Item, Vector;  Vector should contain all possible items to
     *                           be compared against Item
     *   output:  Vector  -  contains all possible Item matches
     *
     ************************************************************************/
/*    
    private Vector getPossibleMatches( Item item, Vector allItems )
    {
        if( debug ) System.out.println( "getPosibleMatches( " + item + " , " + allItems + " )" );
        if( debug ) System.out.println( "getPosibleMatches( item, allItems )" );
        Vector v = new Vector();
        for( int i = 0; i < allItems.size(); i++ )
        {
            Item item2 = ( Item )allItems.elementAt( i );
            if( !item.cannotBe( item2 ) )
            {
                v.addElement( item2 );
            }
        }
        System.out.println( "returning " + v.size() + " items" );
        return v;
    }
 */
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    private Vector<Item> getBasicPossibleMatches( Item item, Vector allItems )
    {
        // if( debug ) System.out.println( "getBasicPossibleMatches( " + item + " , " + allItems + " )" );
        if( debug ) System.out.println( "getBasicPossibleMatches( item, allItems )" );
        
        // Display "allItems" on GUI        
        navikey.setAllItemCount( allItems.size() );
        System.out.println( "allItems.size() : " + allItems.size() );
        
        Vector<Item> v = new Vector<Item>();
        for( int i = 0; i < allItems.size(); i++ )
        {
            Item item2 = ( Item )allItems.elementAt( i );
// DIDI ???            if( !item.cannotBe( item2 ) )            
            if( !item.cannotBe( navikey, item2 ) )
            {
                v.addElement( item2 );
            }
        }
        // Display "resultItems" on GUI        
        navikey.setFoundItemCount( v.size() );
        System.out.println( "returning " + v.size() + " items" );
        return v;
    }    
    /*************************************************************************
     *
     * getItem
     *   input:   Object  - should be identifying integer
     *   output:  Item  -  item with id of Object
     *
     ************************************************************************/
    public Item getItem( Object itemId )
    {
        if( debug ) 
        {
            System.out.println( "getItem( " + itemId +  " )" );
        }
        Item i = dd.getItem( ((Integer) itemId ).intValue() );
        return i;
    }    
    /*************************************************************************
     *
     * getItemTitle
     *   input:   Object  - should be identifying integer
     *   output:  String  - name of identified item
     *
     ************************************************************************/
    public String getItemTitle( Object itemId )
    {
        if( debug ) 
        {
            System.out.println( "getItemTitle( " + itemId + " )" );
        }
        return getItem( itemId ).getName();
    }    
    /*************************************************************************
     *
     * getItemDescription
     *   input:   Object  - should be identifying integer
     *   output:  Vector  - contains set of strings describing item
     *
     ************************************************************************/
    public Vector<String> getItemDescription( Object itemId )
    {
        if( debug ) 
        {
            System.out.println( "getItemDescription( " + itemId + " )" );
        }
        
        Item myItem = getItem( itemId );
        Vector<String> strings = new Vector<String>();
        String currentString;
        Vector atv = myItem.getAttributes();
        // 30.10.2007 sort output date because the implicide Values are appendet.          
        
        java.util.Collections.sort( atv );

        for( int i = 0; i < atv.size(); i++ )
        {
            Attribute at = ( Attribute )atv.elementAt( i );
            currentString = at.getComment();
            if( currentString == null )
            {
                currentString = "";
            }
            currentString =currentString + at.getItemCharacter().getFeature();
            if( at.getItemCharacter().isMultiState() )
            {
                Vector csv = at.getCommentedStates();
                for( int j = 0; j < csv.size(); j++ )
                {
                    CommentedState cs = ( CommentedState )csv.elementAt( j );
                    State s = cs.getState();
                    String sn;
                    try
                    {
                        sn = s.getName();
                    }
                    catch( Exception e )
                    {
                        sn = "";
                        System.out.println( "OOPS!" + e );
                    }
                    String sc = cs.getComment();
                    if( sc == null )
                        sc = "";
                    currentString = currentString +":  "+ sn + sc;
                }
            }
            else if( at.getItemCharacter().isNumeric() )
            {
                NumericAttribute na = ( NumericAttribute )at;
                String rs;
                try
                {
                    rs = na.shortOutput();
                }
                catch( Exception e )
                {
                    rs = "NUMBER NOT SHOWING UP";
                }
                currentString = currentString + ":  " + rs;
            }
            else if ( at instanceof TextAttribute )
            {
                try
                {
                    TextAttribute ta = ( TextAttribute )at;
                    TextState ts = ta.getTextState();
                    currentString = currentString + ":  " + ts.getValue();
                }
                catch( Exception e )
                {
                    System.out.println( "Must be a resource." );
                }
            }
            strings.addElement( currentString );
        }
        return strings;
    }    
    /*************************************************************************
     *
     * getItemResources
     *   input:   Object  - should be identifying integer
     *   output:  Vector  - elements are of type Resource of requested Item
     *
     ************************************************************************/
    public Vector<Resource> getItemResources( Object itemId )
    {
        if( debug )
        { 
            System.out.println( "getItemResource( " + itemId + " )" );
        }
        Vector<Resource> rv = new Vector<Resource>();
        Item myItem = getItem( itemId );
        Vector atv = myItem.getAttributes();
        String title = myItem.getName();
        for( int i = 0; i < atv.size(); i++ )
        {
            Attribute at = ( Attribute )atv.elementAt( i );
            if( at instanceof ResourceAttribute )
            {
                try
                {
                    ResourceAttribute ra = ( ResourceAttribute )at;
                    Vector v = ra.getResources();
                    for( int j=0; j<v.size(); j++ )
                    {
                        try
                        {
                            ResourceState rs = ( ResourceState )v.elementAt( j );
                            Resource re= rs.getResource();
                            re.setTitle( title );
                            rv.addElement( re );
                        }
                        catch( Exception e )
                        {
                            System.out.println( "Unable to get resource. "+e );
                        }
                    }
                }
                catch( Exception e )
                {
                    System.out.println( "Not a resource, must be text. "+e );
                }
            }
        }
        return rv;
    }    
    /*************************************************************************
     *
     * getBasicItemCharacter
     *   input:   Object  - should be indentifying integer
     *   output:  BasicItemCharacter
     ************************************************************************/
    public BasicItemCharacter getBasicItemCharacter( Object itemCharacterId )
    {
        if( debug ) System.out.println( "getBasicItemCharacter( " + itemCharacterId + " )" );
        return ( BasicItemCharacter ) dd.getItemCharacter( (( Integer )itemCharacterId).intValue() );
    }
    //--------------------------------------------------------------------
    public void setData( DeltaData dd )
    {
        this.dd = dd;
    }
    //--------------------------------------------------------------------------
    public DeltaData getDeltaData() 
    {
        return dd;
    }
    //--------------------------------------------------------------------------
    public String getItemImage( String item )
    {
        return (String) dd.getItemImageHashMap().get( item );
    }
}
