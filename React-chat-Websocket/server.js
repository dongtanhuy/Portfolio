var express= require('express');
var app= express();
var http= require('http').Server(app);
var io = require('socket.io')(http);
app.use(express.static(__dirname + '/static'));
app.get("/", function(req,res){
	res.sendFile("index.html");
});

io.on('connection',function(socket){
	console.log("A new user connected");
	socket.on('disconnect',function(){
		console.log("A user disconnected")
	});
	socket.on('chat message',function(msg){
		io.emit('chat message',msg)
		console.log("message: "+msg);
	});
})

http.listen(3000, function(){
  console.log('listening on *:3000');
});