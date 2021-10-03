<?php
/*================================================================================*
*       Acacia - A Generic Conceptual Schema for Taxonomic Databases              *
*                 Copyright 2008-2019 Mauro J. Cavalcanti                         *
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

<?php include("config.php"); ?>
<?php include("library/functions.php"); ?>

<?php
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
	<link rel="stylesheet" href="library/stylesheet.css" type="text/css">
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
		<!-- <div style="font-variant:small-caps;"> -->
		[ Home
		| <a href="query/search.php" title="Data retrieval">Search</a>
		<?php
			if (!$config['readonly']) {
				echo "| <a href=\"edit/taxa.php\" title=\"Data input\">Edit</a>\n";
			}	
			else {
				echo "| <a href=\"edit/taxa.php\" title=\"Data browse\">Browse</a>\n";
			}
			if ($config['morph']) {
				if ($config['keys']) {
					echo "| <a href=\"keys/keys.php\" title=\"Interactive keys\">Keys</a>\n";
				}
			}
			if ($config['geog']) {
				if ($config['maps']) {
					echo "| <a href=\"maps/maps.php\" title=\"Distribution maps\">Maps</a>\n";
				}
			}	
		?>
		| <a href="report/stats.php" title="Database statistics">Statistics</a>
		| <a href="help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>
	<!-- </style> -->
</table>

<center><h3>Main Page</h3>
<table class="gridtable" width="80%">
   <tr>
	<th colspan=2>Database details</th>
	</tr>
   <tbody>
    <tr>
	  <td><b>Full name:</b></td>
	  <td><?php echo mysqli_result($query, 0, 'M_TITLE')?></td>
	</tr>
	<tr>
	  <td><b>Short name:</b></td>
	  <td><?php echo mysqli_result($query, 0, 'M_ACRONYM')?></td>
	</tr>
    <tr>
      <td><b>Authors/editors:</b></td>
      <td><?php echo mysqli_result($query, 0, 'M_AUTHOR')?></td>
    </tr>
    <tr>
      <td><b>Version:</b></td>
      <td><?php echo mysqli_result($query, 0, 'M_VERSION')?></td>
    </tr>
    <tr>
      <td><b>Release date:</b></td>
      <td><?php echo mysqli_result($query, 0, 'M_DATE')?></td>
    </tr>
	<tr>
	  <td><b>Geographical scope:</b></td>
	  <td><?php echo mysqli_result($query, 0, 'M_SCOPE')?></td> 	
	</tr>
	<tr>
	  <td><b>Ecological scope:</b></td>
	  <td><?php echo mysqli_result($query, 0, 'M_ENVIRONMENT')?></td> 	
	</tr>
    <tr>
      <td><b>Taxonomic coverage:</b></td>
	  <?php 
		$sql = "SELECT DISTINCT T_KINGDOM, T_PHYLUM, T_CLASS, T_ORDER, T_FAMILY FROM highertaxa";
		$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$ndiv = mysqli_num_rows($res);
		$cover = "";
		if ($ndiv > 1) {
			$sql = "SELECT DISTINCT T_PHYLUM FROM highertaxa";
			$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($res)) {
				$cover .= $row[0]."/";
			}
		}
		else {
			$row = mysqli_fetch_array($res);
			$cover = $row[0]." - ".$row[1]." - ".$row[2]." - ".$row[3]." - ".$row[4];
		}
		echo "<td>".chop($cover,'/')."</td>";
		$sql = "UPDATE metadata SET M_COVERAGE='".$cover."'";
		$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
      ?>
	</tr>
    <tr>
      <td><b>Number of species:</b></td>
	  <?php 
		$sql = "SELECT COUNT(*) FROM taxa WHERE T_STATUS = 'Accepted' AND T_RANK = 'Species'";
		$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
		$value = mysqli_fetch_array($res);
		$ntax = $value[0];
      ?>
	  <td><?php echo $ntax; ?></td>
    </tr>
	<?php
		if ($config['subsp']) {
			echo "<tr>\n";
			echo "<td><b>Number of infraspecific taxa:</b></td>\n";
			$sql = "SELECT COUNT(*) FROM taxa WHERE T_STATUS = 'Accepted' AND T_RANK = 'Subsp.' OR T_RANK = 'Var.'";
			$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_fetch_array($res);
			$ninftax = $value[0];
			$sql = "SELECT COUNT(*) FROM synonyms WHERE S_RANK = 'Subsp.'";
			$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_fetch_array($res);
			$ninfsyn = $value[0];
			echo "<td>".$ninftax."</td>\n";
			echo "</tr>\n";
		}
	?>
	<tr>
	  <td><b>Number of synonyms:</b></td>
	  <?php
			$sql = "SELECT COUNT(*) FROM synonyms WHERE S_RANK = 'Species' OR S_RANK = 'Subsp.' OR S_RANK = 'Var.'";
			$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$value = mysqli_fetch_array($res);
			$nsyn = $value[0];
	   ?>
	   <td><?php echo $nsyn; ?></td>
	</tr>	
    <?php 
        if ($config['common']) {
    		echo "<tr>\n";
    		echo "<td><b>Number of common names:</b></td>\n";
        	$sql = "SELECT DISTINCT V_NAME FROM commonnames";
			$res = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			$ncomm = mysqli_num_rows($res);
		    echo "<td>".$ncomm."</td>\n";
    		echo "</tr>\n";
		}
		else {
			$ncomm = 0;
		}
    ?>
    <tr>
      <td><b>Total number of names:</b></td>
      <td><?php echo $ntax + $nsyn + $ninftax + $ninfsyn + $ncomm; ?></td>
    </tr>
    <tr>
      <td><b>Abstract:</b></td>
      <td><?php echo mysqli_result($query, 0, 'M_DESCRIPTION'); ?></td>
    </tr>
    <tr>
      <td><b>Organization:</b></td>
      <td><?php echo mysqli_result($query, 0, 'M_PUBLISHER'); ?></td>
    </tr>
    <tr>
      <td><b>Web site:</b></td>
	  <?php $url = mysqli_result($query, 0, 'M_URL'); ?>
	  <td><?php echo "<a href=".$url.">".$url."</a>"; ?></td>
	</tr>
  </tbody>
</table>
</center>

<?php
	mysqli_free_result($query);
	mysqli_close($link);
?>

<br>
<hr />
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>