import React from 'react'
import { useState,useEffect } from 'react';
import restaurantService from '../../service/restaurant.service';
import Loader from '../../shared/util/Loader/Loader';
import ListContainer from '../../shared/util/ListContainer/ListContainer'

function FavoriteRestaurants() {
  var mounted = false;
  const [favorites, setFavorites] = useState();
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (!mounted) {

      mounted = true;
      setLoading(true)

      restaurantService.getUserFavorites().then(res => {
        setLoading(false)
        if (res.status == 200)
          setFavorites(res.data)
        else
          console.log(res)


      })
    }



  }, [])


  return (
    <>
    <Loader isOpen={loading} >
      {favorites?
    <ListContainer items={favorites} title={"Favorite restaurants"} showFilters={false} grid={false}></ListContainer>
    : <></>}
    </Loader>
    </>
  )
}

export default FavoriteRestaurants