var prefix = "/manage/order";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#orderTable').bootstrapTable('destroy');
    $('#orderTable')
        .bootstrapTable(
    {

        method : 'get', // 服务器数据的请求方式 get or post
            url : prefix + "/lists", // 服务器数据的加载地址
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
            field : 'userId',
            title : '用户号',
            align : 'center'
        },
        {
            field : 'orderNo',
            title : '订单号',
            align : 'center'
        },
        {
            field : 'payment',
            title : '总金额',
            align : 'center',
            formatter:function (value) {
                return value.toFixed(2);
            }
        },
        {
            field : 'paymentType',
            title : '支付方式',
            align : 'center',
            formatter : function(value, row, index) {
                if (value==1){var a='<span style="color: blue">在线支付</span>';return a;}
              }
        },{
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
            align : 'center',
            formatter : function(value, row, index) {
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
            title : '生成时间',
            align : 'center',
            formatter:function (value) {
                if (value!=null) {
                    return value.substr(0, 20);
                }
            }
        },
        {
            field : 'paymentTime',
            title : '支付时间',
            align : 'center',
            formatter:function (value) {
                if (value!=null) {
                    return value.substr(0, 20);
                }
            }
        },
        // {
        //     field : 'sendTime',
        //     title : '发货时间',
        //     align : 'center'
        // },
        // {
        //     field : 'endTime',
        //     title : '结束时间',
        //     align : 'center'
        // },

        {
            title : '操作',
            field : 'id',
            align : 'center',
            formatter : function(value, row, index) {
                var a = '<a  href="#" class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;" title="编辑"onclick="update1(\''+row.id+'\')">编辑</a>&nbsp;&nbsp;';
                var b = '<a  href="#" class="btn btn-danger" style="padding:3px 9px; font-size:10px;line-height:15px;" title="删除" onclick="delete1(\''+row.id+'\')" >删除</a><br>';
                return a +b;
            }
        }

    ]
    });
}

function reLoad() {
    $('#orderTable').bootstrapTable('refresh');
}
//删除订单
delete1=function(idd){
    var a=confirm("确定要删除该订单吗？");
    if (a===true) {
        $.ajax({
            type: 'post',//提交方式 post 或者get
            url: '/manage/order/delete.do',//提交到那里后他的服务
            data:{"id":idd
            },
            dataType:'json',
            success: function (data) {
                if (data.code == 200) {
                    window.location.href = '/manage/order/list';
                    alert("删除成功");
                }
                if (data.code == 1) {
                    window.location.href = '/manage/order/list';
                    alert("无此权限");
                }if (data.code==0) {
                    window.location.href = '/manage/order/list';
                    alert("删除失败");
                }
            }
        });
    }}

//编辑订单
update1=function(idds){
    var a=confirm("确定要编辑该订单吗？");
    if (a===true) {
        window.location.href = '/manage/order/update.do/'+idds;

    }}


