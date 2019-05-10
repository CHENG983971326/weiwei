var prefix = "/customer/product";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#ProductTable').bootstrapTable('destroy');
    $('#ProductTable')
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
                pageList : [5,10,20,30],
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                showFooter:true,
                search : true, // 是否显示搜索框
               // showColumns : true, // 是否显示内容下拉框（选择显示的列）
                sidePagination : "client", // 设置在哪里进行分页，可选值为"client" 或者
                // "server"
                columns : [

                    {
                        field : 'categoryId',
                        title : '类别名称',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value==1){var a='<span >电脑数码</span>';return a;}
                            if (value==2){var a='<span >运动户外</span>';return a;}
                            if (value==3){var a='<span >母婴用品</span>';return a;}
                            if (value==4){var a='<span >食品生鲜</span>';return a;}
                            if (value==5){var a='<span >图书音像</span>';return a;}
                            if (value==6){var a='<span >家具家装</span>';return a;}
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
                            // var b='<a class = "view " href="javascript:void(0)">查看</a>&nbsp;';
                            return a;
                        },
                        events: 'operateEvents'
                    },
                    {
                        field : 'name',
                        title : '商品名称',
                        align : 'center'
                    },
                    {
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
                            if (row['status']==1) {
                                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" onclick="query(\''+row.id+'\')" title="查看详情">查看详情</a> ';
                                var c = '<a  href="#" class="btn btn-success " style="padding:3px 9px; font-size:10px;line-height:15px;" title="加入购物车" onclick="add(\''+row.id+'\',\''+row.name+'\',\''+row.price+'\')" >加入购物车</a>&nbsp;';
                                return a+c;}
                            else if ( row['status']==2) {
                                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" onclick="query(\''+row.id+'\')" title="查看详情">查看详情</a> <br>';
                                return a;
                            }

                        }
                    }

                ]
            });
}
function query(ide) {
    window.location.href = '/customer/product/query.do/'+ide;
}
add=function(idd,ids,idr){
        window.location.href = '/customer/product/add.do/' + idd + '/' + ids + '/' + idr;

}

function reLoad() {
    $('#ProductTable').bootstrapTable('refresh');
}
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