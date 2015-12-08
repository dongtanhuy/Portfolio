var React = require('react');
var ReactDOM = require('react-dom');
var socket=io();

var Chat = React.createClass({
	getInitialState: function (){
		return{
			username:"",
		};
	},
	_onName: function(e){
	    if (e.key != "Enter") return;
	    var username = e.target.value;
	    this.setState({username: username});
  	},
	render:function(){
		return(
			<div>
				<WelcomeView username={this.state.username} _onName={this._onName} />
				<MainView username={this.state.username} />
			</div>
		);
	}
});

var WelcomeView = React.createClass({
	render: function(){
		var username = this.props.username;
		return(
			username?<h1>Welcome {username}</h1> : <input onKeyPress={this.props._onName} placeholder="Please enter your name" />
		);
	}
});

var Messages = React.createClass({

	render:function(){
		var list=this.props.messages.map(function(message){
			return(
				<li>
          			<p><b><i>At {message.time}</i>, {message.username} said :</b>{message.text}</p>
				</li>
			);
		});
		return( <ul>{list}</ul>);
	}
});

var MainView = React.createClass({
	getInitialState: function(){
		return{
			messages:[],
		};
	},

	_onMessage: function(e){
		if (e.key != "Enter") return;
		var input = e.target;
		var text = input.value;
		if (text==='') return;
		var message={
			username:this.props.username,
			text:text,
			time:new Date().toISOString().slice(0,10)
		}
		socket.emit('chat message',message);
		input.value='';
	},
	send:function(message){
		var messages=this.state.messages;
		messages.push(message);
		this.setState({messages:messages});
	},
	componentDidMount: function() {
		socket.on('chat message',this.send);
	},
	render: function(){
		if (!this.props.username) var style = {display:'none'};
		console.log(this.state.messages);
		return (
			<div style={style}>
				<Messages messages={this.state.messages} />
				<input placeholder="Type your message here" onKeyPress={this._onMessage} />
			</div>
		);
	}
});


ReactDOM.render(<Chat />, document.getElementById('app'));