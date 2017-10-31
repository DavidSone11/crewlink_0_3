'use strict';

var ServerTableFetch = function(url,subLinks, SpringDataRestApi, callBackBefore,callBackAfter,callBackAfterError,isWait) {
	this.url = url;
	this.urlValue = url;
	this.subLinks = subLinks;
	this.SpringDataRestApi = SpringDataRestApi;
	this.callBackBefore = callBackBefore;
	this.callBackAfter = callBackAfter;
	this.callBackAfterError = callBackAfterError;
	this.isWait = isWait;
	this.process = function(tableState){
		this.callBackBefore("CAllBACK BEFORE");
		if(angular.isFunction(this.url)){
			this.urlValue = this.url(tableState);
		} 
		
		if(this.urlValue == ''){
			this.callBackAfterError("no Fetch");
			return;
		}
		
		var pagination = tableState.pagination;
		var page = (pagination.start/pagination.number) || 0;     // This is NOT the page number,
											// but the index of item in the
											// list that you want to use to
											// display the table.
		var size = pagination.number || 10;  // Number of entries showed
												// per page.
		var sort = (tableState.sort.predicate)?tableState.sort.predicate:"";
		var sortDir = (tableState.sort.reverse)?"asc":"desc";
		var searchParams = "";
		if(this.urlValue.indexOf("?")==-1){
			searchParams = "?";
		}else{
			searchParams = "&";
		}
		if(tableState.search.predicateObject){	
			for(var searchItem in tableState.search.predicateObject){
				var re = new RegExp(searchItem+"=(\w+)","g");
				if(searchParams.search(re)!=-1){
					searchParams = searchParams.replace(re, searchItem+'='+tableState.search.predicateObject[searchItem]+'&');
				}else{
					searchParams += searchItem+"=" +tableState.search.predicateObject[searchItem]+"&";
				}
				//searchParams  += searchItem+"="+ tableState.search.predicateObject[searchItem]+"&";
			}
		}
		var call = this.urlValue + searchParams;
		if(call.search(/page=(\w+)/g)!=-1){
			if(page>-1)
				call = call.replace(/page=(\w+)/g, 'page='+page+'&');
		}else{
			if(page>-1)
				call += "page=" +page;
		}
		if(call.search(/size=(\w+)/g)!=-1){
			if(size>0)
				call = call.replace(/size=(\w+)/g, 'size='+size+'&');
		}else{
			if(size)
				call += "&size=" +size;
		}
		if(call.search(/limit=(\w+)/g)!=-1){
			if(size>0)
				call = call.replace(/limit=(\w+)/g, 'limit='+size+'&');
		}else{
			if(size>0)
				call += "&limit=" +size;
		}
		if(call.search(/sort=(\w+)/g)!=-1){
			if(sort!=""){
				var tmp = call.replace(/sort=(\w+),(\w+)/g, "sort="+sort+"," +sortDir);
				call = tmp;
			}
				
		}else{
			if(sort!="")
				call += "&sort=" +sort+ "," +sortDir;
		}
//		
//		var call = this.urlValue + searchParams
//		  +"page=" +page + "&limit="+size
//		  +"&size=" +size+ "&sort=" +sort+ ","
//		  +sortDir;
		
		this.SpringDataRestApi.get(call,this.subLinks,this.isWait)
		.then(function(response){
			  for(var item in response._embedded)
				  break;
			  var resultObj = response._embedded[item];
			  tableState.pagination.numberOfPages = response.page.totalPages;
			  resultObj.tableState = tableState;
			  this.callBackAfter(resultObj);
		}.bind(this),
		function(response){
			if(this.callBackAfterError){
				this.callBackAfterError(response);
			}
		}.bind(this));
	}.bind(this);
};

var ServerTableFetch2 = function(url, SpringDataRestApi, callBackBefore,callBackAfter,callBackAfterError,isWait) {
	this.url = url;
	this.urlValue = url;
	this.SpringDataRestApi = SpringDataRestApi;
	this.callBackBefore = callBackBefore;
	this.callBackAfter = callBackAfter;
	this.callBackAfterError = callBackAfterError;
	this.isWait = isWait;
	this.process = function(tableState){
		this.callBackBefore();
		if(angular.isFunction(this.url)){
			this.urlValue = this.url(tableState);
		}
		
		if(this.urlValue == ''){
			this.callBackAfterError("no Fetch");
			return;
		}
		
		var pagination = tableState.pagination;
		var page = (pagination.start/pagination.number) || 0;     // This is NOT the page number,
											// but the index of item in the
											// list that you want to use to
											// display the table.
		var size = pagination.number || 10;  // Number of entries showed
												// per page.
		var sort = (tableState.sort.predicate)?tableState.sort.predicate:"";
		var sortDir = (tableState.sort.reverse)?"asc":"desc";
		var searchParams = "";
		if(this.urlValue.indexOf("?")==-1){
			searchParams = "?";
		}else{
			searchParams = "&";
		}
		if(tableState.search.predicateObject){	
			for(var searchItem in tableState.search.predicateObject){
				var re = new RegExp(searchItem+"=(\w+)","g");
				if(searchParams.search(re)!=-1){
					//searchParams = searchParams.replace(re, searchItem+'='+tableState.search.predicateObject[searchItem]+'&');
				}else{
					searchParams += searchItem+"=" +tableState.search.predicateObject[searchItem]+"&";
				}
				//searchParams  += searchItem+"="+ tableState.search.predicateObject[searchItem]+"&";
			}
		}
		var call = this.urlValue + searchParams;
		if(call.search(/page=(\w+)/g)!=-1){
			if(page>-1)
				call = call.replace(/page=(\w+)/g, 'page='+page+'&');
		}else{
			if(page>-1)
				call += "page=" +page;
		}
		if(call.search(/size=(\w+)/g)!=-1){
			if(size>0)
				call = call.replace(/size=(\w+)/g, 'size='+size+'&');
		}else{
			if(size)
				call += "&size=" +size;
		}
		if(call.search(/limit=(\w+)/g)!=-1){
			if(size>0)
				call = call.replace(/limit=(\w+)/g, 'limit='+size+'&');
		}else{
			if(size>0)
				call += "&limit=" +size;
		}
		if(call.search(/sort=(\w+)/g)!=-1){
			if(sort!=""){
				var tmp = call.replace(/sort=(\w+),(\w+)/g, "sort="+sort+" " +sortDir);
				call = tmp;
			}
				
		}else{
			if(sort!="")
				call += "&sort=" +sort+ " " +sortDir;
		}
//		
//		var call = this.urlValue + searchParams
//		  +"page=" +page + "&limit="+size
//		  +"&size=" +size+ "&sort=" +sort+ ","
//		  +sortDir;
		
		this.SpringDataRestApi.get(call,null,this.isWait)
		.then(function(response){
			  tableState.pagination.numberOfPages = response.selectionDetails.totalPages+1; // As the total pages is start from 0
			  tableState.pagination.totalItemCount = response.selectionDetails.totalElements;
			  this.callBackAfter(response);
		}.bind(this),
		function(response){
			if(this.callBackAfterError){
				this.callBackAfterError(response);
			}
		}.bind(this));
	}.bind(this);
};

ServerTableFetch.prototype = {
	url : null,
	subLinks : null,
	SpringDataRestApi : null,
	searchItems : null,
	resultObj : null,
	process : null,
	callBack : null
};

ServerTableFetch2.prototype = {
		url : null,
		SpringDataRestApi : null,
		searchItems : null,
		resultObj : null,
		process : null,
		callBack : null
	};
