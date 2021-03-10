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

<?php
	function ellipsify($string, $length, $mode="right") {
		if ($length and strlen($string) > $length) {
			switch ($mode) {
				case "right":
					return substr($string, 0, $length - 3)."...";
				case "left" :
					return "...".substr($string, strlen($string) - $length + 3);
				case "center":
					return substr($string, 0, floor($length / 2) - 2)."...".substr($string, floor($length / 2) + 1);
			}
		}
		return $string;
	}
	
	function strslice($str, $start, $end) {
		$end = $end - $start;
		return substr($str, $start, $end);
	}
	
	function percent($val1, $val2) {
		if ($val2 == 0) {
			$ret_val = 0;
		}
		elseif ($val2 == 1) {
			$ret_val = 100;
		}
		else {
			$ret_val = (floatval($val1) / floatval($val2)) * 100.00;
		}	
		return number_format($ret_val, 2);
	}

	function mean($nums) {
        $temp = 0;
        foreach($nums as $key => $val) {
                $temp += $val;
        }
        return  $temp / sizeof($nums);;
	}
	
	function variance($nums) {
        $n = count($nums);
        $mean = mean($nums);
        foreach($nums as $key => $val) {
                $temp += pow($val - $mean, 2);
        }
        return $temp/$n;
	}

	function stdev($nums) {
        return sqrt(variance($nums));
	}
	
	function GC_content($seq) {
        $number_of_G = substr_count($seq, "G");
        $number_of_C = substr_count($seq, "C");
        $gc_porcentaje = round(100 * ($number_of_G + $number_of_C) / strlen($seq), 2);
        return $gc_porcentaje;
	}
	
	function writeToKml($fname, $cond) {
		$outfile = fopen($fname, 'w');
		
		// Print the head of the document
		fprintf($outfile, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<kml xmlns=\"http://earth.google.com/kml/2.0\">\n");
		fprintf($outfile, "<Document>");
		
		$query = "SELECT P_LATITUDE, P_LONGITUDE FROM distribution WHERE P_LATITUDE IS NOT NULL AND P_LONGITUDE IS NOT NULL ORDER BY P_LATITUDE";
		$result = mysql_query($query) or die('Query failed!');
		$row = mysql_fetch_array($result);
	
		// Include a default map view using the following lines
		fprintf($outfile, " 
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
		if (!empty($cond)) {
			$query = $query." AND taxa.T_NO = ".$cond;
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
					$local = $local."No locality data available";
				}
				fprintf($outfile, "
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
		fprintf($outfile, "\n</Document>\n</kml>");
		fclose($outfile);
	}
?>