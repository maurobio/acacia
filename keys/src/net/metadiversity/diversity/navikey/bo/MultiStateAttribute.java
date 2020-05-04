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

import net.metadiversity.diversity.navikey.ui.NaviKey;

import java.util.Vector;

/**
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class MultiStateAttribute extends Attribute
{
    private Vector<CommentedState> commentedStates;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public MultiStateAttribute()
    {
        super();
        commentedStates = new Vector<CommentedState>();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<CommentedState> getCommentedStates()
    { 
        return commentedStates;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<State> getStates()
    {
        Vector<State> v = new Vector<State>();
        for( int i = 0; i < commentedStates.size(); i++ )
        {
            CommentedState cs = commentedStates.elementAt(i);
            State s = cs.getState();
            v.addElement( s );
        }
        return v;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addCommentedState( State s, String comment )
    {
        commentedStates.addElement( new CommentedState( s, comment ) );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addMultiState( MultiState ms )
    {
        addCommentedState( ms, "" );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean sameAs( NaviKey navikey, Attribute a )
    {
        Vector states = a.getStates();
        Vector mystates = getStates();
        for( int i = 0; i < states.size(); i++ )
        {
            State st = ( State )states.elementAt( i );
            if( !st.containedIn( mystates ) )
            {
                return false;
            }
        }
        return true;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean containsState( State st )
    {
        Vector v = getStates();
        for( int i = 0; i < v.size(); i++ )
        {
            State st2 = ( State )v.elementAt( i );
            if( st == st2 )
            {
                return true;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void print()
    {
        System.out.println( toString() );
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append( "ItemCharacter: " + super.ic.getId() + ". " + super.ic.getFeature() );
        if( comment != null )
        {
            sb.append( super.comment );
        }
        sb.append( "States:" );
        for( int i = 0; i < commentedStates.size(); i++ )
        {
            CommentedState cs = ( CommentedState )commentedStates.elementAt( i );
            sb.append( cs.toString() );
        }
        return sb.toString();
    }
}
