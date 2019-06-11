<#macro chartArea chart>
    <div class="card border-0 shadow mb-4">
        <div class="card-header border-0 py-3 d-flex flex-row align-items-center justify-content-between">
            <h6 class="m-0 font-weight-bold text-primary">${chart.name}</h6>
            <button type="button" id="save-${chart.id}" class="card border-0 bg-primary text-white shadow">
                <i class="fas fa-save"></i>
            </button>
        </div>
        <div class="card-body">
            <div class="chart-area">
                <canvas id="${chart.id}"></canvas>
            </div>
        </div>
    </div>
</#macro>