/*
#=================================================================================#
#       Acacia - A Generic Conceptual Schema for Taxonomic Databases              #
#                 Copyright 2008-2012 Mauro J. Cavalcanti                         #
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
*/

function enableControls() {
	if (document.reportform.all.checked) {
		uncheckAll(document.reportform.section);
	}
	else {
		checkAll(document.reportform.section);
	}
}

function checkAll(field) {
	for (i = 0; i < field.length; i++) {
		field[i].disabled = false;
		field[i].checked = true;
	}
}

function uncheckAll(field) {
	for (i = 0; i < field.length; i++) {
		field[i].checked = false;
		field[i].disabled = true;
	}	
}

function getOption() {
	var options = "";
	for (var i = 0; i < document.reportform.stats.length; i++) {
		if (document.reportform.stats[i].checked) {
			var selection = document.reportform.stats[i].value;
		}
	}
	if (document.reportform.charts.checked) {
		var plot = document.reportform.charts.value;
	}
	else {
		var plot = "false";
	}
	for (var i = 0; i < document.reportform.section.length; i++) {
		if (document.reportform.section[i].checked) {
			options += document.reportform.section[i].value + ":";
		}
	}
	if (options.length == 0) {
		options = "section=all";
	}
	else {
		options = "section=" + options.slice(0, -1);
	}
	if (selection == "summary") {
		var url = "summary.php?" + options + "&plot=" + plot;
	}
	else if (selection == "detail") {
		var url = "detail.php?" + options + "&plot=" + plot;
	}
	window.open(url, "_self");
	return true;
}

function selectOption() {
	var option = document.searchform.searchfield.selectedIndex;
	var selection = document.searchform.searchfield.options[option].value;
	var fieldvalue = document.searchform.searchterm.value;
	switch (selection) {
		case "division": 
			var dataclass = "highertaxa";
			var fieldname = "T_PHYLUM";
			break;
		case "class": 
			var dataclass = "highertaxa";
			var fieldname = "T_CLASS";
			break;
		case "order": 
			var dataclass = "highertaxa";
			var fieldname = "T_ORDER";
			break;	
		case "family":
			var dataclass = "highertaxa";
			var fieldname = "T_FAMILY";
			break;
		case "genus": 
			var dataclass = "taxa";
			var fieldname = "T_GENUS";
			break;
		case "species": 
			break;
		case "epithet": 
			var dataclass = "taxa";
			var fieldname = "T_SPECIES";
			break;
		case "continent": 
			var dataclass = "distribution";
			var fieldname = "P_CONTINENT";
			break;	
		case "region": 
			var dataclass = "distribution";
			var fieldname = "P_REGION";
			break;
		case "country": 
			var dataclass = "distribution";
			var fieldname = "P_COUNTRY";
			break;
		case "state": 
			var dataclass = "distribution";
			var fieldname = "P_STATE";
			break;
		case "habitat": 
			var dataclass = "habitats";
			var fieldname = "H_HABITAT";
			break;
		case "common": 
			var dataclass = "commonnames";
			var fieldname = "V_NAME";
			break;
		case "use": 
			var dataclass = "uses";
			var fieldname = "U_NAME";
			break;
		case "status": 
			var dataclass = "status";
			var fieldname = "C_STATUS";
			break;
		case "descriptor": 
			var dataclass = "descriptors";
			var fieldname = "D_CHARACTER";
		case "nucleotide": 
			break;
		case "protein": 
			break;
	}
	if (selection == "species") {
		var url = "species.php?name=" + fieldvalue; 
	}
	else if (selection == "nucleotide") {
		var url = "sequence.php?filter=" + fieldvalue + "&type=nucleotide";
	}
	else if (selection == "protein") {
		var url = "sequence.php?filter=" + fieldvalue + "&type=protein";
	}
	else {
		var url = "browse.php?class=" + dataclass + "&field=" + fieldname + "&filter=" + fieldvalue;
	}	
	window.open(url, "_self");
	return true;
}

function validateTextSearch() {
  if (document.searchform.searchterm.value == "") {
    alert("Please enter search terms into the text box.");
    document.searchform.searchterm.focus();
    return false;
  } 
  else {
  	var search_pattern = /^\s*[A-Za-z]+(\s+[A-Za-z]+)*\s*$/;
   	if (search_pattern.test(document.searchform.searchterm.value)) {
	  	var result = true;
   	 	return result;
   	 }
   	 else {
		alert("Not a valid entry");
		document.searchform.searchterm.focus();
		return false;
   	 }
  }
}

function displayMap() {
	//var index = document.searchform.searchname.selectedIndex;
	//var searchvalue = document.searchform.searchname.options[index].text;
	//if (document.searchform.track.checked)
	//	var track = true;
	//else
	//	var track = false;
	//var url = "display.php?name=" + searchvalue + "&track=" + track;
	var url = "display.php?";
	for (var i = 0; i < document.searchform.searchname.length; i++) {
		if (document.searchform.searchname.options[i].selected) {
			var str = document.searchform.searchname.options[i].text;
			//url += "name[]=" + document.searchform.searchname.options[i].text + "&";
			url += "name[]=" + str.substring(0, str.indexOf("(") - 1) + "&";
		}
	}
	if (url.length > 12) {
		url = url.slice(0, -1);
		//url += "track=" + track;
		window.open(url, "_self");
		return true;
	}
	else {
		alert("No species selected!");
		return false;
	}
}

function randomColor() {
    return '#' + ('00000' + (Math.random() * 16777216 << 0).toString(16)).substr(-6);
}

function popupWin(url) {
	var width  = 300;
	var height = 350;
	var left   = (screen.width  - width) / 2;
	var top    = (screen.height - height) / 2;
	var params = 'width=' + width + ', height=' + height;
	params += ', top=' + top + ', left=' + left;
	params += ', directories=no';
	params += ', location=no';
	params += ', menubar=no';
	params += ', resizable=no';
	params += ', scrollbars=no';
	params += ', status=no';
	params += ', toolbar=no';
	newwin = window.open(url,'windowname5', params);
	if (window.focus) {newwin.focus()}
	return false;
}