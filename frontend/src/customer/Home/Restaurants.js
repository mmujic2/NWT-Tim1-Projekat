import React, { useEffect, useState } from 'react'
import restaurantService from '../../service/restaurant.service';
import RestaurantCard from '../Restaurant/RestaurantCard';
import RestaurantList from '../Restaurant/RestaurantList';
import { Spinner } from 'react-bootstrap';

function Restaurants() {
    var mounted = false;
    const [favorites,setFavorites] = useState();
    const [searchResults, setSearchResults] = useState();

    useEffect(()=> {
        if(!mounted) {
            console.log(process.env.REACT_APP_TEST)
            mounted=true;
            restaurantService.getAllRestaurants().then(res => {
                if(res.status==200) {
                    setSearchResults(res.data)
                    restaurantService.getUserFavorites().then(res => {
                        if(res.status==200) 
                            setFavorites(res.data)
                        else
                            console.log(res)
        
                            
                    })
                }
                else
                    console.log(res)

            })

            
        }
    },[])

    


  return (
    <>
    {searchResults? <RestaurantList restaurants={searchResults}></RestaurantList>: 
    
       <div style={{display:"flex",justifyContent:"center"}}>
           <Spinner animation="border" style={{color:"white",marginTop:"20%"}}/>
       </div>
     }
    </>
  )
}

export default Restaurants