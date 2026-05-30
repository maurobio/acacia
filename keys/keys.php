<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2026 Mauro J. Cavalcanti                         *
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
<?php include("../mysql.php"); ?>

<?php
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd'], $config['dbname']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
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
	<?php echo "<title>An Interactive Key to ".$title."</title>\n"; ?>
</head>
<body>

<?php
	$banner_image = ".."."/images/".$banner;
	if (!empty($banner)) {
		echo "<img src=\"".$banner_image."\" alt=\"Project banner\" width=\"991\" height=\"120\">\n";
	}
	else {
		$logo_image = ".."."/images/".$logo;
		echo "<table class=\"header\" width=\"100%\">\n";
		echo "<tr>\n";
		echo "<td width=\"10%\"> <img src=\"".$logo_image."\" alt=\"Project logo\" width=\"128\" height=\"80\"></td>\n";
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
		?>
		| Keys
		<?php
			if ($config['geog']) {
				if ($config['maps']) {
					echo "| <a href=\"../maps/maps.php\" title=\"Distribution maps\">Maps</a>\n";
				}
			}	
		?>		
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../query/factsheet.php" title="Species fact sheet">Fact Sheet</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>
</table>

<center>
<h3>Interactive Key</h3>
<h2><a href="../keys/sliks/ACACIA.html">ACACIA Demonstration Database</a></h2>
</center>

<p>Powered by <a href="https://www.stingersplace.com/SLIKS/" target="_blank">SLIKS</a><br>

<br>
<hr>
<p align="center">
<a class="footer" href="http://www.biotupe.org/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>