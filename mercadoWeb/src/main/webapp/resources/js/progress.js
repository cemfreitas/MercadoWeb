var handle = {}  
   
 function on_start() {  
     handle = setTimeout(function() {  
         $('html').addClass('progress')  
     }, 250)  
 }  
   
 function on_complete() {  
     clearTimeout(handle)  
     $('html').removeClass('progress')  
 }  