<?php 
	session_start(); 
	if(!isset($_SESSION['filter'])) {
		$_SESSION['filter'] = $_GET['filter'];
	}
	if(!isset($_SESSION['type'])) {
		$_SESSION['type'] = $_GET['type'];
	}
?>

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

// author    Joseba Bikandi
// license   GNU GPL v2
// source code available at biophp.org

//############################################################################
//################# Functions used in this script ############################
//############################################################################

function complement($seq){
    // change the sequence to upper case
    $seq = strtoupper ($seq);
    // the system used to get the complementary sequence is simple but fas
    $seq = str_replace("A", "t", $seq);
    $seq = str_replace("T", "a", $seq);
    $seq = str_replace("G", "c", $seq);
    $seq = str_replace("C", "g", $seq);
    $seq = str_replace("Y", "r", $seq);
    $seq = str_replace("R", "y", $seq);
    $seq = str_replace("W", "w", $seq);
    $seq = str_replace("S", "s", $seq);
    $seq = str_replace("K", "m", $seq);
    $seq = str_replace("M", "k", $seq);
    $seq = str_replace("D", "h", $seq);
    $seq = str_replace("V", "b", $seq);
    $seq = str_replace("H", "d", $seq);
    $seq = str_replace("B", "v", $seq);
    // change the sequence to upper case again for output
    $seq = strtoupper ($seq);
    return $seq;
}

function remove_non_coding($seq) {
    // change the sequence to upper case
    $seq = strtoupper($seq);
    // remove non-words (\W), con coding ([^ATGCYRWSKMDVHBN]) and digits (\d) from sequence
    $seq = preg_replace("/\W|[^ATGCYRWSKMDVHBN]|\d/","",$seq);
    // replace all X by N (to normalized sequences)
    $seq = preg_replace("/X/","N",$seq);
    return $seq;
}

function display_both_strands($seq) {
    // get the complementary sequence
    $revcomp = complement($seq);
    $result = "";
    $i = 0;
    while ($i < strlen($seq)){
        if (strlen($seq) < ($i + 70)) {$j = strlen($seq);} else {$j = $i;}
        $result .= substr($seq,$i,70)."\t$j\n";
        $result .= substr($revcomp,$i,70)."\t$j\n";
        $result .= "\n"; //line break
        $i += 70;
    }
    return $result;
}

function GC_content($seq) {
    $number_of_G = substr_count($seq,"G");
    $number_of_C = substr_count($seq,"C");
    $gc_porcentaje = round(100 * ($number_of_G + $number_of_C) / strlen($seq),2);
    return "G+C %: $gc_porcentaje\n\n";
}

function toRNA($seq) {
    // replace T by U
    $seq = preg_replace("/T/","U",$seq);
    $seq = chunk_split($seq, 70);
    return $seq;
}

function ACGT_content($seq) {
    $result = "Nucleotide composition";
    $result .= "\nA: ".substr_count($seq,"A");
    $result .= "\nC: ".substr_count($seq,"C");
    $result .= "\nG: ".substr_count($seq,"G");
    $result .= "\nT: ".substr_count($seq,"T");
    if (substr_count($seq,"Y")>0){$result .= "\nY: ".substr_count($seq,"Y");}
    if (substr_count($seq,"R")>0){$result .= "\nR: ".substr_count($seq,"R");}
    if (substr_count($seq,"W")>0){$result .= "\nW: ".substr_count($seq,"W");}
    if (substr_count($seq,"S")>0){$result .= "\nS: ".substr_count($seq,"S");}
    if (substr_count($seq,"K")>0){$result .= "\nK: ".substr_count($seq,"K");}
    if (substr_count($seq,"M")>0){$result .= "\nM: ".substr_count($seq,"M");}
    if (substr_count($seq,"D")>0){$result .= "\nD: ".substr_count($seq,"D");}
    if (substr_count($seq,"V")>0){$result .= "\nV: ".substr_count($seq,"V");}
    if (substr_count($seq,"H")>0){$result .= "\nH: ".substr_count($seq,"H");}
    if (substr_count($seq,"B")>0){$result .= "\nB: ".substr_count($seq,"B");}
    if (substr_count($seq,"N")>0){$result .= "\nN: ".substr_count($seq,"N");}
    $result .= "\n\n";
    return $result;
}

function remove_non_coding_prot($seq) {
    // change the sequence to upper case
    $seq = strtoupper($seq);
    // remove non-coding characters([^ARNDCEQGHILKMFPSTWYVX\*])
    $seq = preg_replace ("([^ARNDCEQGHILKMFPSTWYVX\*])", "", $seq);
    return $seq;
}

function protein_isoelectric_point($pK, $aminoacid_content) {
    // At isoelectric point, charge of protein will be 0
    // To calculate pH where charge is 0 a loop is required
    // The loop will start computing charge of protein at pH=7, and if charge is not 0, new charge value will be computed
    //    by using a different pH. Procedure will be repeated until charge is 0 (at isoelectric point)
    $pH = 7;          // pH value at start
    $delta = 4;       // this parameter will be used to modify pH when charge!=0. The value of $delta will change during the loop
    while(1) {
            // compute charge of protein at corresponding pH (uses a function)
            $charge = protein_charge($pK, $aminoacid_content, $pH);
            // check whether $charge is 0 (consecuentely, pH will be the isoelectric point
            if (round($charge, 4) == 0) {break;}
            // next line to check how computation is perform
            // print "<br>$charge\t$pH";
            // modify pH for next round
            if ($charge > 0) {$pH = $pH + $delta;} else {$pH = $pH - $delta;}
            // reduce value for $delta
            $delta = $delta/2;
    }
    // return pH at which charge=0 (the isoelectric point) with two decimals
    return round($pH, 2);
}

function partial_charge($val1, $val2) {
    // compute concentration ratio
    $cr = pow(10, $val1 - $val2);
    // compute partial charge
    $pc = $cr / ($cr + 1);
    return $pc;
}

// computes protein charge at corresponding pH
function protein_charge($pK, $aminoacid_content, $pH) {
    $charge = partial_charge($pK["N_terminus"], $pH);
    $charge += partial_charge($pK["K"], $pH) * $aminoacid_content["K"];
    $charge += partial_charge($pK["R"], $pH) * $aminoacid_content["R"];
    $charge += partial_charge($pK["H"], $pH) * $aminoacid_content["H"];
    $charge -= partial_charge($pH, $pK["D"]) * $aminoacid_content["D"];
    $charge -= partial_charge($pH, $pK["E"]) * $aminoacid_content["E"];
    $charge -= partial_charge($pH, $pK["C"]) * $aminoacid_content["C"];
    $charge -= partial_charge($pH, $pK["Y"]) * $aminoacid_content["Y"];
    $charge -= partial_charge($pH, $pK["C_terminus"]);
    return $charge;
}

function pK_values ($data_source) {
    // pK values for each component (aa)
    if ($data_source == "EMBOSS") {
            $pK = array(
                    "N_terminus" => 8.6,
                    "K" => 10.8,
                    "R" => 12.5,
                    "H" => 6.5,
                    "C_terminus" => 3.6,
                    "D" => 3.9,
                    "E" => 4.1,
                    "C" => 8.5,
                    "Y" => 10.1
            );
    }elseif ($data_source == "DTASelect"){
            $pK = array(
                    "N_terminus" => 8,
                    "K" => 10,
                    "R" => 12,
                    "H" => 6.5,
                    "C_terminus" => 3.1,
                    "D" => 4.4,
                    "E" => 4.4,
                    "C" => 8.5,
                    "Y" => 10
            );
    } elseif ($data_source == "Solomon") {
            $pK = array(
                    "N_terminus" => 9.6,
                    "K" => 10.5,
                    "R" => 125,
                    "H" => 6.0,
                    "C_terminus" => 2.4,
                    "D" => 3.9,
                    "E" => 4.3,
                    "C" => 8.3,
                    "Y" => 10.1
            );
    }
    return $pK;
}


function print_aminoacid_content($aminoacid_content) {
    $results = "";
    foreach($aminoacid_content as $aa => $count){
            $results .= "$aa\t".seq_1letter_to_3letter ($aa)."\t$count\n";
    }
    return $results;
}

function aminoacid_content($seq) {
    $array = array("A"=>0,"R"=>0,"N"=>0,"D"=>0,"C"=>0,"E"=>0,"Q"=>0,"G"=>0,"H"=>0,"I"=>0,"L"=>0,
                   "K"=>0,"M"=>0,"F"=>0,"P"=>0,"S"=>0,"T"=>0,"W"=>0,"Y"=>0,"V"=>0,"X"=>0,"*"=>0);
    for($i = 0; $i < strlen($seq); $i++){
            $aa = substr($seq, $i, 1);
            $array[$aa]++;
    }
    return $array;
}

function molar_absorption_coefficient_of_prot($seq, $aminoacid_content, $molweight) {
    // Prediction of the molar absorption coefficient of a protein
    // Pace et al. . Protein Sci. 1995;4:2411-23.
    $abscoef = ($aminoacid_content["A"] * 5500 + $aminoacid_content["Y"] * 1490 + $aminoacid_content["C"] * 125) / $molweight;
    return $abscoef;
}

// molecular weight calculation
function protein_molecular_weight ($seq, $aminoacid_content) {
    $molweight = $aminoacid_content["A"] * 71.07;         // for Alanine
    $molweight += $aminoacid_content["R"] * 156.18;        // for Arginine
    $molweight += $aminoacid_content["N"] * 114.08;        // for Asparagine
    $molweight += $aminoacid_content["D"] * 115.08;        // for Aspartic Acid
    $molweight += $aminoacid_content["C"] * 103.10;        // for Cysteine
    $molweight += $aminoacid_content["Q"] * 128.13;        // for Glutamine
    $molweight += $aminoacid_content["E"] * 129.11;        // for Glutamic Acid
    $molweight += $aminoacid_content["G"] * 57.05;         // for Glycine
    $molweight += $aminoacid_content["H"] * 137.14;        // for Histidine
    $molweight += $aminoacid_content["I"] * 113.15;        // for Isoleucine
    $molweight += $aminoacid_content["L"] * 113.15;        // for Leucine
    $molweight += $aminoacid_content["K"] * 128.17;        // for Lysine
    $molweight += $aminoacid_content["M"] * 131.19;        // for Methionine
    $molweight += $aminoacid_content["F"] * 147.17;        // for Phenylalanine
    $molweight += $aminoacid_content["P"] * 97.11;         // for Proline
    $molweight += $aminoacid_content["S"] * 87.07;         // for Serine
    $molweight += $aminoacid_content["T"] * 101.10;        // for Threonine
    $molweight += $aminoacid_content["W"] * 186.20;        // for Tryptophan
    $molweight += $aminoacid_content["Y"] * 163.17;        // for Tyrosine
    $molweight += $aminoacid_content["Z"] * 99.13;         // for Valine
    $molweight += 18.02;                     // water
    $molweight += $aminoacid_content["X"] * 114.822;       // for unkwon aminoacids, add avarage of all aminoacids
    return $molweight;
}

// this function has not been used in this script, but may be interesting for you
function identify_aminoacid_complete_name ($aa) {
    $aa = strtoupper($aa);
    if (strlen($aa) == 1) {
            if ($aa == "A") {return "Alanine";}
            if ($aa == "R") {return "Arginine";}
            if ($aa == "N") {return "Asparagine";}
            if ($aa == "D") {return "Aspartic Acid";}
            if ($aa == "C") {return "Cysteine";}
            if ($aa == "E") {return "Glutamic Acid";}
            if ($aa == "Q") {return "Glutamine";}
            if ($aa == "G") {return "Glycine";}
            if ($aa == "H") {return "Histidine";}
            if ($aa == "I") {return "Isoleucine";}
            if ($aa == "L") {return "Leucine";}
            if ($aa == "K") {return "Lysine";}
            if ($aa == "M") {return "Methionine";}
            if ($aa == "F") {return "Phenylalanine";}
            if ($aa == "P") {return "Proline";}
            if ($aa == "S") {return "Serine";}
            if ($aa == "T") {return "Threonine";}
            if ($aa == "W") {return "Tryptophan";}
            if ($aa == "Y") {return "Tyrosine";}
            if ($aa == "V") {return "Valine";}
    } elseif (strlen($aa) == 3) {
            if ($aa == "ALA") {return "Alanine";}
            if ($aa == "ARG") {return "Arginine";}
            if ($aa == "ASN") {return "Asparagine";}
            if ($aa == "ASP") {return "Aspartic Acid";}
            if ($aa == "CYS") {return "Cysteine";}
            if ($aa == "GLU") {return "Glutamic Acid";}
            if ($aa == "GLN") {return "Glutamine";}
            if ($aa == "GLY") {return "Glycine";}
            if ($aa == "HIS") {return "Histidine";}
            if ($aa == "ILE") {return "Isoleucine";}
            if ($aa == "LEU") {return "Leucine";}
            if ($aa == "LYS") {return "Lysine";}
            if ($aa == "MET") {return "Methionine";}
            if ($aa == "PHE") {return "Phenylalanine";}
            if ($aa == "PRO") {return "Proline";}
            if ($aa == "SER") {return "Serine";}
            if ($aa == "THR") {return "Threonine";}
            if ($aa == "TRP") {return "Tryptophan";}
            if ($aa == "TYR") {return "Tyrosine";}
            if ($aa == "VAL") {return "Valine";}
    }
}

function seq_1letter_to_3letter ($seq) {
    $seq = chunk_split($seq, 1, '#');
    $seq = chunk_split($seq, 40);
    for($i = 0; $i < strlen($seq); $i++) {
           $seq = preg_replace("(A\#)", "Ala", $seq);
           $seq = preg_replace("(R\#)", "Arg", $seq);
           $seq = preg_replace("(N\#)", "Asp", $seq);
           $seq = preg_replace("(D\#)", "Asn", $seq);
           $seq = preg_replace("(C\#)", "Cys", $seq);
           $seq = preg_replace("(E\#)", "Glu", $seq);
           $seq = preg_replace("(Q\#)", "Gln", $seq);
           $seq = preg_replace("(G\#)", "Gly", $seq);
           $seq = preg_replace("(H\#)", "His", $seq);
           $seq = preg_replace("(I\#)", "Ile", $seq);
           $seq = preg_replace("(L\#)", "Leu", $seq);
           $seq = preg_replace("(K\#)", "Lys", $seq);
           $seq = preg_replace("(M\#)", "Met", $seq);
           $seq = preg_replace("(F\#)", "Phe", $seq);
           $seq = preg_replace("(P\#)", "Pro", $seq);
           $seq = preg_replace("(S\#)", "Ser", $seq);
           $seq = preg_replace("(T\#)", "Thr", $seq);
           $seq = preg_replace("(W\#)", "Trp", $seq);
           $seq = preg_replace("(Y\#)", "Tyr", $seq);
           $seq = preg_replace("(V\#)", "Val", $seq);
           $seq = preg_replace("(X\#)", "XXX", $seq);
           $seq = preg_replace("(\*\#)", "*** ", $seq);
    }
    return $seq;
}

function protein_aminoacid_nature1($seq) {
    $result = "";
    for($i = 0; $i < strlen($seq); $i++) {
            // non-polar aminoacids, magenta
            if (strpos(" GAPVILFM", substr($seq, $i, 1)) > 0) {$result .= "<font color=yellow>".substr($seq, $i, 1)."</font>"; continue;}
            // polar aminoacids, magenta
            if (strpos(" SCTNQHYW", substr($seq, $i, 1)) > 0) {$result .= "<font color=magenta>".substr($seq, $i, 1)."</font>"; continue;}
            // charged aminoacids, red
            if (strpos(" DEKR", substr($seq, $i, 1)) > 0) {$result .= "<font color=red>".substr($seq, $i, 1)."</font>"; continue;}
    }
    return $result;
}

function protein_aminoacid_nature2($seq) {
    $result = "";
    for($i = 0; $i < strlen($seq); $i++) {
            // Small nonpolar (yellow)
            if (strpos(" GAST", substr($seq, $i, 1)) > 0) {$result .= "<font color=yellow>".substr($seq, $i, 1)."</font>"; continue;}
            // Small hydrophobic (green)
            if (strpos(" CVILPFYMW", substr($seq, $i, 1)) > 0) {$result .= "<font color=green>".substr($seq, $i, 1)."</font>"; continue;}
            // Polar
            if (strpos(" DQH", substr($seq, $i, 1)) > 0) {$result .= "<font color=magenta>".substr($seq, $i, 1)."</font>"; continue;}
            // Negatively charged
            if (strpos(" NE", substr($seq, $i, 1)) > 0) {$result .= "<font color=red>".substr($seq, $i, 1)."</font>"; continue;}
            // Positively charged
            if (strpos(" KR", substr($seq, $i, 1)) > 0) {$result.="<font color=red>".substr($seq, $i, 1)."</font>"; continue;}
    }
    return $result;
}

// Chemical group/aminoacids:
//   L/GAVLI       Amino Acids with Aliphatic R-Groups
//   H/ST          Non-Aromatic Amino Acids with Hydroxyl R-Groups
//   M/NQ          Acidic Amino Acids
//   R/FYW         Amino Acids with Aromatic Rings
//   S/CM          Amino Acids with Sulfur-Containing R-Groups
//   I/P           Imino Acids
//   A/DE          Acidic Amino Acids
//   C/KRH         Basic Amino Acids
//   */*
//   X/X
function protein_aminoacids_chemical_group($amino_seq){
    $chemgrp_seq = "";
    $ctr = 0;
    while(1)
            {
            $amino_letter = substr($amino_seq, $ctr, 1);
            if ($amino_letter == "") break;
            if (strpos(" GAVLI", $amino_letter) > 0) $chemgrp_seq .= "L";
            elseif (($amino_letter == "S") or ($amino_letter == "T")) $chemgrp_seq .= "H";
            elseif (($amino_letter == "N") or ($amino_letter == "Q")) $chemgrp_seq .= "M";
            elseif (strpos(" FYW", $amino_letter) > 0) $chemgrp_seq .= "R";
            elseif (($amino_letter == "C") or ($amino_letter == "M")) $chemgrp_seq .= "S";
            elseif ($amino_letter == "P") $chemgrp_seq .= "I";
            elseif (($amino_letter == "D") or ($amino_letter == "E")) $chemgrp_seq .= "A";
            elseif (($amino_letter == "K") or ($amino_letter == "R") or ($amino_letter == "H"))
                    $chemgrp_seq .= "C";
            elseif ($amino_letter == "*") $chemgrp_seq .= "*";
            elseif ($amino_letter == "X" or $amino_letter == "N") $chemgrp_seq .= "X";
            else die("Invalid amino acid symbol in input sequence.");
            $ctr++;
            }
    return $chemgrp_seq;
}

//############################################################################
//############################### End of fuctions ############################
//############################################################################

?>

<?php include("../config.php"); ?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
		"http://www.w3.org/TR/html4/loose.dtd">
		
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
	<?php echo "<title>".$title." - Search Results</title>"; ?>
	<link rel="stylesheet" href="../library/stylesheet.css" type="text/css">
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

<center>
<?php 
	$filter = $_SESSION['filter'];
	$type = $_SESSION['type'];
	
	if ($type == "Nucleotide") {
		echo "<h3>DNA Sequence Manipulation</h3>";
		echo "<h3>Your query: "; 
		echo ".....      DNA sequence equals '".$filter."'";
		echo "</h3>\n";
		
		$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
		mysql_select_db($config['dbname']);
		$sql = "SELECT G_SEQUENCE FROM genome WHERE G_DESCRIPTION LIKE '%".$filter."%'";
		$query = mysql_query($sql, $link) or die("Error: ". mysql_error());
		$seq = mysql_result($query, 0, 'G_SEQUENCE');
		
		//$seq = $_POST["seq"];
		$action = $_POST["action"];

		// remove non coding (works by default)
		$seq = remove_non_coding($seq);

		// if subsequence is requested
		if ($_POST["start"] or $_POST["end"]) {
			if ($_POST["start"] != "") {$start = $_POST["start"] - 1;} else {$start = 0;}
			if ($_POST["end"] != "") {$end = $_POST["end"];} else {$end = strlen($seq);}
			$seq = substr($seq, $start, $end - $start);
		}

		// length of sequence
		$seqlen = strlen($seq);

		if ($action == "reverse") {
            // reverse the sequence
            $seq = strrev($seq);
		}

		if ($action == "complement") {
            // get the complementary sequence
            $seq = complement($seq);
		}

		if ($action == "reverse_and_complement") {
            // reverse the sequence
            $seq = strrev($seq);
            // get the complementary sequence
            $seq = complement($seq);
		}

		$result = "";
		if ($action == "display_both_strands") {
           // get a string with results
           $result = display_both_strands($seq);
		}
        
		if ($action == "toRNA") {
           // get a string with results
            $result = toRNA($seq);
		}

		if ($_POST["GC"] == 1) {
           // calculate G+C content
           $result .= GC_content($seq);
		}
        
		if ($_POST["ACGT"] == 1) {
            // calculate nucleotide conposition
            $result .= ACGT_content($seq);
		}

		// 70 characters per line before output
		$seq = chunk_split($seq, 70);
?>
		<form method="post" action="<? print $_SERVER["PHP_SELF"]; ?>">
			<table cellpadding="5" width="650" border="0" bgcolor="DDFFFF">
			<tr><td>
                <b>Sequence <?php if ($seq) {print "($seqlen bp)";} ?>:</b>
			</td></tr>
			<tr><td>
               <textarea name="seq" rows="8" cols="80" readonly="readonly"><?php print $seq ?></textarea>
			</td></tr>
			<tr><td>
                <select name="action" size="6">
					<option value="remove_non_coding">Remove no coding characters
					<option value="reverse">Reverse sequence
					<option value="complement">Complement sequence
					<option value="reverse_and_complement">Reverse and Complement of sequence
					<option value="display_both_strands">Display Double-stranded Sequence
					<option value="toRNA">Convert to RNA
                </select>
                <br><br>Select subsequence from position <input type="text" name="start" size="4"> to <input type="text" name="end" size="4"> (both included)
             </td></tr>
			 <tr><td>
                <input type="checkbox" name="GC" value="1"> G+C content
                <br><input type="checkbox" name="ACGT" value="1"> Nucleotide composition
			  </td></tr>

			 <tr><td align="center">
                <input type="submit" value="Submit">
             </td></tr>
             <?php
					if ($other_results != "") {
						print "<tr><td align=\"center\">\n";
						print "<textarea rows=\"10\" cols=\"80\">$other_results</textarea>\n";
						print "</td></tr>\n";
					}
			 ?>
         </table>
		</form>
        <table cellpadding="5" width="650" border="0">
           <tr><td>
           <pre><?php print $result; ?></pre>
           </td></tr>
           <tr><td>
           <b>NOTES</b>:
           <br>Non-coding characters will be removed by default, and X is replaced by N.
           <br><a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=retrieve&db=pubmed&list_uids=7957164&dopt=abstract">NC-UIBMB</a>
           codes are used as a reference.
		   <p>Powered by <a href="http://www.biophp.org/">BioPHP.org</a><br>
		   </td></tr>
        </table>
		
<?php		
	} else if ($type == "Protein") {
		echo "<h3>Protein Sequence Information</h3>";
		echo "<h3>Your query: "; 
		echo ".....      Protein sequence equals '".$filter."'";
		echo "</h3>\n";
		
		$link = mysql_connect($config['host'], $config['user'], $config['pwd']) or die("Connection error: ".mysql_errno().": ".mysql_error());
		mysql_select_db($config['dbname']);
		$sql = "SELECT G_SEQUENCE FROM genome WHERE G_DESCRIPTION LIKE '%".$filter."%'";
		$query = mysql_query($sql, $link) or die("Error: ". mysql_error());
		$seq = mysql_result($query, 0, 'G_SEQUENCE');
		
		//$seq=$_POST["seq"];
        $pH = $_POST["pH"];
        $data_source = $_POST["data_source"];
        $result = "";

        // remove non coding (works by default)
        $seq = remove_non_coding_prot($seq);

        // we will save the original sequence, just in case subsequence is used
        $original_seq = chunk_split($seq, 70);
        // if subsequence is requested
        if ($_POST["start"] or $_POST["end"]){
            if ($_POST["start"] != "") {$start = $_POST["start"] - 1;} else {$start = 0;}
            if ($_POST["end"] != "") {$end = $_POST["end"];} else {$end = strlen($seq);}
            $seq = substr($seq, $start, $end - $start);
            $result .= "<p><b>Subsequence used for calculations:</b><br>".chunk_split($seq, 70);
        }

        // length of sequence
        $seqlen = strlen($seq);

        // compute requested parameter
        if ($_POST["composition"] == 1 or $_POST["molweight"] == 1 or $_POST["abscoef"] == 1 or $_POST["charge"] == 1 or $_POST["charge2"] == 1) {
            // calculate nucleotide conposition
            $aminoacid_content = aminoacid_content($seq);
            // prepare nucleotide composition to be printed out
            if ($_POST["composition"] == 1) {
                $result .= "<p><b>Aminoacid composition of protein:</b><br>".print_aminoacid_content($aminoacid_content);
            }
        }

        if ($_POST["molweight"] == 1 or $_POST["abscoef"] == 1) {
            // calculate molecular weight of protein
            $molweight = protein_molecular_weight($seq, $aminoacid_content);
            if ($_POST["molweight"] == 1) {
                $result .= "<p><b>Molecular weight:</b><br>$molweight Daltons";
            }
        }

        if ($_POST["abscoef"] == 1) {
            $abscoef = molar_absorption_coefficient_of_prot($seq, $aminoacid_content, $molweight);
            $result .= "<p><b>Molar Absorption Coefficient at 280 nm:</b><br>".round($abscoef, 2);
        }

        if ($_POST["charge"] == 1) {
            // get pk values for charged aminoacids
            $pK = pK_values ($data_source);
            // calculate isoelectric point of protein
            $charge = protein_isoelectric_point($pK, $aminoacid_content);
            $result .= "<p><b>Isoelectric point of sequence ($data_source):</b><br>".round($charge, 2);
        }
		
        if ($_POST["charge2"] == 1) {
            // get pk values for charged aminoacids
            $pK = pK_values ($data_source);
            // calculate charge of protein at requested pH
            $charge = protein_charge($pK, $aminoacid_content, $pH);
            $result .= "<p><b>Charge of sequence at pH = $pH ($data_source):</b><br>".round($charge, 2);
        }

         // colored sequence based in plar/non-plar/charged aminoacids
        if ($_POST["3letters"] == 1) {
            // get the colored sequence (html code)
            $three_letter_code = seq_1letter_to_3letter($seq);
            // add to result
            $result .= "<p><b>Sequence as three letters aminoacid code:</b><br>".$three_letter_code;

        }
		
        // 50 characters per line before output
        $seq = chunk_split($seq, 70);

        // colored sequence based in polar/non-plar/charged aminoacids
        if ($_POST["type1"] == 1) {
            // get the colored sequence (html code)
            $colored_seq = protein_aminoacid_nature1($seq);
            // add to result
            $result .= "<p><b><font color=Magenta>Polar</font>, <font color=yellow>Nonpolar</font></b> or <b><font color=red>Charged</font></b> aminoacids:<br>".$colored_seq;

        }
		
        // colored sequence based in polar/non-plar/charged aminoacids
        if ($_POST["type2"] == 1) {
            // get the colored sequence (html code)
            $colored_seq = protein_aminoacid_nature2($seq);
            // add to result
            $result .= "<p><b><font color=magenta>Polar</font>, <font color=yellow>small non-polar</font>, <font color=green>hydrophobic</font>, <font color=red>negatively</font></b> or <b><font color=blue>positively</font> charged</b> aminoacids:<br>".$colored_seq;

        }
//	}
?>
		<form method="post" action="<? print $_SERVER["PHP_SELF"]; ?>">
         <table cellpadding="5" width="650" border="0" bgcolor="DDFFFF">
           <tr><td>
                <b>Sequence <?php if($seq) {print "(length: $seqlen)";} ?>:</b>
           </td></tr>
           <tr><td>
               <textarea name="seq" rows="4" cols="80" readonly="readonly"><?php print $original_seq; ?></textarea>
           </td></tr>
           <tr><td>
                Select subsequence from position <input type="text" name="start" size="4" value="<? if ($_POST["start"] == 1) {print $_POST["start"];} ?>"> to <input type="text" name="end" size="4" value="<? if ($_POST["end"]==1) {print $_POST["end"];} ?>"> (both included) for computation
                <hr><input type="checkbox" name="composition" value="1"<? if ($_POST["composition"] == 1) {print " checked";} ?>>Aminoacid composition
                <br><input type="checkbox" name="molweight" value="1"<? if ($_POST["molweight"] == 1) {print " checked";} ?>>Molecular weight
                <br><input type="checkbox" name="abscoef" value="1"<? if ($_POST["abscoef"] == 1) {print " checked";} ?>>Molar absorption coefficient
                <br><input type="checkbox" name="charge" value="1"<? if ($_POST["charge"] == 1) {print " checked";} ?>>Protein isoelectric point with pK values from
                        <select name="data_source">
                             <option><? if ($data_source == "EMBOSS") {print " selected";} ?>EMBOSS
                             <option><? if ($data_source == "DTASelect") {print " selected";} ?>DTASelect
                             <option><? if ($data_source == "Solomon") {print " selected";} ?>Solomon
                        </select>
                <br><input type="checkbox" name="charge2" value="1"<? if ($_POST["charge2"] == 1) {print " checked";} ?>>Charge at pH = <input type=text name=pH value="<? print $pH ?>" size=4>
                <br><input type="checkbox" name="3letters" value="1"<? if ($_POST["3letters"] == 1) {print " checked";} ?>>Show sequence as 3 letters aminoacid code
                <br><input type="checkbox" name="type1" value="1"<? if ($_POST["type1"] == 1) {print " checked";} ?>>Show polar, non-polar and charged nature of aminoacids
                <br><input type="checkbox" name="type2" value="1"<? if ($_POST["type2"] == 1) {print " checked";} ?>>Show polar, non-polar, Hydrofobic, and negatively or positively charged nature of aminoacids
           </td></tr>
           <tr><td align="center">
                <input type="submit" value="Submit">
           </td></tr>
           <?php if($result != "") {print "<tr><td bgcolor=FFFFFF><pre>$result</pre></td></tr>";} ?>
		   <tr><td>
           <b>NOTES</b>:
           <br>Non-coding characters will be removed by default.
		   <br><a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?cmd=retrieve&db=pubmed&list_uids=7957164&dopt=abstract">NC-UIBMB</a> codes are used as a reference.
		   <p>Powered by <a href="http://www.biophp.org/">BioPHP.org</a><br>
		   </td></tr>
         </table>
		</form>
<?php
}
?>	
</center>

<hr>
<p align="center">
<a class="footer" href="http://sites.google.com/site/acaciadb/" target=_blank>Powered by ACACIA</a></p>
</body>
</html>