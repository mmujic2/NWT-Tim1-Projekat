import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";

function MenuOverview({ restaurantUUID }) {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (restaurantUUID != null) {
      menuService.getActiveRestaurantMenus(restaurantUUID).then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setMenus(res.data);
        }
        console.log(res);
      });
    }
  }, []);
  return <div>{JSON.stringify(menus)}</div>;
}

export default MenuOverview;
