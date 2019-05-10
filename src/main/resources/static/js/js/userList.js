var prefix = "/user";
$(function() {
    load();
});

function load() {
    console.log("123");
    $('#UserListTable').bootstrapTable('destroy');
    $('#UserListTable')
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
                        field : 'username',
                        title : '用户名称',
                        align : 'center'
                    },
                    {
                        field : 'email',
                        title : '邮箱',
                        align : 'center'

                    },
                    {
                        field : 'phone',
                        title : '手机号',
                        align : 'center'
                    },
                    {
                        field : 'role',
                        title : '角色',
                        align : 'center',
                        formatter : function(value, row, index) {
                        if (value==0){return'<span style="color:green ">管理员</span>';}
                        if (value==1){return'<span style="color:grey ">普通用户</span>';}
                       }
                    },

                    {
                        field : 'createTime',
                        title : '注册时间',
                        align : 'center',
                        formatter:function (value) {
                            if (value!=null) {
                                return value.substr(0, 20);
                            }
                        }
                    },
                    {
                        field : 'updateTime',
                        title : '更新时间',
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
                            var a = '<a  href="#"  class="btn btn-info " style="padding:3px 9px; font-size:10px;line-height:15px;"  title="编辑"onclick="update1(\''+row.id+'\')">编辑</a>&nbsp;&nbsp;';
                            var b = '<a  href="#" class="btn btn-danger " style="padding:3px 9px; font-size:10px;line-height:15px;"  title="删除" onclick="delete1(\''+row.id+'\',\''+row.role+'\')" >删除</a>';
                            return a +b;
                        }
                    }

                ]
            });
}

function reLoad() {
    $('#UserListTable').bootstrapTable('refresh');
}
//删除用户
delete1=function(idd,role){
    var a=confirm("确定要删除该用户吗？");
    if (a===true) {
    $.ajax({
        type: 'post',//提交方式 post 或者get
        url: '/user/delete.do',//提交到那里后他的服务
        data:{"id":idd,
            "role":role

        },
        dataType:'json',
        success: function (data) {
            if (data.code == 200) {
                window.location.href = '/user/list';
                alert("删除成功");
            }
            if (data.code == 1) {
                window.location.href = '/user/list';
                alert("无此权限");
            }if (data.code==0) {
                window.location.href = '/user/list';
                alert("删除失败");
            }
        }
    });
}}

//编辑用户
update1=function(idds){
    var a=confirm("确定要编辑该用户吗？");
    if (a===true) {
        window.location.href = '/user/update.do/' + idds;
    }
}

