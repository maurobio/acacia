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

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<?php
	include("../config.php");
	include("../library/functions.php");
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$title = mysqli_result($query, 0, 'M_TITLE');
	$pub = mysqli_result($query, 0, 'M_PUBLISHER');
	$logo = mysqli_result($query, 0, 'M_LOGO');
	$banner = mysqli_result($query, 0, 'M_BANNER');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<script language="JavaScript" type="text/javascript" src="../library/functions.js">
	</script>
	<?php echo "<title>".$title." - Distribution Maps</title>"; ?>
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
		?>
		| Maps
		| <a href="../report/stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center>
<h3>Mapper</h3>
<table class="browser" border="0"><td>
<form action="#" method="POST" name="searchform" id="searchform">
	<fieldset>
	<legend>Species (# records)</legend>
    <p><select multiple="multiple" size="6" name="searchname">
	<?php
		$sql = "SELECT CONCAT(taxa.T_GENUS, ' ', taxa.T_SPECIES, ' ', taxa.T_SUBSP) AS species, COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO GROUP BY species ORDER BY species";
		$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$num_rows = mysqli_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysqli_fetch_array($query)) {
				echo "<option value=\"name\">".$row['species'];
				if ($row['COUNT(*)'] > 1) {
					echo " (".$row['COUNT(*)']." records) ";
				}
				else {
					echo " (".$row['COUNT(*)']." record) ";
				}	
				echo "</option>\n";
			}
		}
		else {
			echo "<option value=\"name\">No distribution data available</option>";
		}
	?>
	</select>
	<!-- <p><input type="checkbox" name="track" value="0">Plot individual tracks<p/> -->
    <hr>
    <p align="center"><input type="button" value="Submit" OnClick="displayMap()">
    <input type="reset" value="Reset"></p>
	</fieldset>
 </form>
</td></table> 
Hold down the Ctrl (Linux/Windows) or Command (Mac) key to select multiple species.
</center>

<?php
	mysqli_free_result($query);
	mysqli_close($link);
?>

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>