<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro js_imports>
    <script src="${dictionary.table.url}"></script>
</#macro>

<#macro page_content>
    <@table dictionary.table/>
</#macro>

<@display_page sidebar=sidebar title=dictionary.name generated_date=generated_date/>