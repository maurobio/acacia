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
/*
 * TestRegex.java
 * 
 * Created on 12.11.2007, 16:27:24
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.metadiversity.diversity.navikey.util;


import java.util.regex.*;

/**
 *
 * @author didi
 */
public class TestRegex {

    /** Creates a new instance of TestRegex */
    public TestRegex() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        
        String test = "Test test";
        
        test = test.replace( " ", "%20" );
        
        System.out.println( test );
        
        String [] strings = { "Erysiphe adunca var. regularis (R. Y. Zheng & G. Q. Chen) U. Braun & [111] S. Takam. [2000006] [A33] kljadfglkahdgfaldghflahgflagdf",
                              "Candelariella placodizans_Vaagaa",      // oslo
                              "Mycocalicium subtile_Gundersen",
                              "Alectoria sarmentosa ssp. vexillifera", // Iceland
                              "Lecidea lactea ssp. pantherina",
                              "Toninia tristis subsp. asiae-centralis",// Italic
                              "Umbilicaria crustulosa subsp. crustulosa var. crustulosa",
                              "Cladonia cervicornis subsp. cervicornis",
                              "Sarcogyne regularis var. regularis",
                              "Squamarina cartilaginea f. pseudocrassa",
                              "Toninia tristis subsp. asiae-centralis",
                              "Toninia tristis Subsp. asiae-centralis",
                              "Lecanora silvae-nigrae",
                              "test1 test2 test3",
                              "Acer saccharinum L.",
                              
                              "twst \\i{} asjdfgkjasgdfas \\i0{}",
                              "twst \\sub{} asjdfgkjasgdfas \\sub0{} \\nonsupersub{} ",
                              "C\\sub{}4\\nosupersub{}"
                              
                            };   
        
        String p = null;
        
        p = "(.* )(.*\\.)(.*\\()";
        p = "(.* )(.*\\.)";
        p = "(\\w* )(\\w*)";  // to words
        p = "(\\w* )([a-zA-Z_0-9\\-]*)( [a-zA-Z_\\-]*\\. )";  // abc def-ghi
        p = "([a-zA-Z\\-]* )([a-zA-Z\\-]*)( [a-zA-Z\\-]*\\.)";  // abc def-ghi
        p = "([a-zA-Z\\-]* )([a-zA-Z\\-]*)";  // abc def-ghi
        p = "([a-zA-Z\\-]+ )([a-zA-Z\\-]+)";  // abc def-ghi

        p = "([a-zA-Z\\-]+ )([a-zA-Z\\-]+)(.ssp\\.)";  // abc def-ghi
        p = "([a-zA-Z\\-]+ )([a-zA-Z\\-]+)";  // abc def-ghi

        p = "([a-zA-Z\\-]+ )([a-zA-Z\\-]+ )([^s][^s][^p][^\\.])";
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (ssp\\.)";

        
        // negation !?
        // ??? case insensitive ?i
        
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!(ssp\\.))";
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!(ssp\\.))";
        // Alle einträge die aus 2 Wörtern bestehen, und das 3. Wort nicht ssp., var., f. subso. ist        
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!(ssp\\.)|(var\\.)|(subsp\\.)|(f\\.))";

        
        
        
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!((ssp\\.)|(var\\.)|(subsp\\.)|(f\\.)))";
        
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!(ssp\\.)|(f\\.))";
        p = ".*(?!(ssp\\.)|(var\\.)|(subsp\\.)|(f\\.))";

        p = "([a-zA-Z\\-]+)\\b([a-zA-Z\\-]+)(?!\\b(?:ssp\\.|var\\.|subsp\\.|f\\.))";

        p = "([a-zA-Z\\-]+)\\b([a-zA-Z\\-]+)(?!\\b(ssp\\.|var\\.|subsp\\.|f\\.))(.*)";

        p = "([a-zA-Z\\-]+)\\b([a-zA-Z\\-]+)(?!(\\b(?:ssp|var|subsp|f)\\.))(.*)";

        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!((?:ssp|var|subsp|f)\\.))(.*)";
        p = "([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!((ssp|var|subsp|f)\\.))(.*)";
        
        p = "(?i)(.*subsp.*)";
        p = "(?i)([a-zA-Z\\-]+) ([a-zA-Z\\-]+) (?!((ssp|var|subsp|f)\\.))(.*)";
        p = "([a-zA-Z\\-]+)";
        
        p = "Ö";
        
        // alles was vor "(" Klammerautor kommt
        
        p = "([^ ]*)( )([^ ]*)( )[^\\()]";

        p = "[A-Z][^(\\(|_|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z)]*";
        p = "[A-Z][^(\\(|_|A-Z)]*";
        
        
/*        
\i      Start italics   \i{}Agrostis\i0{}
\i0     Stop italics
\b      Start bold      \b{}Habit\b0{}
\b0     Stop bold
\sub    Start subscript C\sub{}4\nosupersub{}
\nosupersub Stop super- or subscript
\fsN    Font size       \fs16{}eight point
\plain  Set default font attributes
\endash En dash         4\endash{}10mm
\lquote                 \lquote{}normal\rquote{}
\rquote Right quote
*/
        
        p = "\\\\(i|b|sub|nonsupersub)(0\\{|\\{)\\}";
        
        // Start with \, then not {, end with {}
        
        p = "\\\\[^\\{]*\\{\\}";
        
        //Pattern pattern = Pattern.compile( p );
        
        // Pattern pattern = Pattern.compile( p, Pattern.CASE_INSENSITIVE ); // only ASCII Characters
        //  + Pattern.CASE_INSENSITIVE
        Pattern pattern = Pattern.compile( p, Pattern.UNICODE_CASE ); 
        
        for( int ii= 0; ii < strings.length; ii++ )
        {
            Matcher matcher = pattern.matcher( strings[ ii ] );

            // boolean b = matcher.matches();

            System.out.println( "String: " + strings[ ii ] + "Result : " + strings[ ii ].replaceAll( p , "*") );

            while( matcher.find() ) 
            {
                int i = matcher.groupCount();

                System.out.println( "  groupCount(): " + i );        

                for( int n = 0; n <= i; n++ )
                {    
                    System.out.println( "  Result ( " + n + " ): " + matcher.group( n ) );
                }    
            }
        }
    }
}
