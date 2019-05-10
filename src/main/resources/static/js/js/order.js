var prefix = "/order";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#OrderTable').bootstrapTable('destroy');
    $('#OrderTable')
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
                // contentType : "application/x-www-form-urlencoded",
                // //发送到服务器的数据编码类型
                pageSize : 10, // 如果设置了分页，每页数据条数
                pageNumber : 1, // 如果设置了分布，首页页码
                showFooter:true,
                search : true, // 是否显示搜索框
               // showColumns : true, // 是否显示内容下拉框（选择显示的列）
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
                    {
                        checkbox:true
                    },
                    {
                        field : 'userId',
                        title : '用户id',
                        align : 'center'
                    },
                    {
                        field : 'orderNo',
                        title : '订单号',
                        align : 'center'
                    },
                    {
                        field : 'payment',
                        title : '金额',
                        align : 'center',
                        formatter:function (value) {
                            return value.toFixed(2);
                        }
                    },
                    {
                        field : 'paymentType',
                        title : '支付类型',
                        align : 'center',
                        formatter : function(value, row, index) {
                            if (value==1){var a='<span >线上支付</span>';return a;}
                        }
                    },
                    {
                        field : 'postage',
                        title : '运费',
                        align : 'center',
                        formatter:function (value) {
                            return value.toFixed(2);
                        }

                    },
                    {
                        field : 'status',
                        title : '状态',
                        formatter : function (value, row, index) {
                            if (value==0){return '<span style="color:darkred ">已取消</span>';}
                            if (value==10){return'<span style="color:grey ">未付款</span>';}
                            if (value==20){return'<span style="color:green ">已付款</span>';}
                            if (value==40){var d='<span style="color:cornflowerblue">已发货</span>';return d;}
                            if (value==50){return'<span style="color:green ">交易成功</span>';}
                            if (value==60){return'<span style="color:green ">交易关闭</span>';}
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
                            if (row['status']==10) {
                                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;"  onclick="orderInfo(\''+row.id+'\')" title="查看详情">查看详情</a> <br>';
                            var c = '<a  href="#" class="btn btn-success" style="padding:3px 9px; font-size:10px;line-height:15px;" title="支付" onclick="pay(\''+row.id+'\')" >支付</a>&nbsp;';
                                var b = '<a  href="#" title="取消" class="btn btn-danger" style="padding:3px 9px; font-size:10px;line-height:15px;"  onclick="delete1(\''+row.id+'\')" >删除</a>';
                                return a+c+b;}
                            else {
                                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;"  onclick="orderInfo(\''+row.id+'\')" title="查看详情">查看详情</a> <br>';
                                var b = '<a  href="#" class="btn btn-danger" style="padding:3px 9px; font-size:10px;line-height:15px;"title="取消"    onclick="delete1(\''+row.id+'\')" >删除</a>';
                                return a+b;}

                        }
                    }

                ]

            });
    $('#OrderTable').bootstrapTable('hideColumn', 'userId');
}

function reLoad() {
    $('#OrderTable').bootstrapTable('refresh');
}
function orderInfo(id) {
    // iframe层
        window.location.href=prefix + '/orderInfo/'+id // iframe的url
}
function pay(id) {
    window.location.href=prefix + '/pay/'+id // iframe的url
}
// function pay(id) {
//     var a = confirm("是否支付该订单");
//     if (a === true) {
//         $.ajax({
//             url: prefix + "/pay",
//             type: "get",
//             data: {
//                 'id': id
//             },
//             success: function (data) {
//                 if (data.code === 200) {
//                     alert("支付成功");
//                     reLoad();
//                 } else if (data.code === 0) {
//                     alert("支付失败");
//                 }
//             }
//         });
//
//     }
// }

function delete1(id) {
    var a = confirm("是否取消该订单");
    if (a === true) {
        $.ajax({
            url: prefix + "/delete",
            type: "get",
            data: {
                'id': id
            },
            success: function (data) {
                if (data.code == 200) {
                    alert("取消成功");
                    reLoad();
                } if (data.code == 0) {
                    alert("取消失败");
                    reLoad();
                }
            }
        });

    }
}
