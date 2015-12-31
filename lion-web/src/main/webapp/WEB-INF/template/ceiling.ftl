<#macro ceiling num>
    <#local pageCount = (totalCount / 10)?string('#.#') />
    <#if pageCount?contains('.')>
        <#local pageCount = (pageCount?substring(0, pageCount?last_index_of('.')))?number + 1/>
    </#if>
</#macro>