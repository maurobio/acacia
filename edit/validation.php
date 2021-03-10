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

<script language="JavaScript" type="text/javascript" src="../library/functions.js">
</script>

<?php
	include_once("../library/functions.php");
	
	if ($this->tb == "taxa") {
		// Validate names
		if(!empty($newvals['T_GENUS'])) {
			$newvals['T_GENUS'] = ucfirst(chop($newvals['T_GENUS']));
		}

		if(!empty($newvals['T_SUBGENUS'])) {
			$newvals['T_SUBGENUS'] = ucfirst($newvals['T_SUBGENUS']);
		}

		if(!empty($newvals['T_SPECIES'])) {
			$newvals['T_SPECIES'] = strtolower(chop($newvals['T_SPECIES']));
		}

		if(!empty($newvals['T_SUBSP'])) {
			$newvals['T_SUBSP'] = strtolower(chop($newvals['T_SUBSP']));
		}
	}

	if ($this->tb == "synonyms") {
		// Validate synonyms
		if(!empty($newvals['S_GENUS'])) {
			$newvals['S_GENUS'] = ucfirst(chop($newvals['S_GENUS']));
		}

		if(!empty($newvals['S_SUBGENUS'])) {
			$newvals['S_SUBGENUS'] = ucfirst($newvals['S_SUBGENUS']);
		}

		if(!empty($newvals['S_SPECIES'])) {
			$newvals['S_SPECIES'] = strtolower(chop($newvals['S_SPECIES']));
		}

		if(!empty($newvals['S_SUBSP'])) {
			$newvals['S_SUBSP'] = strtolower(chop($newvals['S_SUBSP']));
		}
	}
	
	if ($this->tb == "highertaxa") {
	// Validate highertaxa
		if(!empty($newvals['T_KINGDOM'])) {
			$newvals['T_KINGDOM'] = ucfirst($newvals['T_KINGDOM']);
		}

		if(!empty($newvals['T_PHYLUM'])) {
			$newvals['T_PHYLUM'] = ucfirst($newvals['T_PHYLUM']);
		}

		if(!empty($newvals['T_SUBPHYLUM'])) {
			$newvals['T_SUBPHYLUM'] = ucfirst($newvals['T_SUBPHYLUM']);
		}

		if(!empty($newvals['T_CLASS'])) {
			$newvals['T_CLASS'] = ucfirst($newvals['T_CLASS']);
		}

		if(!empty($newvals['T_SUBCLASS'])) {
			$newvals['T_SUBCLASS'] = ucfirst($newvals['T_SUBCLASS']);
		}

		if(!empty($newvals['T_ORDER'])) {
			$newvals['T_ORDER'] = ucfirst($newvals['T_ORDER']);
		}

		if(!empty($newvals['T_SUBORDER'])) {
			$newvals['T_SUBORDER'] = ucfirst($newvals['T_SUBORDER']);
		}

		if(!empty($newvals['T_SUPERFAMILY'])) {
			$newvals['T_SUPERFAMILY'] = ucfirst($newvals['T_SUPERFAMILY']);
		}

		if(!empty($newvals['T_FAMILY'])) {
			$newvals['T_FAMILY'] = ucfirst($newvals['T_FAMILY']);
		}

		if(!empty($newvals['T_SUBFAMILY'])) {
			$newvals['T_SUBFAMILY'] = ucfirst($newvals['T_SUBFAMILY']);
		}

		if(!empty($newvals['T_TRIBE'])) {
			$newvals['T_TRIBE'] = ucfirst($newvals['T_TRIBE']);
		}
	}
	
	if ($this->tb == "commonnames") {
		// Validate vernacular names
		if(!empty($newvals['V_NAME'])) {
			$newvals['V_NAME'] = strtolower($newvals['V_NAME']);
		}
	}

	if ($this->tb == "bibliography") {
		// Validate citations
		if(!empty($newvals['B_AUTHOR'])) {
			$authors = explode(',', $newvals['B_AUTHOR']);
			if (count($authors) > 3) {
				$newvals['B_AUTHOR'] = $authors[0].','.$authors[1]." et al.";
			}
		}
	
		if(!empty($newvals['B_TITLE'])) {
			if (strlen($newvals['B_TITLE']) > 254) {
				$newvals['B_TITLE'] = ellipsify($newvals['B_TITLE'], 254);
			}
		}
	}	
	
	return true;
?>