
$(document).ready(function () {
    loadWishlist() 
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


     // for getting lot size for amoutn calculation
     $('#quantity').on('input', function() {
        var quantity = $(this).val();
        var description = $('#description').text(); // Get description from input field
        var price = parseFloat($('#price').text());
        // console.log("quantity : "+ quantity);
        // console.log("price : " +price);
        // console.log("description : " + description);
      
        $.ajax({
            url: '/getLotSize',
            type: 'GET',
            data: { description: description },
            success: function(data) {
                var lotamout = data.lotAmount;
                var amount = quantity * lotamout;
                $('#amount').text(amount);
                // console.log("inside success");
            },
            error: function(error) {
                console.error('Error fetching lot size:', error);
            }
        });
    });


});




   
    let currentWishlist='wishlist-1';

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
    function removeFromWishlist(description,price){
       
        let wishlist = JSON.parse(localStorage.getItem(currentWishlist)) || [];
       
        // Find the index of the item to remove
        const itemIndex = wishlist.findIndex(item => item.description === description);
        
      
        // Remove the item from the array
        wishlist.splice(itemIndex, 1);
        
        // Update local storage with the modified wishlist
        localStorage.setItem(currentWishlist, JSON.stringify(wishlist));
        loadWishlist();
        console.log("Item removed from wishlist");
       
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
                         data-description="${item.description}" data-price="${item.price}"
                         onclick="makeOrder('buy',this.getAttribute('data-description'),this.getAttribute('data-price'))"
                        class="text-black text-center bg-green-400 hover:bg-green-800 focus:ring-2 focus:ring-black font-medium rounded-lg text-sm px-2 py-1 dark:bg-blue-600 w-11">B</a>
                    <a href="#"
                        data-description="${item.description}" data-price="${item.price}"
                         onclick="makeOrder('sell',this.getAttribute('data-description'),this.getAttribute('data-price'))"
                        class="text-black text-center bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-black font-medium rounded-lg text-sm px-2 py-1 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-11">S</a>
                    <a href="#"
                        data-description="${item.description}" data-price="${item.price}"
                         onclick="removeFromWishlist(this.getAttribute('data-description'),this.getAttribute('data-price'))"
                        class="text-black text-center bg-white hover:bg-yellow-200 focus:ring-2 focus:ring-black font-medium rounded-lg text-sm px-2 py-1 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800 w-10">
                        <svg class="w-5 h-5 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 20 16">
    <path d="M19 0H1a1 1 0 0 0-1 1v2a1 1 0 0 0 1 1h18a1 1 0 0 0 1-1V1a1 1 0 0 0-1-1ZM2 6v8a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V6H2Zm11 3a1 1 0 0 1-1 1H8a1 1 0 0 1-1-1V8a1 1 0 0 1 2 0h2a1 1 0 0 1 2 0v1Z"/>
</svg></a>
                   
                </div>
            </li>
       
            
                `;

            ul.appendChild(li);
        });
    
    }
    

    function makeOrder(order,description,price){
        // console.log("makeorder " + order + description + price);
        if(order==='buy'){
           
            const modal = document.getElementById('crud-modal');

            // document.getElementById('name').value = "ayush";

            $('#price').text(price);
            $('#description').text(description);
            $('#submitButton').text("BUY");
            $('#quantity').val=0;
            $('#amount').text(0);
        
            modal.classList.toggle('hidden');
        }
        else{
            const modal = document.getElementById('crud-modal');
            // document.getElementById('name').value = "ayush";
            $('#price').text(price);
            $('#description').text(description);
            $('#submitButton').text("BUY");
            $('#quantity').val=0;
            $('#amount').text(0);
            modal.classList.toggle('hidden');
        }
    }
