<#-- curIndex:当前页码, totalCount:数据总条数, pageSize:每页大小, url: 网址 -->
<#--
使用方式:
<@pagination.pagination pageSize=RequestParameters["pageSize"] curIndex=RequestParameters["curIndex"] totalCount=Request["totalCount"] url='/${projectName}/list/${env}' />
-->
<#macro pagination pageSize, curIndex, totalCount, url>
    <#if (pageSize != '')>
        <#local pageSize = pageSize?number />
        <#if (pageSize > 20)>
            <#local pageSize = 20 />
        </#if>
    <#else>
        <#local pageSize = 10 />
    </#if>
    <#if (curIndex != '')>
        <#local curIndex = curIndex?number />
    <#else>
        <#local curIndex = 1 />
    </#if>
    <#local pageCount = (totalCount / pageSize)?ceiling />
    <#if (curIndex < 1)>
        <#local curIndex = 1 />
    <#elseif (curIndex > pageCount)>
        <#local curIndex = pageCount />
    </#if>
    <#if !url?contains('?')>
        <#local url = url + '?pageSize=' + pageSize + '&curIndex='/>
    <#elseif url?ends_with('?')>
        <#local url = url + 'pageSize=' + pageSize + '&curIndex='/>
    <#else>
        <#local url = url + '&pageSize=' + pageSize + '&curIndex='/>
    </#if>
<nav>
    <ul class="pagination">
        <#if (pageCount > 1)>
        <#-- 处理第1页 -->
            <#if (curIndex == 1)>
                <li class="disabled"><span aria-hidden="true">&laquo;</span></li>
                <li class="active"><span>1</span></li>
            <#else>
                <li>
                    <a href="${url}${curIndex - 1}" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>
                <li><a href="${url}1">1</a></li>
            </#if>

            <#if (pageCount > 6)>
            <#-- 处理大于6页的情况 -->
                <#if (curIndex < 5)>
                <#-- 处理当前页是前4页的情况 -->
                    <#list 2..5 as i>
                        <#if (i != curIndex)>
                            <li><a href="${url}${i}">${i}</a></li>
                        <#else>
                            <li class="active"><span>${i}</span></li>
                        </#if>
                    </#list>
                <#-- 在第5页后面添加... -->
                    <li class="disabled"><span>...</span></li>
                <#else>
                <#-- 处理当前页不是前4页的情况 -->
                <#-- 在第1页后面添加...... -->
                    <li class="disabled"><span>...</span></li>
                    <#if (pageCount - curIndex < 4)>
                    <#-- 处理当前页是倒数4页的情况 -->
                        <#list (pageCount - 4)..(pageCount - 1) as i>
                            <#if (i != curIndex)>
                                <li><a href="${url}${i}">${i}</a></li>
                            <#else>
                                <li class="active"><span>${i}</span></li>
                            </#if>
                        </#list>
                    <#else>
                    <#-- 处理当前页不是前4页,也不是倒数4页,即是在中间的情况 -->
                        <#list (curIndex - 2)..(curIndex + 2) as i>
                            <#if (i != curIndex)>
                                <li><a href="${url}${i}">${i}</a></li>
                            <#else>
                                <li class="active"><span>${i}</span></li>
                            </#if>
                        </#list>
                        <li class="disabled"><span>...</span></li>
                    </#if>
                </#if>
            <#else>
            <#-- 处理小于等于6页的情况 -->
                <#if (pageCount > 2)>
                    <#list 2..(pageCount - 1) as i>
                        <#if (i != curIndex)>
                            <li><a href="${url}${i}">${i}</a></li>
                        <#else>
                            <li class="active"><span>${i}</span></li>
                        </#if>
                    </#list>
                </#if>
            </#if>
        <#-- 处理最后一页 -->
            <#if (curIndex != pageCount)>
                <li><a href="${url}${pageCount}">${pageCount}</a></li>
                <li>
                    <a href="${url}${curIndex + 1}" aria-label="Next"><span aria-hidden="true">&raquo;</span></a>
                </li>
            <#else>
                <li class="active"><span>${pageCount}</span></li>
                <li class="disabled"><span aria-hidden="true">&raquo;</span></li>
            </#if>
        <#else>
        <#-- 如果只有一页的时候 -->
            <li class="disabled"><span aria-hidden="true">&laquo;</span></li>
            <li class="active"><span>1</span></li>
            <li class="disabled"><span aria-hidden="true">&raquo;</span></li>
        </#if>
    </ul>
</nav>
</#macro>