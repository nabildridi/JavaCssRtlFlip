
$(function () {
 

	  
	  /*
	    Global controls
	  */
	  $('#startUploadButton').on('click', function(evt){
	    
	    var formData = new FormData($('form')[0]);

	    $.LoadingOverlay("show", {image       : "", fontawesome : "fa fa-cog fa-spin"});
	    
		$.ajax({
	        type: 'POST',
	        url: "/upload",
	        processData:false,
	        data: formData,
	        contentType: false,
			cache: false,
	        success: function (data){
	        	processFiles(data);			        	
	        },
	        complete: function() {
	        	$.LoadingOverlay("hide");
	        }
	    });
	    
	    
	  });

	 
	  
    
});




function sendText(){	
	
	var originalText = $("#originalText").val();
	
	if(!originalText){
		return; 
	}
	
				
	$.LoadingOverlay("show", {image       : "", fontawesome : "fa fa-cog fa-spin"});
	
	$.ajax({
        type: 'POST',
        contentType: 'application/text',
        url: "/text",
        processData:false,
        data: originalText,
        success: function (data){
        	var decodedString = atob(data);
        	$("#convertedText").val(decodedString);			        	
        },
        complete: function() {
        	$.LoadingOverlay("hide");
        }
    });	
	
}






function processFiles(data){
	

	if(data.error){
		bootbox.alert(data.errorCause);
	}else{
		
		var json = JSON.parse(data);
	
		var zip = new JSZip();
		
		for(item of json){
			zip.file(item.filename, item.converted);
		}	
		
		zip.generateAsync({type:'blob'}).then(function(content) {
	          SaveAsFile(content,"convertedFiles.zip","application/zip;charset=utf-8"); 
	    });
		
	}
	
	
}

function SaveAsFile(t,f,m) {
    try {
        var b = new Blob([t],{type:m});
        saveAs(b, f);
    } catch (e) {
        window.open("data:"+m+"," + encodeURIComponent(t), '_blank','');
    }
}



