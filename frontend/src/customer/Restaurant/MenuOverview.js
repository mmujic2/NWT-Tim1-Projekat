import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";
import { Tabs, Tab } from "react-bootstrap";
import "./MenuOverview.css";

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
  return (
    <div>
      <Tabs defaultActiveKey={0} id="my-tabs" style={{ padding: 10 }}>
        {menus.map((menus, index) => (
          <Tab
            eventKey={index}
            title={<span style={{ color: "black" }}>{menus.name}</span>}
            key={index}
          >
            <div>
              <h2>{menus.name}</h2>
              <hr className="tab-separator" />
              <p>This is the content for {menus.name}</p>
            </div>
          </Tab>
        ))}
      </Tabs>
    </div>
  );
}

export default MenuOverview;
