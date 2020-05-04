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

//--------------------------------------------------------------------------
//--------------------------------------------------------------------------        
public class Inapplicalible implements Comparable
{
    public int cid;
    public String cs;
    
    public Inapplicalible(int cid, String cs)
    {
        this.cid = cid; 
        this.cs = cs.trim(); 
    }
    
    public int compareTo(Object obj)
    {
        Inapplicalible comp = ( Inapplicalible ) obj;
        
        if( cid < comp.cid )
            return -1;
        else if( cid > comp.cid )
            return 1;
        else 
            return cs.compareTo( comp.cs );
    }
    
    public boolean equals(Object obj)
    {
        Inapplicalible comp = ( Inapplicalible ) obj;
        
        return ( cid == comp.cid && cs.equals( comp.cs ) );
    }
    //------------------------------------------------------------------
    // implement hashCode(), this is used by HashSet to determinate
    // if to objects my be equals
    //------------------------------------------------------------------
    public int hashCode()
    {
        // int h = super.hashCode();
        // System.out.print( cid + " " + cs + " " + h );
        // h = cid  + cs.hashCode();
        // System.out.println( " " + h );
        return cid  + cs.hashCode();
    }
}