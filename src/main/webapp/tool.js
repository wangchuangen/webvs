var tool={
		data:{
			timeGap:0,
			rootPath:null
		},
		alert:function(text){
			alert(text);
		},
		idTag:function(id){
			return document.getElementById(id);
		},
		nameTags:function(name){
			return document.getElementsByName(name);
		},
		tagItself:function(id){
			return document.getElementById(id).outerHTML;
		},
		idLible:function(id){
			return document.getElementById(id);
		},
		nameLibles:function(name){
			return document.getElementsByName(name);
		},
		trim:function(text){
			return text.replace(/(^\s*)|(\s*$)/g,"");
		},
		transcoding:function(uri){
			return encodeURI(uri);
		},
		encodeURI:function(URI){
			if(this.isEmpty(URI))return "";
			return window.encodeURI(URI);
		},
		encodeURIComponent:function(URI){
			if(this.isEmpty(URI))return "";
			return window.encodeURIComponent(URI);
		},
		textFocus:function(textLible,text){
			if(textLible.value==text){
				textLible.value="";
			}
		},
		textBlur:function(textLible,text){
			if(textLible.value==""){
				textLible.value=text;
			}
		},
		getTime:function(){
			return new Date().getTime();
		},
		getServerTime:function(){
			return new Date().getTime()+this.data.timeGap;
		},
		isEmpty:function(x){
			switch (typeof x){ 
			case 'undefined' : return true; 
			case 'string' : if(this.trim(x).length == 0) return true; break; 
			case 'boolean' : if(!x) return true; break; 
			case 'object' : 
				if(null === x) return true; 
				if(undefined !== x.length && x.length==0) return true; 
				for(var k in x){return false;} return true; 
				break; 
			} 
			return false;
		},
		formatTemplate:function(data,template){
		    return template.replace(/{(\w+)}/g,function(mark,markName){
		        if (!markName)return "";
		        return data[markName];
			});  
		},
		getRootPath:function(){
			if(this.isEmpty(this.data.rootPath)){
				var hrefText=window.document.location.href;
				var pathText=window.document.location.pathname;
				var x=pathText.indexOf("/",1);
				var rootPath=pathText.substring(0,x);
				var y=hrefText.indexOf(rootPath);
				var hostText=hrefText.substring(0,y);
				this.data.rootPath=hostText+rootPath+"/";
			}
			return this.data.rootPath;
		},
		getParams:function(isDebug){
			var urlParams=location.search;
			var params=new Object();
			if(urlParams.indexOf("?")>=0){
				var paramText=urlParams.substring(1);
				var paramList=paramText.split("&");
				for ( var i in paramList) {
					var param=paramList[i].split("=");
					if(isDebug)window.console.log(param);
					if(param.length>1)params[param[0]]=unescape(param[1]);
				};
			}
			return params;
		}
};
$(function(){
	jQuery.extend({tool:tool});
});
