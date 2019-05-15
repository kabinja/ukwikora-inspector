$(document).ready(function() {
    var table = $('#${data.id}').DataTable( {
        lengthChange: false,
        searching: false,
        dom: 'Bfrtip',
        buttons: [
            { extend: 'copy', className: 'btn btn-secondary buttons-collection buttons-copy' },
            { extend: 'csv', className: 'btn btn-secondary buttons-collection buttons-csv' },
            { extend: 'excel', className: 'btn btn-secondary buttons-collection buttons-excel' },
            { extend: 'pdf', className: 'btn btn-secondary buttons-collection buttons-pdf' },
            { extend: 'colvis', className: 'btn btn-secondary buttons-collection dropdown-toggle buttons-colvis' }
        ]
    } );

    table.buttons().container().appendTo( '#${data.id}_wrapper .col-md-6:eq(0)' );

    // Setup - add a text input to each footer cell
    $('#${data.id} thead tr').clone(true).appendTo( '#${data.id} thead' );
    $('#${data.id} thead tr:eq(1) th').each( function (i) {
        var title = $(this).text();
        $(this).html( '<input type="text" placeholder="Search '+title+'" />' );

        $( 'input', this ).on( 'keyup change', function () {
            if ( table.column(i).search() !== this.value ) {
                table
                .column(i)
                .search( this.value )
                .draw();
            }
        } );
    } );
} );