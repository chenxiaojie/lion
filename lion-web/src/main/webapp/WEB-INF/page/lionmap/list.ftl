<#setting classic_compatible=true>
<#assign projectName = Request["projectName"] />
<#assign env = Request["env"] />
<head>
    <title>${projectName}-配置管理</title>
    <script src="/js/lionmap.js"></script>
</head>
<body>
<div class="container">
    <div class="panel panel-success" style="margin-top: 60px;">
        <div class="panel-heading">
            <h3 class="panel-title" style="display: inline-block;width: 200px;">项目：${projectName}</h3>
            <a class="btn btn-default btn-xs" style="float: right;"
               href="javascript:showLionAddDialog('${projectName}', '${env}');"><span
                    class="glyphicon glyphicon-plus" aria-hidden="true"></span> 添加</a>
            <a class="btn btn-default btn-xs" style="float: right;margin-right: 20px;" href="/"><span
                    class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span> 返回</a>
        </div>
        <div class="panel-body">
            <ul class="nav nav-tabs" style="margin-bottom: 20px;">
                <li role="presentation" <#if (env == 1)>class="active"</#if>><a href="/${projectName}/list/1">开发</a>
                </li>
                <li role="presentation" <#if (env == 2)>class="active"</#if>><a href="/${projectName}/list/2">Alpha</a>
                </li>
                <li role="presentation" <#if (env == 3)>class="active"</#if>><a href="/${projectName}/list/3">Beta</a>
                </li>
                <li role="presentation" <#if (env == 4)>class="active"</#if>><a href="/${projectName}/list/4">预发</a>
                </li>
                <li role="presentation" <#if (env == 5)>class="active"</#if>><a href="/${projectName}/list/5">线上</a>
                </li>
                <li role="presentation" <#if (env == 6)>class="active"</#if>><a href="/${projectName}/list/6">性能</a>
                </li>
            </ul>
            <table id="dataTable" class="table table-bordered table-hover">
                <thead>
                <tr>
                    <th width="10%">ID</th>
                    <th width="30%">key</th>
                    <th width="30%">value</th>
                    <th width="10%">是否懒加载</th>
                    <th width="20%">操作</th>
                </tr>
                </thead>
                <tbody>
                <#list Request["lionMaps"] as lionMap>
                <tr id="dataTable_tr_${lionMap.id}">
                    <td>${lionMap.id}</td>
                    <td>${lionMap.mapKey}</td>
                    <td>${lionMap.mapValue}</td>
                    <td>${lionMap.lazy?string('是', '否')}</td>
                    <td>
                        <a class="btn btn-default btn-xs"
                           href="javascript:showLionUpdateDialog(${lionMap.id},'${projectName}', '${env}');"><span
                                class="glyphicon glyphicon-upload" aria-hidden="true"></span> 更新</a>
                        <a class="btn btn-default btn-xs" style="margin-left: 20px;"
                           href="javascript:showLionDeleteDialog(${lionMap.id},'${projectName}', '${env}');"><span
                                class="glyphicon glyphicon-trash" aria-hidden="true"></span> 删除</a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
<#import "/template/pagination.ftl" as pagination>
<@pagination.pagination pageSize=RequestParameters["pageSize"] curIndex=RequestParameters["curIndex"] totalCount=Request["totalCount"] url='/${projectName}/list/${env}' />
</div>
<div id="hide-dialog-container"></div>
</body>