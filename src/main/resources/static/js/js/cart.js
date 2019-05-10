var prefix = "/cart";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#CartTable').bootstrapTable('destroy');
    $('#CartTable')
        .bootstrapTable(
            {
                method : 'get',
                url : prefix + "/list",
                striped : true,
                dataType : "json",
                pagination : true,
                singleSelect : false,
                iconSize : 'outline',
                toolbar : '#exampleToolbar',
                pageList : [5,10,20,30],
                pageSize : 10,
                pageNumber : 1,
                showFooter:true,
                search : true,
                // showColumns : true,
                sidePagination : "client",
                columns : [
                    {
                        field : 'id',
                        title : '购物车商品项id',
                        align : 'center'
                    },
                    {
                        field : 'userId',
                        title : '用户id',
                        align : 'center'
                    },
                    {
                        field : 'productId',
                        title : '商品id',
                        align : 'center'
                    },
                    {
                        field : 'quantity',
                        title : '数量',
                        align : 'center',
                        footerFormatter: function (value) {
                            var count = 0;
                            for (var i in value) {
                                if(value[i].checked===1){
                                    count += value[i].quantity;
                                }

                            }
                            return count;
                        }
                    },
                    {
                        field : 'checked',
                        title : '是否已选',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value==1) {
                                return '<span style=" color: #1e7e34">已选定</span>';
                            }else {
                                return '<span style="color:darkred ">未选定</span> ';
                            }
                        }
                    },
                    {
                        field : 'totalPrice',
                        title : '单项总价',
                        align : 'center',
                        footerFormatter: function (value) {
                            var count = 0;
                            for (var i in value) {
                                if (value[i].checked===1) {
                                    count += value[i].totalPrice;
                                }
                            }
                            return count;
                        }
                    },
                    {
                        field : 'createTime',
                        title : '创建时间',
                        align : 'center',
                        formatter:function (value) {
                            if (value!=null) {
                                return value.substr(0, 20);
                            }
                        }

                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var id=row.id;
                            console.log("id:"+id)
                            if (row['checked']==1) {
                                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" title="查看详情"onclick="cartInfo(\''+row.id+'\')">查看详情</a><br>';
                                var b = '<a  href="#" class="btn btn-warning" style="padding:3px 9px; font-size:10px;line-height:15px;" title="取消选定" onclick="cancel(\''+row.id+'\')" >取消选定</a>';
                                return a +b;
                            }
                            if (row['checked']==0) {
                                var a = '<a  href="#"  class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" title="查看详情"  onclick="cartInfo(\''+row.id+'\')">查看详情</a> <br>';
                                var b = '<a href="#" title="选定" class="btn btn-success" style="padding:3px 9px; font-size:10px;line-height:15px;" onclick="pass(\''+row.id+'\')">选定</a> ';
                                var i = '<a href="#" title="删除" class="btn btn-danger" style="padding:3px 9px; font-size:10px;line-height:15px;" onclick="delete1(\''+row.id+'\')">删除</a> ';
                                return a +b+i;
                            }
                        }
                    }

                    ]
            });
    $('#CartTable').bootstrapTable('hideColumn', 'userId');
    // $('#CartTable').bootstrapTable('hideColumn', 'id');
}
function reLoad() {
    $('#CartTable').bootstrapTable('refresh');
}
function cartInfo(id) {
    // iframe层
  window.location.href=prefix + '/cartInfo/'+id;
}
function pass(id) {
    var a = confirm("是否选定该项商品");
    if (a === true) {
        $.ajax({
            url: prefix + "/pass",
            type: "get",
            data: {
                'id': id
            },
            success: function (data) {
                if (data.code === 200) {
                    alert("选定成功");
                    reLoad();
                } else if (data.code === 0) {
                    alert("选定失败");
                }
            }
        });

    }
}

function delete1(id) {
    var a = confirm("是否删除该项商品");
    if (a === true) {
        $.ajax({
            url: prefix + "/delete",
            type: "get",
            data: {
                'id': id
            },
            success: function (data) {
                if (data.code === 200) {
                    alert("删除成功");
                    reLoad();
                } else if (data.code === 0) {
                    alert("删除失败");
                }
            }
        });

    }
}
var cancel=function(id) {
    var a=confirm("是否取消选定");
    if (a===true) {
        $.ajax({
            url : prefix + "/cancel",
            type : "get",
            data : {
                'id' : id
            },
            success : function(data) {
                if (data.code===200) {
                    alert("取消选定成功");
                    reLoad();
                } else if (data.code===0){
                    alert("取消选定失败");
                }
            }
        });
}};


