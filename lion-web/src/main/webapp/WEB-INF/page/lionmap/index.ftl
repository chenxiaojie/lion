<#setting classic_compatible=true>
<head>
    <title>集中配置管理系统 - lion</title>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#projectSelection').modal({
                backdrop: false
            });
            $('#projectName').keyup(function () {
                $('#projectName_span').text($(this).val() + '.');
            });
        });
        function showAddProject() {
            var dialog = $('#lion-add-project-dialog').modal('show');
            dialog.find('#lion-add-project-dialog-form').clearFormData();
        }
        function addProject() {
            if (!$('#projectName').val()) {
                $.warning('项目名不能为空!');
                return;
            }
            if (!$('#mapKey').val()) {
                $.warning('key不能为空!');
                return;
            }
            if (!$('#mapValue').val()) {
                $.warning('value不能为空!', 3000);
                return;
            }
            $.ajax({
                type: 'POST',
                url: '/add-project',
                data: $('#lion-add-project-dialog-form').serialize(),
                success: function (result) {
                    if (result) {
                        window.location.href = "/" + $('#projectName').val() + '/list/1';
                    } else {
                        $.warning('添加失败,检查配置是否已经存在!');
                    }
                }
            });
        }
    </script>
</head>
<body>
<div class="container">
    <div id="projectSelection" class="modal fade">
        <div class="modal-dialog" style="margin: 250px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" style="display: inline-block;width: 200px;">选择项目</h4>
                    <a class="btn btn-default btn-xs" style="float: right;" href="javascript:showAddProject();"><span
                            class="glyphicon glyphicon-plus" aria-hidden="true"></span> 添加项目</a>
                </div>
                <div class="modal-body">
                <#list Request["projectNames"] as projectName>
                    <a class="btn btn-default" href="/${projectName}/list/1">${projectName}</a>
                </#list>
                </div>
            </div>
        </div>
    </div>
</div>
<#include "div/lionmap/lion-add-project-dialog.ftl">
</body>