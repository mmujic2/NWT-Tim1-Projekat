import React from 'react'
import authService from '../../service/auth.service'
import Restaurants from '../../customer/Home/Restaurants';

function Home() {
    const user = authService.getCurrentUser();

  return (
    user.role=="CUSTOMER" ? <Restaurants></Restaurants>: <></>
    
  )
}

export default Home