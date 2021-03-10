<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2021 Mauro J. Cavalcanti                         *
*                           maurobio@gmail.com                                    *
*                                                                                 *
*   This program is free software: you can redistribute it and/or modify          *
*   it under the terms of the GNU General Public License as published by          *
*   the Free Software Foundation, either version 3 of the License, or             *
*   (at your option) any later version.                                           *
*                                                                                 *
*   This program is distributed in the hope that it will be useful,               *
*   but WITHOUT ANY WARRANTY; without even the implied warranty of                *
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                  *
*   GNU General Public License for more details.                                  *
*                                                                                 *
*   You should have received a copy of the GNU General Public License             *
*   along with this program. If not, see <http://www.gnu.org/licenses/>.          *
*=================================================================================*/?>

<?php
	include("../config.php");
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	$pub = mysql_result($query, 0, 'M_PUBLISHER');
	$logo = mysql_result($query, 0, 'M_LOGO');
	$banner = mysql_result($query, 0, 'M_BANNER');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<script language="JavaScript" type="text/javascript" src="../library/functions.js">
	</script>
	<?php echo "<title>".$title." - Database Statistics</title>"; ?>
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
		| Statistics
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center>
<h3>Database Statistics</h3>
<table class="browser" border="0"><td>
<form action="#" method="POST" name="reportform" id="reportform">
<fieldset>
	<legend>Options</legend>
	<input type="radio" name="stats" value="summary" checked /> Summary Statistics<br />
	<input type="radio" name="stats" value="detail" /> Detailed Statistics<br />
	<input type="checkbox" name="charts" value="true" />Plot Charts 
	</fieldset>
	<fieldset>
		<legend>Section</legend>
		<table class="browser" border="0">
		<tr>
		<td><input type="checkbox" name="all" value="all" checked OnClick="enableControls()" />All<br /></td>
		</tr>
		<tr>
		<td><input type="checkbox" name="section" value="taxa" disabled />Taxa<br /></td>
		<td><input type="checkbox" name="section" value="names" disabled />Names<br /></td>
		<td><input type="checkbox" name="section" value="notes" disabled />Notes<br /></td>
		</tr>
		<?php 
			if ($config['common']) {
				echo "<tr>\n";
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"common\" disabled />Common<br /></td>\n";
			}
			if ($config['morph']) {
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"descriptors\" disabled />Descriptors<br /></td>\n";
			}
			if ($config['gene']) {
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"sequences\" disabled />Sequences<br /></td>\n";
				echo "</tr>\n";
			}
			if ($config['geog']) {
				echo "<tr>\n";
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"geography\" disabled />Geography<br /></td>\n";
			}	
			if ($config['ecol']) {	
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"ecology\" disabled />Ecology<br /></td>\n";
			}
			if ($config['status']) {	
				echo "<td><input type=\"checkbox\" name=\"section\" value=\"status\" disabled />Conservation<br /></td>\n";
				echo "</tr>\n";
			}	
		?>
		<td><input type="checkbox" name="section" value="bibliography" disabled />Bibliography<br /></td>
		<td><input type="checkbox" name="section" value="media" disabled />Media<br /></td>
		</table>
		<hr>
		<p align="center"><input type="button" value="Submit" OnClick="getOption()">
		<input type="reset" value="Reset" OnClick="enableControls()"></p>
	</fieldset>
 </form>
</td></table>
</center>

<?php
	mysql_free_result($query);
	mysql_close($link);
?>

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>