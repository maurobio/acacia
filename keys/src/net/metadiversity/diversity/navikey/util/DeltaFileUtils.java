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
/**
 * 
 */
package net.metadiversity.diversity.navikey.util;

/**
 * @author Ben Richardson
 *
 */
public class DeltaFileUtils {

    /**
     * Gets whether the line contains the provided directive
     * <p>
     * From the DELTA-Intkey web site
     * (http://www.delta-intkey.com/www/standard.htm):
     * "A Confor or PANKEY directive consists of a star (*), a control phrase of
     * up to four words, and data. The star must be at the start of a line, or
     * be preceded by a blank. A blank following the star is optional. The
     * control phrase must be in upper-case letters. Only the first three
     * symbols of each word of the control phrase are significant. However, it
     * is recommended that the words be written in full, to make the directive
     * as readable as possible. The data take different forms, depending on the
     * control phrase, and in some directives are absent. A control phrase must
     * be contained in one line, but its data may extend over several lines. A
     * directive is terminated by the star at the start of the next directive,
     * or by the end of the file."
     * @param line       the line to be checked
     * @param directive  the directive to look for
     * @return whether the directive is embedded in the line (true or false)
     */
    public static boolean containsDirective(String line, String directive)
    {
        boolean ret = false;
        line = line.trim();
        line = line.replaceAll("[\t ]", " ");
        
        if ( line.startsWith("*"))
        {
            /*
             * Test for the allowed variations of the *DIRECTIVE value
             */
            if (line.indexOf("*"+directive) == 0)
            {
                /*
                 * '*DIRECTIVE'
                 */
                ret = true;
            }
            else if (line.indexOf("* "+directive) == 0)
            {
                /*
                 * '* DIRECTIVE'
                 */
                ret = true;
            }
            else
            {
                /*
                 * Split the directive into words
                 */
                String[] directiveWords = directive.split("[\t ]");
                String shortDirectiveWord = null;
                String shortDirective = null;
                for (int i=0; i<directiveWords.length; i++)
                {
                    shortDirectiveWord = directiveWords[i].substring(0, 3);
                    if (i == 0)
                    {
                        shortDirective = shortDirectiveWord;
                    }
                    else
                    {
                        shortDirective =
                            shortDirective.concat(" "+shortDirectiveWord);
                    }
                }
                
                if (line.indexOf("*"+shortDirective) == 0)
                {
                    /*
                     * '*DIR'
                     */
                    ret = true;
                }
                else if (line.indexOf("* "+shortDirective) == 0)
                {
                    /*
                     * '* DIR'
                     */
                    ret = true;
                }
            }
        }
        
        return ret;
    }

}
