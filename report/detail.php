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
<?php include("../library/functions.php"); ?>
<?php include "libchart/libchart/classes/libchart.php"; ?>

<?php
	$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	$selected = mysql_select_db($config['dbname']) or die("Could not select ".$config['dbname']);
	$sql = "SELECT * FROM metadata";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$title = mysql_result($query, 0, 'M_TITLE');
	$url = mysql_result($query, 0, 'M_URL');
	$pub = mysql_result($query, 0, 'M_PUBLISHER');
	$logo = mysql_result($query, 0, 'M_LOGO');
	$banner = mysql_result($query, 0, 'M_BANNER');
	$sql = "SELECT * FROM highertaxa";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed"); 
	$kingdom = mysql_result($query, 0, 'T_KINGDOM');
	$sql = "SELECT COUNT(*) FROM taxa WHERE LOWER(T_STATUS) = 'accepted'";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
	$num_spp = mysql_result($query, 0, 'COUNT(*)');
	$sql = "SELECT COUNT(*) FROM synonyms";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
	$num_nms = mysql_result($query, 0, 'COUNT(*)');
	$sql = "SELECT COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO AND LOWER(taxa.T_STATUS) = 'accepted'";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
	$num_rec = mysql_result($query, 0, 'COUNT(*)');
	$sql = "SELECT COUNT(*) FROM taxa, genome WHERE taxa.T_NO = genome.T_NO AND LOWER(taxa.T_STATUS) = 'accepted'";
	$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
	$num_seq = mysql_result($query, 0, 'COUNT(*)');
?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
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
		| <a href="stats.php" title="Database statistics">Statistics</a>
		| <a href="../help/about.php" title="Get help">About</a>
		]
		</td>
	</tr>	
</table>

<center><h3>Detailed Statistics</h3></center>
<ul>

<table class="report" width="50%">
<tbody>

<?php
	$sections = explode(":", $_GET['section']);
	$plot = $_GET['plot'];
	if ($plot == "true") {
		$plot = true;
	}
	else {
		$plot = false;
	}
	
	if ($plot) {
		if (!is_dir("generated/")) {
			mkdir("generated/");
		}
	}
	
	// Taxa
	if (in_array("taxa", $sections) || in_array("all", $sections)) {
		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">TAXA</th></tr>\n";
		echo "<th>\n";
		if (ucfirst($kingdom) == "Plantae") {
			echo "Divisions";
		} 
		else {
			echo "Phyla";
		}
		echo "</th>\n";
		echo "<th>Species</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT T_PHYLUM, COUNT(*) FROM taxa, highertaxa WHERE taxa.T_NO = highertaxa.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY highertaxa.T_PHYLUM ORDER BY highertaxa.T_PHYLUM";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\">".$row['T_PHYLUM']."</td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['T_PHYLUM'], $row['COUNT(*)']));
				}
			}
		}
		if ($plot) {
			$chart->setDataSet($dataSet);
			if (ucfirst($kingdom) == "Plantae") {
				$chart->setTitle("Divisions");
			}
			else {
				$chart->setTitle("Phyla");
			}
			$chart->render("generated/5.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/5.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}

		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><td colspan=\"3\"><hr></td></tr><tr>\n";
		echo "<th>Classes</th>\n";
		echo "<th>Species</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT T_CLASS, COUNT(*) FROM taxa, highertaxa WHERE taxa.T_NO = highertaxa.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY highertaxa.T_CLASS ORDER BY highertaxa.T_CLASS";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\">".$row['T_CLASS']."</td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['T_CLASS'], $row['COUNT(*)']));
				}
			}
		}
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Classes");
			$chart->render("generated/6.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/6.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}

		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><td colspan=\"3\"><hr></td></tr><tr>\n";
		echo "<th>Orders</th>\n";
		echo "<th>Species</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT T_ORDER, COUNT(*) FROM taxa, highertaxa WHERE taxa.T_NO = highertaxa.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY highertaxa.T_ORDER ORDER BY highertaxa.T_ORDER";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\">".$row['T_ORDER']."</td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['T_ORDER'], $row['COUNT(*)']));
				}
			}
		}
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Orders");
			$chart->render("generated/7.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/7.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}

		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
		echo "<tr>\n";
		echo "<th>Families</th>\n";
		echo "<th>Species</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT T_FAMILY, COUNT(*) FROM taxa, highertaxa WHERE taxa.T_NO = highertaxa.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY highertaxa.T_FAMILY ORDER BY highertaxa.T_FAMILY";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\">".strtoupper($row['T_FAMILY'])."</td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['T_FAMILY'], $row['COUNT(*)']));
				}
			}
		}
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Families");
			$chart->render("generated/8.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/8.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}
		
		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
		echo "<tr>\n";
		echo "<th>Genera</th>\n";
		echo "<th>Species</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT DISTINCT T_GENUS, COUNT(*) FROM taxa WHERE LOWER(T_STATUS) = 'accepted' GROUP BY T_GENUS ORDER BY T_GENUS";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\"><i>".$row['T_GENUS']."</i></td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['T_GENUS'], $row['COUNT(*)']));
				}
			}
		}
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Genera");
			$chart->render("generated/9.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/9.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}
		
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";	
	}

	// Names
	if (in_array("names", $sections) || in_array("all", $sections)) {
		if ($plot) {
			$chart = new PieChart();
			$dataSet = new XYDataSet();
		}
		echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">NAMES</th></tr>\n";
		echo "<tr>\n";
		echo "<th>Species</th>\n";
		echo "<th>Names</th>\n";
		echo "<th>%</th>\n";
		echo "</tr>\n";
		$sql = "SELECT CONCAT(taxa.T_GENUS, ' ', taxa.T_SPECIES, ' ', taxa.T_SUBSP) AS species, COUNT(*) FROM taxa, synonyms WHERE taxa.T_NO = synonyms.T_NO GROUP BY synonyms.T_NO ORDER BY species";
		$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
		$num_rows = mysql_num_rows($query);
		if ($num_rows > 0) {
			while($row = mysql_fetch_array($query)) {
				echo "<tr>";
				echo "<td align=\"left\"><i>".$row['species']."</i></td>";
				echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
				echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_nms)."</td></tr>\n";
				if ($plot) {
					$dataSet->addPoint(new Point($row['species'], $row['COUNT(*)']));
				}
			}
		}
		
		if ($plot) {
			$chart->setDataSet($dataSet);
			$chart->setTitle("Names");
			$chart->render("generated/10.png");
			echo "<td><p><img alt=\"Pie chart\" src=\"generated/10.png\" style=\"border: 1px solid gray;\"/></p></td>";
		}
		
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";	
	}

	// Common
	if (in_array("common", $sections) || in_array("all", $sections)) {
		if ($config['common']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">COMMON</th></tr>\n";
			echo "<tr>";
			echo "<th>Vernacular</th>";
			echo "<th>Species</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT V_NAME, V_LANGUAGE, COUNT(*) FROM taxa, commonnames WHERE taxa.T_NO = commonnames.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY V_NAME ORDER BY V_NAME";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['V_NAME']." (".$row['V_LANGUAGE'].") "."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['V_NAME'], $row['COUNT(*)']));
					}
				}
			}
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Common Names");
				$chart->render("generated/11.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/11.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Use</th>";
			echo "<th>Species</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT U_NAME, COUNT(*) FROM taxa, uses WHERE taxa.T_NO = uses.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY U_NAME ORDER BY U_NAME";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['U_NAME']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['U_NAME'], $row['COUNT(*)']));
					}
				}
			}
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Uses");
				$chart->render("generated/12.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/12.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
		}
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}
	
	// Descriptors
	if (in_array("descriptors", $sections) || in_array("all", $sections)) {
		if ($config['morph']) {
			$grpCheck = "";
			$grpEval = "";
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">DESCRIPTORS</th></tr>\n";
			echo "<tr>";
			echo "<th>Descriptor</th>";
			echo "<th>Type</th>";
			echo "<th>Statistics</th>";
			echo "</tr>\n";
			$sql = "SELECT * FROM descriptors ORDER BY D_CHARACTER, D_STATE";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					$grpCheck = $row['D_CHARACTER'];
					if ($grpEval != $grpCheck) {
						echo "<tr>\n";
						$grpEval = $grpCheck;
						echo "<td>".$grpCheck."</td>";
						echo "<td>".$row['D_CHAR_TYPE']."</td>";
						$sql = "SELECT COUNT(DISTINCT D_STATE), COUNT(DISTINCT T_NO) FROM descriptors WHERE D_CHARACTER = '$grpCheck'";
						$result = mysql_query($sql, $link) or die("Error: MySQL query failed");
						$row1 = mysql_fetch_row($result);
						$states = $row1[0];
						$items = $row1[1];
						$sql = "SELECT DISTINCT(D_STATE) FROM descriptors WHERE D_CHARACTER = '$grpCheck'";
						$result = mysql_query($sql, $link) or die("Error: MySQL query failed");
						if ($row['D_CHAR_TYPE'] == "Continuous" || $row['D_CHAR_TYPE'] == "Discrete") {
							$charlist = array();
							$i = 0;
							while($aux = mysql_fetch_array($result)) {
								$data = $aux['D_STATE'];
								$charlist[$i] = floatval($data);
								$i += 1;
							}
							echo "<td>";
							echo "Mean: ".number_format(mean($charlist), 3)."<br>";
							echo "Standard deviation: ".number_format(stdev($charlist), 3)."<br>";
							echo "Maximum: ".number_format(max($charlist), 2)."<br>";
							echo "Minimum: ".number_format(min($charlist), 2)."<br>";
						}
						elseif ($row['D_CHAR_TYPE'] == "Unordered" || $row['D_CHAR_TYPE'] == "Ordered") {
							$statelist = array();
							$i = 0;
							while($aux = mysql_fetch_array($result)) {
								$state = $aux['D_STATE'];
								$statelist[$i] = $state;
								$i += 1;
							}
							$freq = "";
							for ($j = 0; $j <= count($statelist); $j++) {
								$sql = "SELECT COUNT(*) FROM descriptors WHERE D_STATE = '$statelist[$j]'";
								$result = mysql_query($sql, $link) or die("Error: MySQL query failed");
								$row3 = mysql_fetch_row($result);
								if ($row3[0] > 0) {
									$freq = $freq." ".$statelist[$j]." (".$row3[0].");";
								}	
							}
							$freq = strslice($freq, 0, -1);
							echo "<td>";
							echo "Number of states: ".$states."<br>";
							echo "Frequency of states: ".$freq."<br>";
						}
						echo "Scored taxa: ".$items."</td>\n";
						echo "</tr>\n";
						echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
					}
				}
			}
		}		
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}

	if (in_array("sequences", $sections) || in_array("all", $sections)) {
		if ($config['gene']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">SEQUENCES</th></tr>\n";
			echo "<tr><th>Type</th>";
			echo "<th>Sequences</th>";
			echo "<th>%</th>";
			echo "</tr>\n";
			$sql = "SELECT G_SEQ_TYPE, COUNT(*) FROM taxa, genome WHERE taxa.T_NO = genome.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY G_SEQ_TYPE ORDER BY G_SEQ_TYPE";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".ucfirst($row['G_SEQ_TYPE'])."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_seq)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['G_SEQ_TYPE'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Biomolecular Sequences");
				$chart->render("generated/13.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/13.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			$sql = "SELECT DISTINCT T_NO FROM genome ORDER BY T_NO";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$value = mysql_num_rows($query);
			echo "<tr>";
			echo "<td align=\"left\">Sequenced Taxa</td>";
			echo "<td align=\"left\">".$value."</td>";
			echo "<td align=\"left\">".percent($value, $num_spp)."</td></tr>\n";
	
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Species</th>";
			echo "<th>Sequences</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT CONCAT(taxa.T_GENUS, ' ', taxa.T_SPECIES, ' ', taxa.T_SUBSP) AS species, COUNT(*) FROM taxa, genome WHERE taxa.T_NO = genome.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY species ORDER BY species";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\"><i>".$row['species']."</i></td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_rec)."</td></tr>\n";
				}
			}
			
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Sequence</th>";
			echo "<th>Length</th>";
			echo "<th>%GC</th>";
			echo "</tr>";
			$sql = "SELECT CONCAT(taxa.T_GENUS, ' ', taxa.T_SPECIES, ' ', taxa.T_SUBSP) AS species, G_SEQUENCE AS sequence, G_DESCRIPTION AS description FROM taxa, genome WHERE taxa.T_NO = genome.T_NO AND genome.G_SEQ_TYPE = 'Nucleotide' AND LOWER(taxa.T_STATUS) = 'accepted' ORDER BY species, sequence";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					$species = $row['species'];
					$seq = $row['sequence'];
					$desc = $row['description'];
					if (strlen($seq) > 0) {
						echo "<tr>";
						echo "<td align=\"left\"><i>".$species."</i><br>".$desc."</td>";
						echo "<td align=\"left\">".strlen($seq)."</td>";
						echo "<td align=\"left\">".GC_content($seq)."</td></tr>\n";
					}
				}
			}
		}
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}		

	// Geography
	if (in_array("geography", $sections) || in_array("all", $sections)) {
		if ($config['geog']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">GEOGRAPHY</th></tr>\n";
			echo "<tr>";
			echo "<th>Continent</th>";
			echo "<th>Ocurrences</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT P_CONTINENT, COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY P_CONTINENT ORDER BY P_CONTINENT";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['P_CONTINENT']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_rec)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['P_CONTINENT'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Continents");
				$chart->render("generated/14.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/14.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			if ($plot) {
				$chart = new PieChart(600, 300);
				$dataSet = new XYDataSet();
			}
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Region</th>";
			echo "<th>Ocurrences</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT P_REGION, COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY P_REGION ORDER BY P_REGION";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['P_REGION']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_rec)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['P_REGION'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Regions");
				$chart->render("generated/15.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/15.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Country</th>";
			echo "<th>Ocurrences</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT P_COUNTRY, COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY P_COUNTRY ORDER BY P_COUNTRY";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['P_COUNTRY']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_rec)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['P_COUNTRY'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Countries");
				$chart->render("generated/16.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/16.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr>";
			echo "<th>Species</th>";
			echo "<th>Ocurrences</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT CONCAT(taxa.T_GENUS, ' ', taxa.T_SPECIES, ' ', taxa.T_SUBSP) AS species, COUNT(*) FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY species ORDER BY species";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\"><i>".$row['species']."</i></td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_rec)."</td></tr>\n";
				}
			}
		}
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}

	// Ecology
	if (in_array("ecology", $sections) || in_array("all", $sections)) {
		if ($config['ecol']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">ECOLOGY</th></tr>\n";
			echo "<tr>";
			echo "<th>Habitat</th>";
			echo "<th>Species</th>";
			echo "<th>%</th>";
			echo "</tr>";
			$sql = "SELECT H_HABITAT, COUNT(*) FROM taxa, habitats WHERE taxa.T_NO = habitats.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY H_HABITAT ORDER BY H_HABITAT";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['H_HABITAT']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['H_HABITAT'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Habitats");
				$chart->render("generated/17.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/17.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
		}		
		echo "<tr>\n";
		echo "<td>&nbsp;</td>\n";
		echo "<td>&nbsp;</td></tr>\n";
	}

	// Conservation status
	if (in_array("status", $sections) || in_array("all", $sections)) {
		if ($config['status']) {
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><th colspan=\"3\" style=\"background:#ffffcc;\">CONSERVATION</th></tr>\n";
			echo "<tr><th>IUCN Categories</th>";
			echo "<th>Species</th>";
			echo "<th>%</th>";
			echo "</tr>\n";
			$sql = "SELECT C_STATUS, COUNT(*) FROM taxa, status WHERE taxa.T_NO = status.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY C_STATUS ORDER BY C_STATUS";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['C_STATUS']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['C_STATUS'], $row['COUNT(*)']));
					}
				}		
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Conservation Status");
				$chart->render("generated/18.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/18.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
	
			if ($plot) {
				$chart = new PieChart();
				$dataSet = new XYDataSet();
			}
			echo "<tr><td colspan=\"3\"><hr></td></tr>\n";
			echo "<tr><th>Population Trends</th>";
			echo "<th>Species</th>";
			echo "<th>%</th>";
			echo "</tr>\n";
			$sql = "SELECT C_TREND, COUNT(*) FROM taxa, status WHERE taxa.T_NO = status.T_NO AND LOWER(taxa.T_STATUS) = 'accepted' GROUP BY C_TREND ORDER BY C_TREND";
			$query = mysql_query($sql, $link) or die("Error: MySQL query failed");
			$num_rows = mysql_num_rows($query);
			if ($num_rows > 0) {
				while($row = mysql_fetch_array($query)) {
					echo "<tr>";
					echo "<td align=\"left\">".$row['C_TREND']."</td>";
					echo "<td align=\"left\">".$row['COUNT(*)']."</td>";
					echo "<td align=\"left\">".percent($row['COUNT(*)'], $num_spp)."</td></tr>\n";
					if ($plot) {
						$dataSet->addPoint(new Point($row['C_TREND'], $row['COUNT(*)']));
					}
				}
			}
			
			if ($plot) {
				$chart->setDataSet($dataSet);
				$chart->setTitle("Population Trends");
				$chart->render("generated/19.png");
				echo "<td><p><img alt=\"Pie chart\" src=\"generated/19.png\" style=\"border: 1px solid gray;\"/></p></td>";
			}
		}
	}
	
	mysql_free_result($query);
	mysql_close($link);
?>	

</tbody>
</table>
</ul>

<br>
<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>