<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro js_imports>
    <script src="${clones.table.url}"></script>
</#macro>

<#macro page_title>

</#macro>

<#macro page_content>
    <@table clones.table/>
</#macro>

<@display_page sidebar=sidebar title=clones.name generated_date=generated_date/>