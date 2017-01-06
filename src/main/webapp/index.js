var indexData={
		init:function(){
			try {
				$("#data-table").treegrid({
					url:"findResourceTree.do?time="+tool.getTime(),
					loadMsg:'正在加载,请稍后...',
					collapsible : true,
					fitColumns : true,
					fit : true,
					idField: 'id',
					treeField: 'fileName',
					remoteSort: false,
					sortOrder:'asc',
					columns:[[
						{field : 'fileName', title : '文件', width : 320, sortable: true, halign:"center"},
					    {field : 'label', title : '标签', width : 160, sortable: true, align:"center"},        
					    {field : 'versionNumber', title : '版本', width : 80, sortable: true, align:"center"},        
					    {field : 'updateTimeText', title : '更新时间', width : 160, sortable: true, align:"center"},
					    {field : 'cz',title : '操作', width : 160,sortable: true, align:"center", formatter:function(value,rowData){
					    	try {
					    		var value = [];
					    		value.push("<a class='link-button' href='javascript:;' onclick='indexData.refreshResource("+rowData.id+")'>&nbsp;刷新版本&nbsp;</a>");
					    		if(rowData.urlType != "directory"){
					    			value.push("<a class='link-button' href='javascript:;' onclick='indexData.updateResourceLabel("+rowData.id+")'>&nbsp;修改标签&nbsp;</a>");
					    		}
					    		return value.join("&ensp;");
							} catch (e) {
								alert(e.message);
							}
					    }}
					]],
					toolbar: [{id:'rebuildDatabase',iconCls: 'icon-25',text:'重建数据', handler: function(){
						$('#pass-verify').dialog({
							title : "验证密码",
							buttons:[{
								text:'确定',
								iconCls:'icon-ok',
								handler:function(){
									var password = $("#password").val();
									if(tool.isEmpty(password)){
										$.messager.alert('提示',"密码不能为空...",'info');
									}else{
										$.getJSON("rebuildDatabase.do",{"admin":password,"time":tool.getTime()},function(result){
							   				$.messager.alert('提示',result.message,'info');
										});
										$('#pass-verify').dialog("close");
									}
								}
							}]
						});
						$("#password").focus();
					}},'-',{id:'refreshAll',iconCls: 'icon-30',text:'全部更新', handler: function(){
						$.messager.confirm('确认', '你确定要更新全部数据么?', function(r){
							if (r){
								$.getJSON("refreshAllResource.do",{"time":tool.getTime()},function(result){
					   				$.messager.alert('提示',result.message,'info');
					   			});
							}
						});
					}}],
					onSelect:function(rowData){
					},
					onLoadSuccess:function(datas) {
					}
				});
			} catch (e) {
				alert(e.message);
			}
		},
		updateResourceLabel:function(id){
			$.messager.prompt('修改标签', '请输入新标签...', function(r){
				if (r){
		    		$.getJSON("updateResourceLabel.do",{"id":id,"label":r,"time":tool.getTime()},function(result){
		    			if(result.status == 200){
		    				$("#data-table").treegrid("reload");
		    			}
		   				$.messager.alert('提示',result.message,'info');
		   			});
		       	}
		    });
		},
		refreshResource:function(id){
			$.messager.confirm('确认', '你确定要更新这条数据么?', function(r){
				if (r){
					$.getJSON("refreshResource.do",{"id":id,"time":tool.getTime()},function(result){
						if(result.status == 200){
		    				$("#data-table").treegrid("reload");
		    			}
		   				$.messager.alert('提示',result.message,'info');
		   			});
				}
			});
		}
};
$(function(){
	indexData.init();
});