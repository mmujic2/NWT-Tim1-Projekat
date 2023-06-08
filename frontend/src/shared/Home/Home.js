import React from 'react'
import authService from '../../service/auth.service'
import Restaurants from '../../customer/Home/Restaurants';
import AdminRestaurants from '../../admin/Home/AdminRestaurants';
import Orders from '../../deliveryPerson/Orders';
import RestaurantOrders from "../../restaurantManager/Home/Orders"

function Home() {
    const user = authService.getCurrentUser();

  return (
    <>

    {user.role=="CUSTOMER" ? <Restaurants></Restaurants>: 
    user.role=="ADMINISTRATOR" ? <AdminRestaurants/> :
    user.role=="COURIER" ? <Orders></Orders> :
    user.role=="RESTAURANT_MANAGER" ? <RestaurantOrders></RestaurantOrders> : <></>}
    </>
    
  )
}

export default Home