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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

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

<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<?php echo "<title>".$title."</title>\n"; ?>
</head>
<body>

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
		[ <a href="../index.php" title="Return to main page">Home</a>
		| <a href="../query/search.php" title="Data retrieval">Search</a>
		<?php
			if (!$config['readonly']) {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data input\">Edit</a>\n";
			}	
			else {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data browse\">Browse</a>\n";
			}
			if ($config['morph']) {
				if ($config['keys']) {
					echo "| <a href=\"../keys/keys.php\" title=\"Interactive keys\">Keys</a>\n";
				}
			}
			if ($config['geog']) {
				if ($config['maps']) {
					echo "| <a href=\"../maps/maps.php\" title=\"Distribution maps\">Maps</a>\n";
				}
			}	
		?>
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| About
		]
		</td>
	</tr>
</table>

<center><h3>The Acacia Biodiversity Database Management System</h3></center>
<hr>

<h3>Overview</h3>

<p>Welcome to <em>Acacia Biodiversity Database Management
System</em>, an interactive data entry, querying, and editing system
based on the <a href="http://sites.google.com/site/acaciadb/">Acacia</a>
generic conceptual schema for taxonomic databases. It combines
the automated use of scientific names and synonyms in a species 
checklist with user-friendly access to geographical data and 
common knowledge data (morphological descriptors, genomics, ecology, 
vernacular names, economic uses, structured notes and conservation status)
about the species. All these data can be cross-indexed to a citation list. 
The design and standard permits rapid customization to suit any
taxonomic group. </p>

<h3>Operating Requeriments</h3>

<p>Minimum operating requirements for running the Acacia
DBMS are:</p>

<ul>
    <li>Linux or Windows server (Apache or IIS)</li>
    <li>PHP version 5.3 or above</li>
    <li>MySQL 5.1 or above</li>
    <li>Internet Explorer, Firefox, Chrome, Opera or
        Safari web browsers</li>
</ul>

A Java&#8482; 2 Runtime Environment is an optional requirement for running the 
interactive keys applet.

<h3>Menu Options</h3>

<h3>Search</h3>

<p>The &quot;Search&quot; form allows to query the database by
Phylum/Division, Class, Order, Family, Genus, Species, Species
epithet, Continent, Region, Country, State/Province, Habitat,
Vernacular name, Use, Conservation status, Morphological
descriptor, or Genetic sequence.</p>

<p>The &quot;Quick Browse&quot; option allows to browse through
species, genera, families, higher taxa, descriptive, genetic, and
geospatial data for which taxa are recorded in the database. 
This is particularly useful for exploratory application by
casual users with only a superficial knowledge of the database contents.</p>

<p>Tabular query results can be downloaded as spreadsheets in
MS-Excel format or comma separated values text files (CSV).</p>

<h3>Edit/Browse</h3>

<p>This option allows to look at and search data
for each data class used in the database (Descriptors,
Distribution, Habitats, Higher Taxa, Notes, Taxa, Synonyms,
Resources, Uses, Vernacular Names, Conservation Status, Genome, 
Bibliography, Metadata), as well as to add or edit data relating to a particular
named species (if the database has not been installed in
read-only mode), using the <a href="http://www.phpmyedit.org/">phpMyEdit</a>
 MySQL table editor and PHP code generator.</p>

<p>Tabular data for each data class can be downloaded as
spreadsheets in MS-Excel format or comma separated values text
files (CSV). For some particular data classes, other, more specific, formats are also available.
So, bibliographic citations can be exported to <a href="http://www.bibtex.org/">BibTeX format</a>, 
morphological descriptive data can be exported to <a href="http://delta-intkey.com/">DELTA format</a>,
genetic sequences data can be exported to <a href="http://zhanglab.ccmb.med.umich.edu/FASTA/">FASTA format</a>,
and geographic distribution data can be exported to <a href="http://earth.google.com/kml/">KML format</a>.
</p>

<h3>Keys</h3>

<p>This option provides access to interactive
identification keys from descriptive data coded in <a
href="http://delta-intkey.com/">DELTA format</a>, using the 
<a href="http://www.navikey.net/">NaviKey</a> Java applet.</p>

<p>To generate interactive keys, it is first required to export descriptive data
to DELTA format by clicking the link on the bottom of the "Descriptors" table.</p>

<h3>Maps</h3>

<p>This option allows species distribution maps to be generated on the
fly, based on georeferenced records stored in the database. Localities are plotted using
the <a
href="http://www.openlayers.org/">OpenLayers</a>
JavaScript library.</p>

<p>Another plot option is available from a link on the bottom of the
&quot;Distribution&quot; table accessible from the Edit/Browse
menu. Clicking this link creates a text file in <a
href="http://earth.google.com/kml/">KML</a> format which can be
saved to disk for later use, or opened for plotting with <a
href="http://earth.google.com/">Google Earth</a>.</p>

<h3>Statistics</h3>

<p>This option displays statistical information
about the currently selected database, including the number of
taxa and taxonomic names, descriptors and descriptors types,
genetic sequences, conservation categories, bibliography, and data dictionaries entries.</p>

<h3>Copyright</h3>

<p><em>Acacia Biodiversity Database Management System</em>
Copyright &copy; 2004-2014 Mauro J. Cavalcanti. The source
code is available <a href="http://code.google.com/p/acacia/">here</a> under the 
<a href="http://www.gnu.org/copyleft/gpl.html">GNU General Public Licence 3</a>.</p>

<h3>Contact</h3>

<p>Comments, suggestions, and bug reports can be
emailed directly to:</p>

<p>Mauro J. Cavalcanti<br>
E-mail: <b>maurobio (at) gmail (dot) com</b><br>
Home page: <a href="http://sites.google.com/site/maurobio/">http://sites.google.com/site/maurobio</a></p>

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>