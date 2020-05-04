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
 * @author Michael Bartley
 * @author Noel Cross
 */

public class TextAttribute extends Attribute
{
    private TextState   ts;
    private String      comment;
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public TextAttribute()
    {
        super();
        ts = new TextState();
        comment = "";
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setTextState(TextState ts)
    {
        this.ts = ts;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public TextState getTextState()
    {
        return ts;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getComment()
    {
        return comment;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void addCommentedState(State s, String str)
    {
        ts = (TextState)s;
        comment = str;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<CommentedState> getCommentedStates()
    {
        Vector<CommentedState> v = new Vector<CommentedState>();
        CommentedState cs = new CommentedState(ts, comment);
        v.addElement(cs);
        return v;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean sameAs( NaviKey navikey, Attribute a)
    {
        boolean same = false;
        try
        {
            TextState ts = this.getTextState();
            String value = ts.getValue();
            
            TextAttribute ta2 = (TextAttribute)a;
            TextState ts2 = ta2.getTextState();
            
            if(value.equals(ts2.getValue()))
            {
                same = true;
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        return same;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Vector<State> getStates()
    {
        Vector<State> v = new Vector<State>();
        v.addElement(ts);
        return v;
    }    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean containsState(State st)
    {
        Vector v = getStates();
        for( int i = 0; i < v.size(); i++ )
        {
            State st2 = (State)v.elementAt(i);
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
        System.out.println(toString());
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("ItemCharacter: " + ic.getId() + ". " + ic.getFeature());
        if(comment != null)
        {
            sb.append(comment);
        }
        if(ts != null)
        {
            sb.append(ts.toString());
        }
        if(comment != null)
        {
            sb.append(comment);
        }
        return new String(sb);
    }
}
