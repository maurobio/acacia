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
 * An Attribute is member of an Item object.  Attributes are subclassed.
 *
 * @author Michael Bartley
 * @author Noel Cross
 * @author Dieter Neubacher
 */

public abstract class Attribute implements java.util.Comparator, java.lang.Comparable
{
    protected  ItemCharacter    ic;
    protected  String           comment;
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public Attribute()
    {
        
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public void setItemCharacter(ItemCharacter ichar)    
    {
        ic = ichar;
    }
    public ItemCharacter getItemCharacter()
    {
        return ic;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    public void setComment(String str)
    {
        comment = str;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public String getComment()
    {
        return comment;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean isMultistate()
    {
        return ic.isMultiState();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean isNumeric()
    {
        return ic.isNumeric();
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public boolean isText()
    {
        return ic.isText();
    }
    //--------------------------------------------------------------------------
    // used for sorting the output list
    //--------------------------------------------------------------------------
    public int compare( java.lang.Object a, java.lang.Object b )
    {
        return ((Attribute) a).getItemCharacter().getId().compareTo( ((Attribute) b).getItemCharacter().getId() ); 
    }
    public int compareTo( java.lang.Object o )
    {
        return this.getItemCharacter().getId().compareTo( ((Attribute) o).getItemCharacter().getId() ); 
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------    
    
    public abstract Vector getCommentedStates();
    public abstract Vector<State> getStates();
    public abstract boolean containsState(State st);
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public abstract void addCommentedState( State st, String comment );
    public abstract boolean sameAs( NaviKey navikey, Attribute a );
    public abstract String toString();
    public abstract void print();
}
