
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
            // $('#search-results').empty();
            if(currentWishlist === ''){
                $('#search-results').empty();
            }
            else{
                loadWishlist() 
            }
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




   
    let currentWishlist='';

    // function setWishList(wishlistId, element) {
    //     const allLinks = document.querySelectorAll('#wishlistMenu a');
    //     allLinks.forEach(link => {
    //         link.classList.remove('bg-blue-300', 'dark:bg-blue-700');
    //     });
    
    //     currentWishlist = wishlistId;
    //     console.log("current wishlist " + currentWishlist);
    //     element.classList.add('bg-blue-300', 'dark:bg-blue-700');
    //     loadWishlist();
    // }

    function setWishList(wishlistId, element) {
        // Get all links in the wishlist menu
        const allLinks = document.querySelectorAll('#wishlistMenu a');
        
        // Remove background classes from all links
        allLinks.forEach(link => {
            link.classList.remove('bg-blue-300', 'dark:bg-blue-700');
            link.classList.add('bg-gray-100');
        });
        
        // Set the clicked link's background color
        element.classList.add('bg-blue-300', 'dark:bg-blue-700');
        element.classList.remove('bg-gray-100');
    
        // Update the current wishlist id
        currentWishlist = wishlistId;
        console.log("current wishlist " + currentWishlist);
        loadWishlist();
    }
    
    function addToWishlist(description,price) {

        if(currentWishlist=== ''){
            alert("No wishlist selected");
        }
        else{

            let wishlist = JSON.parse(localStorage.getItem(currentWishlist)) || [];
            const newItem = {description,price};
            const itemExists = wishlist.some(item => item.description === newItem.description);
            if (!itemExists) {
               wishlist.push(newItem);
                localStorage.setItem(currentWishlist, JSON.stringify(wishlist));
                console.log("data added");
                alert("data added");
            }else{
                console.log("already present") ;
                alert("already present");
            }
            
        }
        
         
    }



    function loadWishlist() {

        console.log("inside load data");
        // Replace with your actual key
        const wishlist = JSON.parse(localStorage.getItem(currentWishlist)) || [];
        const ul = document.getElementById('search-results'); // Get the <ul> element

        // Clear existing list items
        ul.innerHTML = '';

        // Iterate over each wishlist item and create list items
        wishlist.forEach(item => {
            const li = document.createElement('li');
            li.className = 'relative group h-10 list-none p-0 m-0 ';

            li.innerHTML = `
             

 
            <li class="relative group h-10 list-none p-0 m-0 ">
             <a href="/getDetails/${item.description}"
                    class="flex items-center p-1 rounded-lg text-sm dark:text-white hover:bg-gray-100 text-blue-600 h-10 w-full"
                    id="result-link">
                    <span class="flex-1 whitespace-nowrap">${item.description}</span>
                    <span class="whitespace-nowrap text-red-500 text-right">${item.price}</span>
            </a>


                <div
                    class="absolute right-0 top-0 mt-1 mr-1 flex space-x-2 opacity-0 group-hover:opacity-100 transition-opacity duration-300 bg-gray-300">
                    <a href="#"
                        class="text-black text-center bg-green-400 hover:bg-green-800 focus:ring-2 focus:ring-black font-medium rounded-lg text-sm px-2 py-1 dark:bg-blue-600 w-11">B</a>
                    <a href="#"
                        class="text-black text-center bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-black font-medium rounded-lg text-sm px-2 py-1 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-11">S</a>
                   
                </div>
            </li>
       
            
                `;

            ul.appendChild(li);
        });
    
    }
    
