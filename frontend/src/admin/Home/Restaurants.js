import React, { useEffect, useState } from 'react'
import restaurantService from '../../service/restaurant.service';
import RestaurantList from '../../customer/Restaurant/RestaurantList';
import { Spinner,Container } from 'react-bootstrap';
import Loader from '../../shared/util/Loader/Loader';
import Header from '../../shared/util/Header';

function AdminRestaurants() {
    var mounted = false;
    const [favorites,setFavorites] = useState();
    const [searchResults, setSearchResults] = useState();
    const [categories,setCategories] = useState();
    const [loading,setLoading] = useState(false);

    useEffect(()=> {
        if(!mounted) {
            
            mounted=true;
            setLoading(true) // ... vrati ovo na true
            restaurantService.getAllRestaurants().then(res => {
                if(res.status==200) {
                    setSearchResults(res.data)
                    restaurantService.getUserFavorites().then(res => {
                        if(res.status==200) 
                            setFavorites(res.data)
                        else
                            console.log(res)
                        
                        restaurantService.getCategories().then(res=> {
                            setLoading(false)
                            if(res.status==200)
                                setCategories(res.data)
                        })
                            
                    })
                }
                else
                    console.log(res)

            })

            
        }
    },[])

    


  return (
    <>
    <Header></Header>
    <Loader isOpen={loading} >
    {searchResults?
    <Container style={{backgroundColor:"#D9D9D9",width:"95%",margin:"auto",marginTop:"20px",marginBottom:"20px",maxWidth:"95%"}}>
    {favorites && favorites.length>0? <RestaurantList restaurants={favorites} title={"Favorite restaurants"} showFilters={false} ></RestaurantList>: 
    
       <></>
     }
    {searchResults? <RestaurantList restaurants={searchResults} title={"All restaurants"} showFilters={true} perPage={8} categories={categories} setRestaurants={setSearchResults
    }></RestaurantList>: 
    
       <div style={{display:"flex",justifyContent:"center"}}>
           <Spinner animation="border" style={{color:"white",marginTop:"20%"}}/>
       </div>
     }
     </Container> : <></>}
     </Loader>
    </> 
  )
}

export default AdminRestaurants