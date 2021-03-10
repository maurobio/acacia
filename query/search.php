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
	$environ = mysql_result($query, 0, 'M_ENVIRONMENT');
	$sql = "SELECT * FROM highertaxa";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$kingdom = mysql_result($query, 0, 'T_KINGDOM');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<script language="JavaScript" type="text/javascript" src="../library/functions.js">
	</script>
	<?php echo "<title>".$title." - Query</title>"; ?>
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
		| Search
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
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center>
<h3>Search Engine</h3>
<table class="browser" border="0"><td>
<form action="#" method="GET" name="searchform" id="searchform">
<fieldset>
<legend>Search</legend>
    <p><select size="1" name="searchfield">
	<?php
		if (ucfirst($kingdom) == "Animalia") {
			echo "<option value=\"division\">Phylum</option>";
		}	
		else {
			echo "<option value=\"division\">Division</option>";
		}
	?>	
	<option value="class">Class</option>
	<option value="order">Order</option>
	<option value="family">Family</option>
	<option value="genus">Genus</option>
	<option selected value="species">Species</option>
	<option value="epithet">Species epithet</option>
	<?php
		if ($config['geog']) {
			if (ucfirst($environ) == "Marine") {
				echo "<option value=\"continent\">Ocean</option>";
			}
			else {
				echo "<option value=\"continent\">Continent</option>";
			}
			echo "<option value=\"region\">Region</option>";
			echo "<option value=\"country\">Country</option>";
			echo "<option value=\"state\">State/Province</option>";
		}
		if ($config['ecol']) {
			echo "<option value=\"habitat\">Habitat</option>";
		}
		if ($config['common']) {
			echo "<option value=\"common\">Vernacular name</option>";
			echo "<option value=\"use\">Use</option>";
		}
		if ($config['status']) {
			echo "<option value=\"status\">Conservation status</option>";
		}
		if ($config['morph']) {
			echo "<option value=\"descriptor\">Descriptor</option>";
		}
		if ($config['gene']) {
			echo "<option value=\"nucleotide\">DNA sequence</option>";
			echo "<option value=\"protein\">Protein sequence</option>";
		}
	?>
	</select>
	&nbsp; = &nbsp; <input type="text" size="45" name="searchterm"></p>
    <hr>
    <p align="center"><input type="button" value="Search" OnClick="if (validateTextSearch(this)) {selectOption()}">
    <input type="reset" value="Reset"></p>
</fieldset>	
</form>
</td></table>
</center>

<?php
	mysql_free_result($query);
	mysql_close($link);
?>

<hr>
<p><center>
Quick browser:<p>
<?php
	if (ucfirst($kingdom) == "Animalia") {
		echo "<a href=\"list.php?option=division\">Phyla</a>";
	}
	else {
		echo "<a href=\"list.php?option=division\">Divisions</a>";
	}
?>	
| <a href="list.php?option=class">Classes</a>
| <a href="list.php?option=order">Orders</a>
| <a href="list.php?option=family">Families</a>
| <a href="list.php?option=genus">Genera</a>
| <a href="list.php?option=species">Species</a>
| <a href="list.php?option=synonym">Synonyms</a>
<?php
	if ($config['geog']) {
		if (ucfirst($environ) == "Terrestrial" || ucfirst($environ) == "Freshwater") {
			echo "| <a href=\"list.php?option=continent\">Continents</a>";
		}
		else {
			echo "| <a href=\"list.php?option=continent\">Oceans</a>";
		}
		echo "| <a href=\"list.php?option=region\">Regions</a>";
		echo "| <a href=\"list.php?option=country\">Countries</a>";
		echo "| <a href=\"list.php?option=state\">States</a>";
	}
	if ($config['ecol']) {
		echo "| <a href=\"list.php?option=habitat\">Habitats</a>";
	}
	if ($config['common']) {
		echo "| <a href=\"list.php?option=common\">Vernaculars</a>";
		echo "| <a href=\"list.php?option=use\">Uses</a>";
	}
	if ($config['status']) {
		echo "| <a href=\"list.php?option=status\">Conservation</a>";
	}
	if ($config['morph']) {
		echo "| <a href=\"list.php?option=descriptor\">Descriptors</a>";
	}
	if ($config['gene']) {
		echo "| <a href=\"list.php?option=genome\">Genes</a>";
	}
?>
</center></p> 

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>