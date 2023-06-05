import React from 'react'
import authService from '../../service/auth.service'
import Restaurants from '../../customer/Home/Restaurants';
import AdminRestaurants from '../../admin/Home/AdminRestaurants';
import Orders from '../../deliveryPerson/Orders';

function Home() {
    const user = authService.getCurrentUser();

  return (
    <>

    {user.role=="CUSTOMER" ? <Restaurants></Restaurants>: 
    user.role=="ADMINISTRATOR" ? <AdminRestaurants/> :
    user.role=="COURIER" ? <Orders></Orders> : <></>}
    </>
    
  )
}

export default Home