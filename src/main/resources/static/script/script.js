
$(document).ready(function () {

    console.log("inside script");
    $('#default-search').on('input', function () {
    
        var query = $(this).val();
        console.log(query)
        if (query.length > 0) {
            console.log("inside query length");
            $.ajax({
                url: '/search',
                type: 'GET',
                data: { query: query },
                success: function (data) {
                    console.log(data);
                    console.log("inside on success function");
                    $('#search-results').html(data);
                },
                error: function (xhr, status, error) {
                    console.error('AJAX Error: ' + status + ' - ' + error);
                    console.error('Response Text: ' + xhr.responseText);
                }
            });
        } else {
            $('#search-results').empty();
        }
    });
    
    
    
    //For details of  instrument.
    $(document).on('click', '#result-link', function (event) {
        console.log("inside click a");
        event.preventDefault();
        var url = $(this).attr('href');
        console.log(url);
        $.ajax({
            url: url,
            type: 'GET',
            success: function (data) {
                console.log(data);
                $('#details-div').html(data);
            },
            error: function (xhr, status, error) {
                console.error('AJAX Error: ' + status + ' - ' + error);
                console.error('Response Text: ' + xhr.responseText);
            }
        });
    });
    });
    
    
    