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

<?php include("../config.php"); ?>

<?php
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
	<?php echo "<title>".$title." - State Descriptors</title>"; ?>
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
		| <a href="search.php" title="Data retrieval">Search</a>
		<?php
			if (!$config['readonly']) {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data input\">Edit</a>\n";
			}	
			else {
				echo "| <a href=\"../edit/taxa.php\" title=\"Data browse\">Browse</a>\n";
			}
			if ($config['keys']) {
				echo "| <a href=\"../keys/keys.php\" title=\"Interactive keys\">Keys</a>\n";
			}
			if ($config['geog']) {
				if ($config['maps']) {
					echo "| <a href=\"../maps/maps.php\" title=\"Distribution maps\">Maps</a>\n";
				}
			}	
		?>
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>
<hr>

<?php
	$id = $_GET["ID"];

	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$sql = "SELECT COUNT(D_CHARACTER), D_STATE FROM descriptors WHERE D_CHARACTER='$id' GROUP BY D_STATE";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");

	echo "<h2>State Descriptors - ".$id."</h2>\n";
	echo "<ul>\n";
	echo "<table class=\"browser\">\n";
	while($row = mysql_fetch_array($query)) {
		echo "<tr><td>";
		echo "<a href=\"browse.php?class=descriptors&field=D_STATE&filter=".$row[1]."\">";
		echo $row[1]."</a></td> <td>(".$row[0]." taxa)</td></tr>\n";
	}
	echo "</table>\n";
	echo "</ul>\n";
	
	mysql_free_result($query);
	mysql_close($link);
?>

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>