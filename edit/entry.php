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
	$t_no = $_GET['t_no'];
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM highertaxa WHERE T_NO=$t_no";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$num_rows = mysql_num_rows($query);
	if ($num_rows > 0) {
		$kingdom = mysql_result($query, 0, 'T_KINGDOM');
		$division = mysql_result($query, 0, 'T_PHYLUM');
		$class = mysql_result($query, 0, 'T_CLASS');
		$order = mysql_result($query, 0, 'T_ORDER');
		$family = mysql_result($query, 0, 'T_FAMILY');
	}
	else {
		$kingdom = "";
		$division = "";
		$class = "";
		$order = "";
		$family = "";
	}
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<title>Higher Taxon Information</title>
</head>
<body>

<center>
<h3>Higher Taxon Information</h3>
<table class="browser" border="0"><td>
<form action="insert.php" method="POST">
<fieldset>
<legend>Fields</legend>
	<p><b>Taxon number: </b><input type="text" name="t_no" value="<?php echo $t_no; ?>" readonly></p>
    <p><b>Kingdom: </b><select name="kingdom" size="1">
	<?php
		$sql = "SELECT DISTINCT T_KINGDOM FROM highertaxa ORDER BY T_KINGDOM";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		while($row = mysql_fetch_array($query)) {
			$selected = "";
			$catName = $row['T_KINGDOM'];
			if ($catName == $division) {
				$selected = "selected='selected'";
			}
			echo "<option value='$catName' $selected>$catName</option>\n";
		}
	?>
    </select></p>
	<p><b>Phylum/Division: </b><select name="division" size="1">
    <?php
		$sql = "SELECT DISTINCT T_PHYLUM FROM highertaxa ORDER BY T_PHYLUM";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		while($row = mysql_fetch_array($query)) {
			$selected = "";
			$catName = $row['T_PHYLUM'];
			if ($catName == $division) {
				$selected = "selected='selected'";
			}
			echo "<option value='$catName' $selected>$catName</option>\n";
		}
	?>
	</select></p>
	<p><b>Class: </b><select name="class" size="1">
	<?php
		$sql = "SELECT DISTINCT T_CLASS FROM highertaxa ORDER BY T_CLASS";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		while($row = mysql_fetch_array($query)) {
			$selected = "";
			$catName = $row['T_CLASS'];
			if ($catName == $class) {
				$selected = "selected='selected'";
			}
			echo "<option value='$catName' $selected>$catName</option>\n";
		}
	?>
    </select></p>
	<p><b>Order: </b><select name="order" size="1">
	<?php
		$sql = "SELECT DISTINCT T_ORDER FROM highertaxa ORDER BY T_ORDER";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		while($row = mysql_fetch_array($query)) {
			$selected = "";
			$catName = $row['T_ORDER'];
			if ($catName == $order) {
				$selected = "selected='selected'";
			}
			echo "<option value='$catName' $selected>$catName</option>\n";
		}
	?>
    </select></p>
	<p><b>Family: </b><select name="family" size="1">
	<?php
		$sql = "SELECT DISTINCT T_FAMILY FROM highertaxa ORDER BY T_FAMILY";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		while($row = mysql_fetch_array($query)) {
			$selected = "";
			$catName = $row['T_FAMILY'];
			if ($catName == $family) {
				$selected = "selected='selected'";
			}
			echo "<option value='$catName' $selected>$catName</option>\n";
		}
	?>
    </select></p>
	<hr>
    <p align="center"><input type="submit" value="OK">
    <input type="reset" value="Cancel" OnClick="window.close()"></p>
	</fieldset>	
</form>
</td></table>
</center>

<?php
	mysql_free_result($query);
	mysql_close($link);
?>

</body>
</html>