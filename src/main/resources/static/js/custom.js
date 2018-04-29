(function($){
    
    $(document).ready(function(){

    });
    $.fn.timeformat = function() {
		 return $(this).each(function() {
			 var date = $(this).html();
			 var today = new Date();
			 var timestamp = new Date(date);
			 var dd = timestamp.getDate();
			 var mm = timestamp.getMonth()+1;
		     var yyyy =timestamp.getFullYear(); 
		     var HH = timestamp.getHours();
		     var MM = timestamp.getMinutes();
		     if(HH<10){HH='0'+HH} 
		     if(MM<10){MM='0'+MM}
       	 if(dd == today.getDate() && mm == (today.getMonth()+1) && yyyy == today.getFullYear()){
       		 timestamp = HH+':'+MM;
       		 $(this).html(timestamp);
       	 }
       	 else{
			     timestamp = dd+"."+mm+"."+yyyy+" "+HH+":"+MM;
			     $(this).html(timestamp);
		 	}
		 });
	 }
    $.fn.timediff = function() {
		 return $(this).each(function() {
			 var timestamp = $(this).html();
			 var d1 = new Date(timestamp);
			 var seconds = (new Date() - d1)/1000;
			 var days = seconds/(3600*24)
			 var hours = seconds/3600;
			 var minutes = seconds/60;
			 if(days >=7){
				 $(this).html(d1);
				 
			 } else if (days >= 1) {
			 
			 		if(Math.floor(days)>1)
			 			$(this).html("vor " + Math.floor(days) + " Tagen" );
			 		else
			 			$(this).html("vor " + Math.floor(days) + " Tag" );
			 		
		 	} else if (hours >= 1 ) {
		 		
		 			if(Math.floor(hours)>1) 
		 				$(this).html("vor " + Math.floor(hours) + " Stunden" );
		 			else
		 				$(this).html("vor " + Math.floor(hours) + " Stunde" );
		 	} else if (minutes >= 1){
			 		if(Math.floor(minutes)>1) 
		 				$(this).html("vor " + Math.floor(minutes) + " Minuten" );
		 			else
		 				$(this).html("vor " + Math.floor(minutes) + " Minute" );
		 	} else {
			 		if(Math.floor(seconds)>1) 
		 				$(this).html("vor " + Math.floor(seconds) + " Sekunden" );
		 			else
		 				$(this).html("vor " + "1" + " Sekunde" );
		 	}
		 });
	}
    
})(jQuery)