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
  
	mysqli_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysqli_errno().": ".mysqli_error());
	mysql_select_db($config['dbname']);
	  
	$sort = $_GET['sort'];
	$filter = $_GET['filter'];
    $filename = $_GET['filename'];

	if (!isset($filename)) {
		// file name for download
		$filename = "bibliography.bib";

		header("Content-Disposition: attachment; filename=\"$filename\"");
		header("Content-Type: application/octet-stream;");

		$out = fopen("php://output", 'w');
	}	
	else {
		$out = fopen($filename, 'w'); 
	}

	$sql = "SELECT * FROM bibliography";
	if (isset($filter)) {
		$sql = $sql." WHERE ".$filter;
	}
	if (isset($sort)) {
		$sql = $sql." ORDER BY ".$sort;
	}
	$result = mysqli_query($sql) or die('Query failed!');
	
	while($row = mysqli_fetch_array($result)) {
		$entry = $row['B_TYPE'];
		$author = $row['B_AUTHOR'];
		$authors = explode(',', $row['B_AUTHOR']);
		if (count($authors) > 2) {
			$aux = implode(',', $authors);
			$aux = str_replace(".,", ". and ", $aux);
			$aux = str_replace('&', "and", $aux);
			$author = $aux;
		}
		while(substr_count($author,"  ") != 0){
			$author = str_replace("  "," ",$author);
		}
		$ptn = "/[,:]/";
		$str = $row['B_DETAIL'];
		$detail = preg_split($ptn, $str, -1, PREG_SPLIT_DELIM_CAPTURE);
		$pos = strrpos($detail[0], " ");
		$journal = trim(substr($detail[0], 0, $pos));
		$volume = trim(substr($detail[0], $pos));
		$pages = $detail[1];
		$key = str_replace(' ', '', $authors[0]).$row['B_YEAR'].$row['B_SEQUENCE'];
		switch (strtolower($entry)) {
			case "article":
				fwrite($out, "@ARTICLE{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  journal = {".$journal."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "  volume = {".$volume."},\n");
				fwrite($out, "  pages = {".$pages."},\n");
				fwrite($out, "}\n");
				break;
						
			case "book":
				fwrite($out, "@BOOK{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  publisher = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "booklet":
				fwrite($out, "@BOOKLET{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  howpublished = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "conference":
			case "inproceedings":
				fwrite($out, "@INPROCEEDINGS{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  booktitle = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "  volume = {".$volume."},\n");
				fwrite($out, "  pages = {".$pages."},\n");
				fwrite($out, "}\n");
				break;
						
			case "inbook":
				fwrite($out, "@INBOOK{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  pages = {".$pages."},\n");
				fwrite($out, "  publisher = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "  volume = {".$volume."},\n");
				fwrite($out, "}\n");
				break;
						
			case "incollection":
				fwrite($out, "@INCOLLECTION{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  booktitle = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "  volume = {".$volume."},\n");
				fwrite($out, "  pages = {".$pages."},\n");
				fwrite($out, "}\n");
				break;
						
			case "manual":
				fwrite($out, "@MANUAL{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  organization = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "mastersthesis":
				fwrite($out, "@MASTERSTHESIS{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  school = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "misc":
				fwrite($out, "@MISC{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  howpublished = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "phdthesis":
				fwrite($out, "@PHDTHESIS{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  school = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "proceedings":
				fwrite($out, "@PROCEEDINGS{".$key.",\n");
				fwrite($out, "  editor = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "  volume = {".$volume."},\n");
				fwrite($out, "  publisher = {".$row['B_DETAIL']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "techreport":
				fwrite($out, "@TECHREPORT{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  institution = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
						
			case "unpublished":
				fwrite($out, "@UNPUBLISHED{".$key.",\n");
				fwrite($out, "  author = {".$author."},\n");
				fwrite($out, "  title = {".$row['B_TITLE']."},\n");
				fwrite($out, "  note = {".$row['B_DETAIL']."},\n");
				fwrite($out, "  year = {".$row['B_YEAR']."},\n");
				fwrite($out, "}\n");
				break;
		}		
	}
	fclose($out);
	mysqli_free_result($result);
?>