<#include "lib/base.ftl">
<#include "lib/table.ftl">

<#macro js_imports>
    <script src="${violations.table.url}"></script>
</#macro>

<#macro page_title>
</#macro>

<#macro page_content>
    <@table violations.table/>
</#macro>

<@display_page sidebar=sidebar title=violations.name generated_date=generated_date/>