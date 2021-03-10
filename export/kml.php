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
	
	mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
	mysql_select_db($config['dbname']);

	/*$filter = $_GET['filter'];
	$filename = $_GET['filename'];*/
	
	if (!isset($filename)) {
		// file name for download
		$filename = "distribution.kml";
	
		// Important so that the filetype is correct
		header("Content-Disposition: attachment; filename=\"$filename\"");
		header("Content-type: application/xml;");
	
		$out = fopen("php://output", 'w');
	}
	else {
		$out = fopen($filename, 'w');
	}

	// Print the head of the document
	fprintf($out, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://earth.google.com/kml/2.0\">\n");
	fprintf($out, "<Document>");
		
	$query = "SELECT P_LATITUDE, P_LONGITUDE FROM distribution WHERE P_LATITUDE IS NOT NULL AND P_LONGITUDE IS NOT NULL ORDER BY P_LATITUDE";
	$result = mysql_query($query) or die('Query failed!');
	$row = mysql_fetch_array($result);
	
	// Include a default map view using the following lines
	fprintf($out, " 
	<LookAt>
		<latitude>%d</latitude>
		<longitude>%d</longitude>
		<altitude>0</altitude>
		<range>3600000</range>
		<tilt>0</tilt>
		<heading>0</heading>
		<altitudeMode>relativeToGround</altitudeMode>
	</LookAt>",
	htmlspecialchars($row['P_LATITUDE']),
	htmlspecialchars($row['P_LONGITUDE'])
	);
	mysql_free_result($result);	
	
	$query = "SELECT * FROM taxa, distribution WHERE taxa.T_NO = distribution.T_NO";
	if (isset($filter)) {
		$sql = $sql." AND ".$filter;
	}
	$result = mysql_query($query) or die('Query failed!');

	// Iterate over all placemarks (rows)
	while ($row = mysql_fetch_object($result)) {

		// This writes out a placemark with some data
		if (($row->P_LONGITUDE != 0.0) && ($row->P_LATITUDE != 0.0)) {
			$local = $row->P_COUNTRY.": ";
			if (strlen($row->P_STATE) > 0) {
				$local = $local.$row->P_STATE.", ";
			}
			if (strlen($row->P_LOCALITY) > 0) {
				$local = $local.$row->P_LOCALITY;
			}
			else {
				$local = $local."No locality data";
			}
			fprintf($out, "
	<Placemark id=\"%d\">
		<name>%s %s</name>
		<description>%s</description>
		<Point>
			<coordinates>%f,%f</coordinates>
		</Point>
	</Placemark>",
			htmlspecialchars($row->T_NO),
			htmlspecialchars($row->T_GENUS),
			htmlspecialchars($row->T_SPECIES),
			htmlspecialchars($local),
			//htmlspecialchars($row->P_LOCALITY),
			htmlspecialchars($row->P_LONGITUDE),
			htmlspecialchars($row->P_LATITUDE) );
		}		
	};
	
	mysql_free_result($result);
	fprintf($out, "\n</Document>\n</kml>");
	fclose($out);	
?>