/* Plus Size Image Viewer
* Last updated: Jan 11th, 2009. This notice must stay intact for usage 
* Author: JavaScript Kit at http://www.javascriptkit.com/
* Visit http://www.javascriptkit.com/ for full source code
*/

var plusimageviewer={
	enlargeboxmarkup: '<div class="enlargebox"><div class="title"><img src="images/closebox.gif" style="margin: 2px 1px 1px 0" title="Hide Image"  /></div><div class="largeimage"></div></div>',
	captionoffset: [-5, -15], //additional offset of caption relative to bottom left edge of image
	fadeduration: [300, 100], //fade in and out duration, in milliseconds
	//////////NO NEED TO EDIT BEYOND HERE/////////////

	pluscontainers:[],
	$enlargebox: null,
	boxzindex:100,

getcaptionposition:function($img){
	var offsets=$img.offset()
	return [offsets.left+this.captionoffset[0], offsets.top+$img.outerHeight()+this.captionoffset[1]] //return position of caption relative to thumb image
},

getlargeimgposition:function($, $enlargebox){
	var boxdimensions=[$enlargebox.outerWidth(), $enlargebox.outerHeight()]
	var docdimensions=[$(window).width(), $(window).height()]
	var docscrollpos=[$(document).scrollLeft(), $(document).scrollTop()]
	var leftpos=(docdimensions[0]>boxdimensions[0])? docscrollpos[0]+docdimensions[0]/2-boxdimensions[0]/2 : docscrollpos[0]+1 //center large image horizontally
	var toppos=(docdimensions[1]>boxdimensions[1])? docscrollpos[1]+docdimensions[1]/2-boxdimensions[1]/2 : docscrollpos[1]+1
	return [leftpos, toppos]
},

showimage:function($, $img){
	var pluscontainer=this.pluscontainers[$img.data('order')]
	pluscontainer.$enlargearea.empty().html('<img src="'+pluscontainer.enlargesrc+'" style="width:'+pluscontainer.enlargesize[0]+';" height:'+pluscontainer.enlargesize[1]+'" />')
	var largeimgpos=this.getlargeimgposition($, pluscontainer.$enlargebox)
	pluscontainer.$enlargebox.css({zIndex:++this.boxzindex, display:'none', left:largeimgpos[0], top:largeimgpos[1]}).fadeIn(this.fadeduration[0])
},

init:function($, $img){
	var captionpos=this.getcaptionposition($img)
	var $caption=$('<div class="enlargecaption"><a href="#">View Full Image</a></div>').css({left:captionpos[0], top:captionpos[1]}).appendTo(document.body)
	var $enlargebox=$(this.enlargeboxmarkup).appendTo(document.body)
	var $enlargearea=$enlargebox.find('.largeimage:eq(0)') //reference DIV that will contain actual enlarged image
	var enlargesrc=$img.attr('data-plusimage')
	var enlargesize=$img.attr('data-plussize').split(',') //get dimensions of large image as string
	enlargesize=[parseInt(enlargesize[0]), parseInt(enlargesize[1])] //get dimensions of large image as array [w, h]
	$caption.click(function(e){ //open large image when caption is clicked on
		plusimageviewer.showimage($, $img, e)
		e.preventDefault()
		e.stopPropagation()
	})
	$enlargebox.click(function(e){
		e.stopPropagation()
	}).find('div.title img:eq(0)').click(function(){ //close image box when "x" icon is clicked on
		$enlargebox.fadeOut(plusimageviewer.fadeduration[1])
	})
	this.pluscontainers.push({$img:$img, $caption:$caption, captionpos:captionpos, $enlargebox:$enlargebox, $enlargearea:$enlargearea, enlargesrc:enlargesrc, enlargesize:enlargesize})
}

}

jQuery(document).ready(function($){
	var $targetimages=$('img[data-plusimage]')
	$targetimages.each(function(i){
		var $img=$(this).data('order', i)
		plusimageviewer.init($, $(this), i)
	})
	if ($targetimages.length>0){
		$(document).click(function(){ //hide all plus size images when document is clicked
			var pluscontainers=plusimageviewer.pluscontainers
			for (var i=0; i<pluscontainers.length; i++){
				pluscontainers[i].$enlargebox.hide()
			}
		})
		$(window).bind('resize', function(){ //hide all plus size images when document is clicked
			var pluscontainers=plusimageviewer.pluscontainers
			for (var i=0; i<pluscontainers.length; i++){
				var captionpos=plusimageviewer.getcaptionposition(pluscontainers[i].$img)
				pluscontainers[i].captionpos=[captionpos[0], captionpos[1]] //refresh caption position
				pluscontainers[i].$caption.css({left:pluscontainers[i].captionpos[0], top:pluscontainers[i].captionpos[1]}) //reposition captions when window resized
			}
		})		
	}

})