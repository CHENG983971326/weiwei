var prefix = "/manage/product";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#productTable').bootstrapTable('destroy');
    $('#productTable')
        .bootstrapTable(
            {
                method : 'get', // 服务器数据的请求方式 get or post
                url : prefix + "/list", // 服务器数据的加载地址
                striped : true, // 设置为true会有隔行变色效果
                dataType : "json", // 服务器返回的数据类型
                pagination : true, // 设置为true会在底部显示分页条
                //queryParamsType : "limit",
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect : false, // 设置为true将禁止多选
                iconSize : 'outline',
                toolbar : '#exampleToolbar',
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                pageList : [5,10,20,30],
                showFooter:true,
                search : true, // 是否显示搜索框
                //showColumns : true, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者
                // "server"

                // queryParams : queryParams,
                // //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
                // queryParamsType = 'limit' ,返回参数必须包含
                // limit, offset, search, sort, order 否则, 需要包含:
                // pageSize, pageNumber, searchText, sortName,
                // sortOrder.
                // 返回false将会终止请求
                columns : [
                    // {
                    //     checkbox:true
                    // },
                    {
                        field : 'categoryId',
                        title : '所属类别',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value==1){var a='<span>电脑数码</span>';return a;}
                            if (value==2){var a='<span>运动户外</span>';return a;}
                            if (value==3){var a='<span>母婴用品</span>';return a;}
                            if (value==4){var a='<span>食品生鲜</span>';return a;}
                            if (value==5){var a='<span>图书音像</span>';return a;}
                            if (value==6){var a='<span>家具家装</span>';return a;}
                        }

                    },
                    {
                        field : 'mainImage',
                        title : '商品图片',
                        align : 'center',
                        detailView : true,
                        formatter:function (value,row,index) {
                            var d=""+ value.substring(8);
                            var a="<img src='/file/"+d+"' style='height: 50px;width: 50px'>";
                            // var b='<a class = "view " href="javascript:void(0)">查看</a>&nbsp;'
                            return a;
                        },
                        events: 'operateEvents'
                    },
                    {
                        field : 'name',
                        title : '商品名称',
                        align : 'center'
                    },{
                        field : 'subtitle',
                        title : '型号',
                        align : 'center'
                    },
                    {
                        field : 'price',
                        title : '价格',
                        align : 'center',
                        formatter:function (value) {
                            return value.toFixed(2);
                        }
                    },

                    {
                        field : 'status',
                        title : '状态',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value==1){return'<span style="color:green ">在售</span>';}
                            if (value==2){return'<span style="color:darkred ">下架</span>';}
                        }
                    },
                    {
                        title : '操作',
                        field : 'id',
                        align : 'center',
                        formatter : function(value, row, index) {
                            var id=row.id;
                            console.log("id:"+id)
                            var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" title="编辑"onclick="update2(\''+row.id+'\')">编辑</a>&nbsp;&nbsp;';
                            var b = '<a  href="#"class="btn btn-danger " style="padding:3px 9px; font-size:10px;line-height:15px;" title="删除" onclick="delete1(\''+row.id+'\')" >删除</a><br>';
                            var c = '<a  href="#" class="btn btn-success " style="padding:3px 9px; font-size:10px;line-height:15px;" title="上架"onclick="status_on(\''+row.id+'\')">上架</a>&nbsp;&nbsp;';
                            var d = '<a  href="#" class="btn btn-warning " style="padding:3px 9px; font-size:10px;line-height:15px;"title="下架" onclick="status_out(\''+row.id+'\')" >下架</a><br>';
                            if (row.status==1){
                               return a+b+d;
                           }
                           if (row.status==2) {
                               return a +b+c;
                           }


                        }
                    }

                ]
            });
}

function reLoad() {
    $('#productTable').bootstrapTable('refresh');
}
//删除商品
delete1=function(idd){
    var a=confirm("确定要删除该商品吗？");
    if (a===true) {
        $.ajax({
            type: 'POST',//提交方式 post 或者get
            url: '/manage/product/delete.do',//提交到那里后他的服务
            data:{"id":idd},
            dataType:'json',
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = '/manage/product/list.do';
                    alert("删除成功");
                } else {
                    alert("删除失败");
                }
            }
        });
    }
};
//商品上架
status_on=function(ids) {
    var a = confirm("确定要上架该商品吗？");
    if (a === true) {
        $.ajax({
            type: 'POST',//提交方式 post 或者get
            url: '/manage/product/status_on.do',//提交到那里后他的服务
            data: {"id": ids},
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = '/manage/product/list.do';
                    alert("上架成功");
                } else {
                    alert("上架失败");
                }
            }
        });
    }
}
//商品下架
status_out=function(idp) {
    var a = confirm("确定要下架该商品吗？");
    if (a === true) {
        $.ajax({
            type: 'POST',//提交方式 post 或者get
            url: '/manage/product/status_out.do',//提交到那里后他的服务
            data: {"id": idp},
            dataType: 'json',
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = '/manage/product/list.do';
                    alert("下架成功");
                } else {
                    alert("下架失败");
                }
            }
        });
    }
}
//编辑商品
update2=function(ide) {
    var a=confirm("确定要编辑该商品吗？");
    if (a===true) {
        window.location.href = '/manage/product/update.do/'+ide;
    }};
window.operateEvents = {
    'click .view': function (e, value, row, index) {
        layer.open({
            type: 1,
            title: false,
            closeBtn: 0,
            area: ['400px', '400px'],
            skin: 'layui-layer-nobg', //没有背景色
            shadeClose: true,
            content: '<img src="'+value+'" style="width: 400px;height: 400px">'
        });
    }
};
