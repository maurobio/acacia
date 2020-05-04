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

/**
 * DeltaFilePeer.java
 * @author Noel Cross
 * @author Michael Bartley
 */

import java.io.BufferedReader;

public class DeltaFilePeer
{
    private BufferedReader  specsReader;
    private BufferedReader  charsReader;
    private BufferedReader  itemsReader;
    private BufferedReader  tImagesReader;
    
    private boolean         removeCharsCommentFlag;
    private boolean         removeItemsCommentFlag;
    private boolean         removeSpecsCommentFlag;
    
    private boolean         removeCharsStatesCommentFlag;
    private boolean         removeItemsStatesCommentFlag;
    private boolean         removeSpecsStatesCommentFlag;
    
    public DeltaFilePeer( BufferedReader specs, 
                          boolean removeSpecsCommentFlag, 
                          boolean removeSpecsStatesCommentFlag, 
                          BufferedReader chars, 
                          boolean removeCharsCommentFlag, 
                          boolean removeCharsStatesCommentFlag, 
                          BufferedReader items, 
                          boolean removeItemsCommentFlag, 
                          boolean removeItemsStatesCommentFlag,
                          BufferedReader timages                           
                        )
    {        
        specsReader = specs;
        charsReader = chars;
        itemsReader = items;
        tImagesReader = timages;
        
        this.removeCharsCommentFlag = removeCharsCommentFlag;
        this.removeItemsCommentFlag = removeItemsCommentFlag;
        this.removeSpecsCommentFlag = removeSpecsCommentFlag;
        
        this.removeCharsStatesCommentFlag = removeCharsStatesCommentFlag;
        this.removeItemsStatesCommentFlag = removeItemsStatesCommentFlag;
        this.removeSpecsStatesCommentFlag = removeSpecsStatesCommentFlag;
    }
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    public DeltaData loadDeltaData()
    {        
        DeltaData dd = new DeltaData();
        
        SpecsFileReader sfr = new SpecsFileReader();
        CharsFileReader cfr = new CharsFileReader();
        ItemsFileReader ifr = new ItemsFileReader();
        TImageFileReader tifr = new TImageFileReader(); 
        
        sfr.read( specsReader, removeSpecsCommentFlag, removeSpecsStatesCommentFlag );
        sfr.makeItemCharacterTypes();
        sfr.makeItemCharacterDependencies();
        sfr.makeImpliciteValues();
        
        dd.setItemCharacterTypesVector( sfr.getItemCharacterTypes() );
        dd.setDependenciesVector( sfr.getDependenciesVector() );
        
        
        cfr.setItemCharacterTypes( sfr.getItemCharacterTypes() );
        cfr.read( charsReader, removeCharsCommentFlag, removeCharsStatesCommentFlag );

        dd.setItemCharacters( cfr.getItemCharacters() );
        
        // ??? No functionallity ??? ifr.setItemCharacters( cfr.getItemCharacters() );
        ifr.read( itemsReader, removeItemsCommentFlag, removeItemsStatesCommentFlag, dd, sfr.getImplicideValues() );
        
        dd.setItemsVector( ifr.getItems() );
        
        // Item Images
        
        tifr.read( tImagesReader );
        dd.setItemImageHashMap( tifr.getItemImageHashMap() );
        
        return dd;
    }
}
