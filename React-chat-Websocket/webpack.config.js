module.exports={
	context:__dirname,
	entry:"./index.js",
	output: {
        path: __dirname+'/static',
        filename: "bundle.js"
    },
    module:{
        loaders:[
            {
                test:/\.js$/,
                exclude: /node_modules/,
                loader:'babel'
            }
        ]
    }
};