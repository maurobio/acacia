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

import java.util.Vector;

import net.metadiversity.diversity.navikey.ui.NaviKey;

/**
 * @author Noel Cross
 * @author Michael Bartley
 * @author Dieter Neubacher
 */

public class NumericAttribute extends Attribute
{
    private Vector<CommentedState> commentedStates;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public NumericAttribute()
    {
        super();
        commentedStates = new Vector<CommentedState>();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addCommentedState(State s, String str)
    {
        commentedStates.addElement(new CommentedState(s, str));        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector getCommentedStates()
    {
        return commentedStates;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<State> getStates()
    {
        Vector<State> v = new Vector<State>();
        for( int i = 0; i < commentedStates.size(); i++)
        {
            v.addElement( commentedStates.elementAt(i).getState() );
        }
        return v;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean containsState(State st)
    {
        Vector v = getStates();
        for(int i = 0; i < v.size(); i++)
        {
            State st2 = (State)v.elementAt(i);
            if(st == st2)
            {
                return true;
            }
        }
        return false;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public boolean sameAs( NaviKey navikey, Attribute a)
    {
        boolean noContradiction = false;
        Vector passed = a.getStates();
        Vector v2 = getStates();
        
        outerloop:
            for(int i=0; i<passed.size(); i++)
            {
                noContradiction = false;
                NumericState statePassed = (NumericState)passed.elementAt(i);
                if(statePassed == null)
                {
                    continue;
                }
                for(int j=0; j < v2.size(); j++)
                {
                    NumericState s2 = (NumericState) v2.elementAt(j);
                    // statePassed must fall within the extreme ranges of s2
                    if( navikey.isExtremeInterval() == true &&
                        navikey.isOverlappingInterval() == false &&    
                        statePassed != null && s2 != null && s2.fallsWithinExtremeRange( statePassed ))
                    {
                        noContradiction = true;
                        break outerloop;
                    }
                    else
                    // statePassed must fall within the normal ranges of s2
                    if( navikey.isExtremeInterval() == false && 
                        navikey.isOverlappingInterval() == false &&    
                        statePassed != null && s2 != null && s2.fallsWithinNormalRange( statePassed ))
                    {
                        noContradiction = true;
                        break outerloop;
                    }
                    // statePassed must overlapping the extreme ranges of s2
                    if( navikey.isExtremeInterval() == true &&
                        navikey.isOverlappingInterval() == true &&    
                        statePassed != null && s2 != null && s2.overlappingExtremeRange( statePassed ))
                    {
                        noContradiction = true;
                        break outerloop;
                    }
                    else
                    // statePassed must overlapping the normal ranges of s2
                    if( navikey.isExtremeInterval() == false && 
                        navikey.isOverlappingInterval() == true &&    
                        statePassed != null && s2 != null && s2.overlappingNormalRange( statePassed ))
                    {
                        noContradiction = true;
                        break outerloop;
                    }
                }
            }
            return noContradiction;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void print()
    {
        System.out.println(toString());
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("ItemCharacter: " + super.ic.getId() + ". " + super.ic.getFeature());
        for(int i=0; i<commentedStates.size(); i++)
        {
            CommentedState cs = commentedStates.elementAt(i);
            NumericState ns = (NumericState)cs.getState();
            String comment = cs.getComment();
            if(comment != null)
            {
                sb.append(comment);
            }
            if(ns != null)
            {
                sb.append(ns.toString());
            }
        }
        if(super.comment != null)
        {
            sb.append(super.comment);
        }
        return new String(sb);
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String shortOutput()
    {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < commentedStates.size(); i++)
        {
            if( i > 0 )
            {
                sb.append("/");
            }
            CommentedState cs = commentedStates.elementAt(i);
            NumericState ns = (NumericState)cs.getState();
            String comment = cs.getComment();
            if(comment != null)
            {
                sb.append(comment);
            }
            if(ns != null)
            {
                sb.append(ns.getStringValue());
            }
        }
        if(super.comment != null)
        {
            sb.append(super.comment);
        }
        return new String(sb);
    }    
}
