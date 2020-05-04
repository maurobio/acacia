<?php session_start(); session_destroy(); ?>
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

<?php
	$link = mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	$selected = mysqli_select_db($link, $config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$title = mysqli_result($query, 0, 'M_TITLE');
	$pub = mysqli_result($query, 0, 'M_PUBLISHER');
	$logo = mysqli_result($query, 0, 'M_LOGO');
	$banner = mysqli_result($query, 0, 'M_BANNER');
	$environ = mysqli_result($query, 0, 'M_ENVIRONMENT');
	$url = mysqli_result($query, 0, 'M_URL');
	$sql = "SELECT * FROM highertaxa";
	$query = mysqli_query($link, $sql) or die("Error: MySQL query failed"); 
	$kingdom = mysqli_result($query, 0, 'T_KINGDOM');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
	<?php echo "<title>".$title." - Quick list</title>\n"; ?>
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

<?php
	$option = $_GET['option'];
	
	switch ($option) {
		case "division":
			if (ucfirst($kingdom) == "Animalia") {
				echo "<h2>Phyla</h2>\n";
			}
			else {
				echo "<h2>Divisions</h2>\n";
			}
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(highertaxa.T_PHYLUM), highertaxa.T_PHYLUM FROM taxa, highertaxa WHERE highertaxa.T_NO = taxa.T_NO GROUP BY highertaxa.T_PHYLUM";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=highertaxa&field=T_PHYLUM&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;

		case "class":
			echo "<h2>Classes</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(highertaxa.T_CLASS), highertaxa.T_CLASS FROM taxa, highertaxa WHERE highertaxa.T_NO = taxa.T_NO GROUP BY highertaxa.T_CLASS";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=highertaxa&field=T_CLASS&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "order":
			echo "<h2>Orders</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(highertaxa.T_ORDER), highertaxa.T_ORDER FROM taxa, highertaxa WHERE highertaxa.T_NO = taxa.T_NO GROUP BY highertaxa.T_ORDER";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=highertaxa&field=T_ORDER&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;	
	
		case "family":
			echo "<h2>Families</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(highertaxa.T_FAMILY), highertaxa.T_FAMILY FROM taxa, highertaxa WHERE highertaxa.T_NO = taxa.T_NO GROUP BY highertaxa.T_FAMILY";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=highertaxa&field=T_FAMILY&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
		
		case "genus":
			echo "<h2>Genera</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(T_GENUS), (SELECT DISTINCT T_GENUS) FROM taxa GROUP BY T_GENUS";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=taxa&field=T_GENUS&filter=".$row[1]."\">";
				echo "<i>".$row[1]."</i></a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
		  
		case "species":
			echo "<h2>Accepted Names</h2>\n";
			echo "<ul>\n";
			$sql = "SELECT * FROM highertaxa, taxa WHERE taxa.T_NO = highertaxa.T_NO ORDER BY T_PHYLUM, T_CLASS, T_ORDER, T_FAMILY, T_GENUS, T_SPECIES, T_SUBSP";
			if ($config['subsp']) {
				$sql = $sql.", T_SUBSP";
			}
			$grpCheck = "";
			$grpEval = "";
			$aubCheck = "";
			$subEval = "";
			$ordCheck = "";
			$ordEval = "";
			$famCheck = "";
			$famEval = "";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				$grpCheck = $row['T_PHYLUM'];
				if ($grpCheck != $grpEval) {
					$grpEval = $grpCheck;
					echo "<p><b>".$grpCheck."</b></p>";
				}
				$subCheck = $row['T_CLASS'];
				if ($subCheck != $subEval) {
					$subEval = $subCheck;
					echo "<p><i>".$subCheck."</i></p>";
				}
				$ordCheck = $row['T_ORDER'];
				if ($ordCheck != $ordEval) {
					$ordEval = $ordCheck;
					echo "<p>".$ordCheck."</p>";
				}
				$famCheck = $row['T_FAMILY'];
				if ($famCheck != $famEval) {
					$famEval = $famCheck;
					echo "<p>".strtoupper($famCheck)."</p>";
				}
				echo "<dl>\n";
				echo "<dd>\n";
				if ($row['T_STATUS'] != "Accepted") {
					echo "?";
				}
				$name = $row['T_GENUS']." ".$row['T_SPECIES'];
				$subsp = False;
				if ($config['subsp']) {
					if (strlen($row['T_SUBSP']) > 0) {
						$name = $name." ".$row['T_SUBSP'];
						$subsp = True;
					}
				}
				if ($subsp) {
					$author = $row['T_SP_AUTHOR'];
				}
				else {
					$author = $row['T_S_AUTHOR'];
				}
				echo "<a href=\"species.php?name=".urlencode(chop($name))."\"><i>".$name."</i> ".$author."</a>\n";
				echo "</dd>\n";
				echo "</dl>\n";
			}
			echo "</ul>\n";
			break;
			
		case "synonym":
			echo "<h2>Accepted Names plus Synonyms</h2>\n";
			echo "<ul>\n";
			$sql1 = "SELECT * FROM taxa ORDER BY T_GENUS, T_SPECIES, T_SUBSP";
			$query1 = mysqli_query($sql1, $link) or die("Error: MySQL query failed");
			while($row1 = mysqli_fetch_array($query1)) {
				if ($row1['T_STATUS'] == "Provisional") {
					echo "?";
				}
				if (!empty($row1['T_SUBGENUS'])) {
					$subgen = " (".$row1['T_SUBGENUS'].") ";
				}
				else {
					$subgen = " ";
				}
				$name = $row1['T_GENUS']." ".$row1['T_SPECIES'];
				$subsp = False;
				if ($config['subsp']) {
					if (!empty($row1['T_SUBSP'])) {
						$name = $name." ".$row1['T_SUBSP'];
						$subsp = True;
					}
				}
				if ($subsp) {
					$author = $row1['T_SP_AUTHOR'];
				}
				else {
					$author = $row1['T_S_AUTHOR'];
				}
				echo "<a href=\"species.php?name=".urlencode(chop($name))."\"><i>".$name."</i> ".$author."</a>";
				echo "<br>\n";

				$id = $row1['T_NO'];
				$sql2 = "SELECT * FROM synonyms WHERE T_NO = '$id'";
				$query2 = mysqli_query($sql2, $link) or die("Error: MySQL query failed");
				while($row2 = mysqli_fetch_array($query2)) {
					echo "<ul>\n";
					$ssubgen = " ";
					if ($config['subgen']) {
						if (!empty($row2['S_SUBGENUS'])) {
							$ssubgen = " (".$row2['S_SUBGENUS'].") ";
						}
					}
					echo " = <i>".$row2['S_GENUS'].$ssubgen.$row2['S_SPECIES']."</i> ".$row2['S_S_AUTHOR'];
					if ($config['subsp']) {
						if (!empty($row2['S_SUBSP'])) {
							echo " ".strtolower($row2['S_RANK'])." <i>".$row2['S_SUBSP']."</i> ".$row2['S_SP_AUTHOR'];
						}
					}	
					echo " {".$row2['S_STATUS']."}";
					echo "</ul>\n";
				}
				mysqli_free_result($query2);
				echo "<br>\n";
			}
			break;
			
		case "descriptor":
			$sql = "SELECT COUNT(D_CHARACTER), (SELECT DISTINCT D_CHARACTER) FROM descriptors GROUP BY D_CHARACTER";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			echo "<h2>Taxon Descriptors</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"describe.php?ID=".$row[1]."\">$row[1]</a>.</td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "continent":
			if (ucfirst($environ) == "Marine") {
				echo "<h2>Oceans</h2>\n";
			}
			else {
				echo "<h2>Continents</h2>\n";
			}
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(P_CONTINENT), (SELECT DISTINCT P_CONTINENT) FROM distribution GROUP BY P_CONTINENT";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=distribution&field=P_CONTINENT&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." records)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;	
			
		case "region":
			echo "<h2>Regions</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(P_REGION), (SELECT DISTINCT P_REGION) FROM distribution GROUP BY P_REGION";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				if (strlen($row[1]) > 0) {
					echo "<tr><td>";
					echo "<a href=\"browse.php?start=0&class=distribution&field=P_REGION&filter=".$row[1]."\">";
					echo $row[1]."</a></td> <td>(".$row[0]." records)</td></tr>\n";
				}	
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;	
		  
		case "country":
			echo "<h2>Countries</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(P_COUNTRY), (SELECT DISTINCT P_COUNTRY) FROM distribution GROUP BY P_COUNTRY";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				if (strlen($row[1]) > 0) {
					echo "<tr><td>";
					echo "<a href=\"browse.php?start=0&class=distribution&field=P_COUNTRY&filter=".$row[1]."\">";
					echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
				}	
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "state":
			echo "<h2>States/Provinces</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(P_STATE), (SELECT DISTINCT P_STATE) FROM distribution GROUP BY P_STATE";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				if (strlen($row[1]) > 0) {
					echo "<tr><td>";
					echo "<a href=\"browse.php?start=0&class=distribution&field=P_STATE&filter=".$row[1]."\">";
					echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
				}	
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;	

		case "habitat":
			echo "<h2>Habitats</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(H_HABITAT), (SELECT DISTINCT H_HABITAT) FROM habitats GROUP BY H_HABITAT";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=habitats&field=H_HABITAT&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "use":
			echo "<h2>Uses</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(U_NAME), (SELECT DISTINCT U_NAME) FROM uses GROUP BY U_NAME";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=uses&field=U_NAME&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "common":
			echo "<h2>Vernacular names</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(V_NAME), (SELECT DISTINCT V_NAME) FROM commonnames GROUP BY V_NAME";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=commonnames&field=V_NAME&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
			
		case "status":
			echo "<h2>Conservation status</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(C_STATUS), (SELECT DISTINCT C_STATUS) FROM status GROUP BY C_STATUS";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"browse.php?start=0&class=status&field=C_STATUS&filter=".$row[1]."\">";
				echo $row[1]."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;

		case "genome":
			echo "<h2>Sequence descriptions</h2>\n";
			echo "<ul>\n";
			echo "<table class=\"browser\">\n";
			$sql = "SELECT COUNT(G_DESCRIPTION), (SELECT DISTINCT G_DESCRIPTION), (SELECT DISTINCT G_SEQ_TYPE) FROM genome GROUP BY G_DESCRIPTION ORDER BY G_SEQ_TYPE";
			$query = mysqli_query($link, $sql) or die("Error: MySQL query failed");
			while($row = mysqli_fetch_array($query)) {
				echo "<tr><td>";
				echo "<a href=\"sequence.php?filter=".$row[1]."&type=".$row[2]."\">";
				echo substr($row[1], 0, 128)."</a></td> <td>(".$row[0]." species)</td></tr>\n";
			}
			echo "</table>\n";
			echo "</ul>\n";
			break;
	}	

	mysqli_free_result($query);
	mysqli_close($link);
?>

<br>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>