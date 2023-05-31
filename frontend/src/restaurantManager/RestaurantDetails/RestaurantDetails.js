import React, { useState } from 'react'
import Sidebar from '../../shared/util/Sidebar/Sidebar'
import authService from '../../service/auth.service'
import NotFound from '../../shared/util/NotFound'
import { useLocation } from 'react-router-dom'
import MainContainer from '../../shared/util/RightSideContainer/MainContainer'
import OrderHistory from '../../customer/CustomerDetails/OrderHistory'
import RestaurantInformation from './RestaurantInformation'

function RestaurantDetails() {
    const options = new Map([['Restaurant details', "/restaurant/details"], ["Menus", "/restaurant/menus"], ["Order history", "/restaurant/order/history"],["Reviews", "/restaurant/reviews"],["Coupons", "/restaurant/coupons"]])
    const icons = new Map([['Restaurant details', "utensils"], ["Menus", "book-open"], ["Order history", "receipt"], ["Reviews","star"], ["Coupons","percent"]])
    const user = authService.getCurrentUser();
    const [collapsed,setCollapsed] = useState(false)
    const location = useLocation();

    return (
        <div>{
            user.role != "RESTAURANT_MANAGER" ? <NotFound /> :
                <Sidebar optionsMap={options} iconsMap={icons} collapsed={collapsed} setCollapsed={setCollapsed}/>
        }
        <MainContainer collapsed={collapsed}>
            {location.pathname=="/restaurant/details" ? 
            <RestaurantInformation/> :
            location.pathname=="/restaurant/order/history" ? 
            <OrderHistory/> :
            <></>}
        </MainContainer>
        </div>
    )
}

export default RestaurantDetails