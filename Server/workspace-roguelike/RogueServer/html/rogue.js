// rogue.js

   var imgfont = document.createElement("img");
   imgfont.src = "roguefont.png";
   
   var actiontext = document.getElementById("action");

   var screen = document.getElementById("screen");                       
   var context = screen.getContext("2d");
   
   var person = "name";
   
   context.imageSmoothingEnabled = false;
   context.mozImageSmoothingEnabled = false;
   context.webkitImageSmoothingEnabled = false;
   context.msImageSmoothingEnabled = false;
   
   var ws = null;

   function WebSocketStart()
   {
      if ("WebSocket" in window)
      {
         person = prompt("Please enter your name", "");

         if (person == null || person == "")  // Cancelled 
         {
            return;
         }     
         
         person = person.substring(0, 16);
          
         // Let us open a web socket
         ws = new WebSocket("ws://localhost:3007/Rogue");         
         ws.binaryType = 'arraybuffer';
	
         // Web Socket is connected
         ws.onopen = function()
         {          
             sendAnnounce(person);
         };
	
         ws.onmessage = function(evt) 
         { 
            var byteArray = new Uint8Array(evt.data);
            drawScreen(byteArray.slice(1,358));
            
            drawString(person.toUpperCase(),24,1);
            drawString("I SEE:",24,3);
            drawString("LEFT: ",24,5);
            drawString("RIGHT:",24,6);             
            drawString("HEALTH:",24,8);
            
             for (var p = 0; p < 40; p++) 
             {
                drawChar(byteArray[358+p], p, 20);
                drawChar(byteArray[398+p], p, 21);
                drawChar(byteArray[438+p], p, 22);
                drawChar(byteArray[478+p], p, 23);
             }
            
            drawChar(byteArray[518], 31, 3); // Seen
            drawChar(byteArray[519], 31, 5); // Left
            drawChar(byteArray[520], 31, 6); // Right
            drawChar(byteArray[521], 31, 8); // Health
            drawChar(byteArray[522], 32, 8); // Health
            drawChar(byteArray[523], 33, 8); // Health
                 
            // TODO, XP, Gold, etc.
         };
	
         ws.onclose = function()
         { 
            // websocket is closed.
            alert("Connection is closed!"); 
         };
		
         window.onbeforeunload = function(event) 
         {
            ws.close();
         };
      }
      
      else
      {
         // The browser doesn't support WebSocket
         alert("WebSocket NOT supported by your Browser!");
      }
   }
   
   function sendAnnounce(name)
   {      
      if (ws != null)
      {
         ws.send("1" + name);
      }  
   }
   
   var command_counter = 0;

   function sendCommand(key)
   {
      var command = new Uint8Array(3);
      command[0] = 2;   // Client Update  
      command[1] = command_counter++;   // Counter
      command[2] = key;
      
      if (ws != null)
      {
         ws.send(command);
      }  
   }

   document.onkeypress = function(event) 
   { 
      var key = event.which;
      sendCommand(key); 
   };   
  
   document.onkeydown = function(event) 
   { 
      var key = event.which;
        
      switch (key) 
      { 
        case 37: 
           moveWest(); 
           break; 

        case 38:
           moveNorth();
           break; 

        case 39:
           moveEast();
           break; 

        case 40:
           moveSouth();
           break;
     } 
  };     

  function Use()
  {
     sendCommand(42);
  }

  function moveNorth()
  {
     sendCommand(119);
  }

  function moveSouth()
  {
     sendCommand(115);
  }

  function moveWest()
  {
     sendCommand(97);
  }

  function moveEast()
  {
     sendCommand(100);
  }

  function myScale()
  {
     context.setTransform(1, 0, 0, 1, 0, 0);
     var scale = 2;  // ddscale.value;
     context.scale(scale,scale);
     repaint();
  }

  function repaint()
  {
    // Background (Not visible)
    context.fillStyle = "yellow"; 
    context.fillRect(0, 0, screen.width, screen.height);

    // Screen
    context.fillStyle = "black";
    context.fillRect(0, 0, 320, 200);
    
    // Box
    context.fillStyle = "grey";
    context.fillRect(0, 0, 184, 152);   
  }

  function load()
  {
      myScale();
      WebSocketStart();
  }

  function drawScreen(charArray)
  {
    var char = 0;

    for (row = 0; row < 17; row++) 
    {
       for (col=0; col < 21; col++)
       {
          drawChar(charArray[char], col+1, row+1);  // +1 to make room for border            
          char++;
       }
    }
  }

  function drawChar(num, x, y)
  {
     const FONTWIDTH = 32;
     const CHARSIZE  = 8;
     
     var col = num % FONTWIDTH;
     var row = Math.floor(num/FONTWIDTH);
     
     col *= CHARSIZE;
     row *= CHARSIZE;

     x *= CHARSIZE;
     y *= CHARSIZE;
   
     context.drawImage(imgfont, col, row, CHARSIZE, CHARSIZE, x, y, CHARSIZE, CHARSIZE); 
  }
  
  function drawString(text, x, y)
  {
     drawChar(5,x,y);
     
     for (var p = 0; p < text.length; p++) 
     {
        var l = text.charCodeAt(p);
        drawChar(l,x+p,y);
     }
  }
  
  
  
  
  imgfont.addEventListener("load", load());

// EOF