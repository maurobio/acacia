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
/*
 * DeltaInterface defines the methods needed to interact and work with our
 * delta data model
 * by defining through an interface, different implementations of the Delta
 * logic can be made, through a direct connection or intermediary like
 * a client-server interaction
 *
 * @author Michael Bartley
 * @author Dieter Neubacher
 */
import java.util.Vector;

public interface DeltaInterface
{
    //the intelligence flag indicates whether or not shrinking lists occur
    public Vector getItemCharacterList(Vector<BasicState> basicStates, boolean intelligent, boolean depentencies );//, boolean intelligence); //vector of BasicItemCharacters returned
    public Vector getStateList(Vector<BasicState> basicStates, Object characterId, boolean intelligent );//, boolean intelligence); //vector of BasicStates returned
    public Vector getItemList(Vector<BasicState> basicStates); //vector of BasicItems returned
    public BasicItemCharacter getBasicItemCharacter(Object itemCharacterId);
    public Item getItem(Object itemId); //for use in generating description list
    public String getItemTitle(Object itemId);
    public Vector< String > getItemDescription(Object itemId);
    public Vector getItemResources(Object itemId);
    public String getItemImage( String item );    
}
