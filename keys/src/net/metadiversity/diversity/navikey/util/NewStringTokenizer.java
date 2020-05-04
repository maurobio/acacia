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
package net.metadiversity.diversity.navikey.util;


/**
 * Unlike StringTokenizer, NewStringTokenizer matches the entire
 * delimiter, allowing for more than one character delimiter
 * @author Michael Bartley
 * changes by Noel Cross 11/98
 */

public class NewStringTokenizer
{
    
    private String delim;
    private String secondaryDelimeter = null;  //Mike added 2/12/99
    private String newString;
    private Character openComment =  null;
    private Character closeComment = null;
    
    public NewStringTokenizer(String str, String delim)
    {
        this.delim = delim;
        newString = str;
    }
    
    public NewStringTokenizer(String str, String delim, char open, char close)
    {
        this(str, delim, new Character(open), new Character(close));
    }
    
    public NewStringTokenizer(String str, String delim, Character open, Character close)
    {
        this(str, delim);
        openComment = open;
        closeComment = close;
    }
    
    public void setSecondaryDelimeter(String s)
    {
        secondaryDelimeter = s;
    }
    
    public String tokenWithComment()
    {
        StringBuffer sb = new StringBuffer();
        int opencount = 0;
        int closecount = 0;
        char open = openComment.charValue();
        char close = closeComment.charValue();
        while(hasMoreTokens())
        {
            String addition = tokenWithoutComment();
            sb.append(addition);
            for(int i = 0; i < addition.length(); i++)
            {
                
                if(open == addition.charAt(i))
                {
                    opencount++;
                }
                else if(close == addition.charAt(i))
                {
                    closecount++;
                }
            }
            if(opencount == closecount)
                break;
        }
        return new String(sb);
    }
    
    public boolean hasMoreTokens()
    {
        if(newString.length() == 0)
            return false;
        return true;
    }
    
    //Method added by Noel Cross 11/98
    public boolean hasMoreElements()
    {
        return hasMoreElements();
    }
    
    public String nextToken()
    {
        if(openComment != null)
            return tokenWithComment();
        else
            return tokenWithoutComment();
    }
    
    public String tokenWithoutComment()
    {
        String rv;
        //find the position of the delimiter
        int position = newString.indexOf(delim);
        int p2 = position;
        if(secondaryDelimeter != null)
            p2 = newString.indexOf(secondaryDelimeter);
        
        position = (position > p2) ? p2 : position;
        
        if(position < 0)
        { //changed by Noel Cross from "if(position <= 0){"
            rv = newString;
            newString = "";
        }
        else
        {
            rv = newString.substring(0, position);
            newString = newString.substring(position + delim.length());
        }
        return rv;
    }
    
    public static void main(String argv[])
    {
        //String test = "This is a test.";
        //NewStringTokenizer st = new NewStringTokenizer(test, "es");
        
        String test = "222|first||third";
        NewStringTokenizer st = new NewStringTokenizer(test, "|");
        int count = 0;
        while(st.hasMoreTokens())
        {
            count++;
            System.out.println("token " + count + " = " + st.nextToken());
        }
    }
    
}
