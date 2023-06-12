import React, { useState } from 'react'
import Sidebar from '../../shared/util/Sidebar/Sidebar'
import authService from '../../service/auth.service'
import NotFound from '../../shared/util/NotFound'
import { useLocation } from 'react-router-dom'
import MainContainer from '../../shared/util/RightSideContainer/MainContainer'
import OrderHistory from '../../customer/CustomerDetails/OrderHistory'
import RestaurantInformation from './RestaurantInformation'
import RestaurantGallery from './RestaurantGallery'
import AddMenu from "../Menus/AddMenu"
import MenusOverview from "../Menus/MenusOverview"
import CouponList from "../Coupons/CouponList";
import Reviews from '../Reviews/Reviews'


function RestaurantDetails() {
  var mounted = false;
  const options = new Map([
    ["Restaurant details", "/restaurant/details"],
    ["Menus", "/restaurant/menus"],
    ["Order history", "/restaurant/order/history"],
    ["Reviews", "/restaurant/reviews"],
    ["Coupons", "/restaurant/coupons"],
  ]);
  const icons = new Map([
    ["Restaurant details", "utensils"],
    ["Menus", "book-open"],
    ["Order history", "receipt"],
    ["Reviews", "star"],
    ["Coupons", "percent"],
  ]);
  const user = authService.getCurrentUser();
  const [collapsed, setCollapsed] = useState(false);
  const location = useLocation();

  return (
    <div>
      {user.role != "RESTAURANT_MANAGER" ? (
        <NotFound />
      ) : (
        <Sidebar
          optionsMap={options}
          iconsMap={icons}
          collapsed={collapsed}
          setCollapsed={setCollapsed}
        />
      )}
      <MainContainer collapsed={collapsed}>
          {location.pathname == "/restaurant/details" ? (
            <RestaurantInformation />
          ) : location.pathname=="/restaurant/gallery" ?
          <RestaurantGallery/> 
          : location.pathname == "/restaurant/order/history" ? (
            <OrderHistory />
          ) : location.pathname == "/restaurant/coupons" ? (
            <CouponList />
          ) : location.pathname == "/restaurant/menus" ? (
            <MenusOverview />
          ) : location.pathname == "/menu/add" ? (
            <AddMenu />
          ) : location.pathname == "/restaurant/reviews" ? (
            <Reviews/>
          ) : (<></>)}
      </MainContainer>
    </div>
  );
      
          }
export default RestaurantDetails;
