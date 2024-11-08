<?php include("password_protect.php"); ?>
<!--
#=================================================================================#
#       Acacia - A Generic Conceptual Schema for Taxonomic Databases              #
#                 Copyright 2008-2019 Mauro J. Cavalcanti                         #
#                           maurobio@gmail.com                                    #
#                                                                                 #
#   This program is free software: you can redistribute it and/or modify          #
#   it under the terms of the GNU General Public License as published by          #
#   the Free Software Foundation, either version 3 of the License, or             #
#   (at your option) any later version.                                           #
#                                                                                 #
#   This program is distributed in the hope that it will be useful,               #
#   but WITHOUT ANY WARRANTY; without even the implied warranty of                #
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                  #
#   GNU General Public License for more details.                                  #
#                                                                                 #
#   You should have received a copy of the GNU General Public License             #
#   along with this program. If not, see <http://www.gnu.org/licenses/>.          #
#=================================================================================#
-->

<?php include("../config.php"); ?>
<?php include("../library/functions.php"); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
		"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>Resources table</title>
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
</head>
<body>

<?php
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$title = mysqli_result($query, 0, 'M_TITLE');
	$pub = mysqli_result($query, 0, 'M_PUBLISHER');
	$logo = mysqli_result($query, 0, 'M_LOGO');
	$banner = mysqli_result($query, 0, 'M_BANNER');
	mysqli_free_result($query);
	mysqli_close($link);
?>

<?php
	if (!empty($banner)) {
		echo "<img src=\"".$banner."\" alt=\"Project banner\" width=\"991\" height=\"120\">\n";
	}
	else {
		echo "<table class=\"header\" width=\"100%\">\n";
		echo "<tr>\n";
		echo "<td width=\"10%\"> <img src=\"".$logo."\" alt=\"Project logo\" width=\"128\" height=\"80\"></td>\n";
		echo "<td width=\"50%\">".$title."<br></td>\n";
		echo "</tr>\n";
		echo "</table>\n";
	}
?>

<table class="menu" width="100%">
	<tr>
		<td align="center" width="100%">
		[ <a href="../index.php?logout=1" title="Return to main page">Home</a>
		<?php
			if ($config['morph']) {
				echo "|	<a href=\"descriptors.php\" title=\"User defined descriptors\">Descriptors</a>";
			}
			if ($config['gene']) {
				echo "| <a href=\"genome.php\" title=\"Genomic data\">Genome</a>";
			}
			if ($config['geog']) {
				echo "|	<a href=\"distribution.php\" title=\"Geographic distribution\">Distribution</a>";
			}
			if ($config['ecol']) {
				echo "|	<a href=\"habitats.php\" title=\"Habitat data\">Habitats</a>";
			}
		?>
		| <a href="highertaxa.php" title="Higher taxon membership">Higher Taxa</a>
		| <a href="notes.php" title="Structured notes">Notes</a>
		| <a href="taxa.php" title="Taxonomic editor">Taxa</a>
		| <a href="synonyms.php" title="Nomenclatural editor">Synonyms</a>
		| Resources
		<?php
			if ($config['common']) {
				echo "| <a href=\"uses.php\" title=\Uses data\">Uses</a>";
				echo "| <a href=\"commonnames.php\" title=\"Vernacular names\">Vernacular Names</a>";
			}
			if ($config['status']) {
				echo "| <a href=\"status.php\" title=\"Conservation status\">Conservation</a>";
			}	
		?>
		| <a href="bibliography.php" title="Edit bibliographic references">Bibliography</a>
		| <a href="metadata.php" title="Database configuration">Metadata</a>
		]
		</td>
	</tr>	
</table>

<center>
<h3>Editor - Resources table</h3>
</center>
<?php

/*
 * IMPORTANT NOTE: This generated file contains only a subset of huge amount
 * of options that can be used with phpMyEdit. To get information about all
 * features offered by phpMyEdit, check official documentation. It is available
 * online and also for download on phpMyEdit project management page:
 *
 * http://platon.sk/projects/main_page.php?project_id=5
 *
 * This file was generated by:
 *
 *                    phpMyEdit version: 5.7.1
 *       phpMyEdit.class.php core class: 1.204
 *            phpMyEditSetup.php script: 1.50
 *              generating setup script: 1.50
 */

// MySQL host name, user name, password, database, and table
$opts['hn'] = $config['host'];
$opts['un'] = $config['user'];
$opts['pw'] = $config['pwd'];
$opts['db'] = $config['dbname'];
$opts['tb'] = 'resources';

// Name of field which is the unique key
$opts['key'] = 'ID';

// Type of key field (int/real/string/date etc.)
$opts['key_type'] = 'int';

// Sorting field(s)
$opts['sort_field'] = array('T_NO');

// Number of records to display on the screen
// Value of -1 lists all records in a table
$opts['inc'] = 15;

// Options you wish to give the users
// A - add,  C - change, P - copy, V - view, D - delete,
// F - filter, I - initial sort suppressed
if ($config['readonly']) {
        $opts['options'] = 'VFI';
}
else {        
        $opts['options'] = 'ACPVDF';
}

// Number of lines to display on multiple selection filters
$opts['multiple'] = '4';

// Navigation style: B - buttons (default), T - text links, G - graphic links
// Buttons position: U - up, D - down (default)
$opts['navigation'] = 'DB';

// Display special page elements
$opts['display'] = array(
	'form'  => true,
	'query' => true,
	'sort'  => false,
	'time'  => false,
	'tabs'  => true
);

// Set default prefixes for variables
$opts['js']['prefix']               = 'PME_js_';
$opts['dhtml']['prefix']            = 'PME_dhtml_';
$opts['cgi']['prefix']['operation'] = 'PME_op_';
$opts['cgi']['prefix']['sys']       = 'PME_sys_';
$opts['cgi']['prefix']['data']      = 'PME_data_';

/* Get the user's default language and use it if possible or you can
   specify particular one you want to use. Refer to official documentation
   for list of available languages. */
//$opts['language'] = $_SERVER['HTTP_ACCEPT_LANGUAGE'] . '-UTF8';
$opts['language'] = 'EN';

/* Table-level filter capability. If set, it is included in the WHERE clause
   of any generated SELECT statement in SQL query. This gives you ability to
   work only with subset of data from table.

$opts['filters'] = "column1 like '%11%' AND column2<17";
$opts['filters'] = "section_id = 9";
$opts['filters'] = "PMEtable0.sessions_count > 200";
*/

/* Field definitions
   
Fields will be displayed left to right on the screen in the order in which they
appear in generated list. Here are some most used field options documented.

['name'] is the title used for column headings, etc.;
['maxlen'] maximum length to display add/edit/search input boxes
['trimlen'] maximum length of string content to display in row listing
['width'] is an optional display width specification for the column
          e.g.  ['width'] = '100px';
['mask'] a string that is used by sprintf() to format field output
['sort'] true or false; means the users may sort the display on this column
['strip_tags'] true or false; whether to strip tags from content
['nowrap'] true or false; whether this field should get a NOWRAP
['select'] T - text, N - numeric, D - drop-down, M - multiple selection
['options'] optional parameter to control whether a field is displayed
  L - list, F - filter, A - add, C - change, P - copy, D - delete, V - view
            Another flags are:
            R - indicates that a field is read only
            W - indicates that a field is a password field
            H - indicates that a field is to be hidden and marked as hidden
['URL'] is used to make a field 'clickable' in the display
        e.g.: 'mailto:$value', 'http://$value' or '$page?stuff';
['URLtarget']  HTML target link specification (for example: _blank)
['textarea']['rows'] and/or ['textarea']['cols']
  specifies a textarea is to be used to give multi-line input
  e.g. ['textarea']['rows'] = 5; ['textarea']['cols'] = 10
['values'] restricts user input to the specified constants,
           e.g. ['values'] = array('A','B','C') or ['values'] = range(1,99)
['values']['table'] and ['values']['column'] restricts user input
  to the values found in the specified column of another table
['values']['description'] = 'desc_column'
  The optional ['values']['description'] field allows the value(s) displayed
  to the user to be different to those in the ['values']['column'] field.
  This is useful for giving more meaning to column values. Multiple
  descriptions fields are also possible. Check documentation for this.
*/

$opts['fdd']['ID'] = array(
  'name'     => 'Media resource number',
  'select'   => 'T',
  'options'  => 'AVCPDR', // auto increment
  'maxlen'   => 10,
  'default'  => '0',
  'sort'     => true
);
$opts['fdd']['T_NO'] = array(
  'name'     => 'Taxon name',
  'select'   => 'T',
  'maxlen'   => 10,
  'sort'     => true
);
//$opts['fdd']['D_NO'] = array(
//  'name'     => 'Descriptor name',
//  'select'   => 'T',
//  'maxlen'   => 10,
//  'sort'     => true
//);
$opts['fdd']['R_TYPE'] = array(
  'name'     => 'Type of resource',
  'select'   => 'T',
  'maxlen'   => 50,
  'sort'     => true
);
$opts['fdd']['R_RESOURCE'] = array(
  'name'     => 'Filename of media resource',
  'select'   => 'T',
  'maxlen'   => 128,
  'sort'     => true
);
$opts['fdd']['R_CAPTION'] = array(
  'name'     => 'Resource caption',
  'select'   => 'T',
  'maxlen'   => 255,
  'sort'     => true
);

$opts['fdd']['T_NO']['values']['table'] = 'taxa'; 
$opts['fdd']['T_NO']['values']['column'] = 'T_NO';
$opts['fdd']['T_NO']['values']['description']['columns'][0] = 'T_GENUS'; 
$opts['fdd']['T_NO']['values']['description']['columns'][1] = 'T_SPECIES'; 
if ($config['subsp']) {
	$opts['fdd']['T_NO']['values']['description']['columns'][2] = 'T_SUBSP';
}
$opts['fdd']['T_NO']['values']['description']['divs'][0] = ' ';
$opts['fdd']['T_NO']['values']['description']['divs'][1] = ' ';
$opts['fdd']['T_NO']['values']['orderby'] = 'T_GENUS, T_SPECIES, T_SUBSP';
$opts['fdd']['R_TYPE']['values'] = array('Audio', 'Illustration', 'Map', 'Photo', 'Video');
$opts['fdd']['R_RESOURCE']['js']['required'] = true;
$opts['fdd']['R_RESOURCE']['js']['hint'] = 'Filename of media resource field is required.';
$opts['fdd']['R_CAPTION']['js']['required'] = true;
$opts['fdd']['R_CAPTION']['js']['hint'] = 'Resource caption field is required.';

// Now important call to phpMyEdit
require_once 'phpMyEdit.class.php';
new phpMyEdit($opts);

?>
<p>
Download as: <a href="../export/excel.php?table=resources&sort=T_NO">Excel</a> | <a href="../export/csv.php?table=resources&sort=T_NO">CSV</a>
</p>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>