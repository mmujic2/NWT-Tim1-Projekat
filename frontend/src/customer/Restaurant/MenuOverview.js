import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";
import { Tabs, Tab } from "react-bootstrap";
import "./MenuOverview.css";
import MenuItem from "./MenuItem";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import { Spinner, Container } from "react-bootstrap";

function MenuOverview({ restaurantUUID }) {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (restaurantUUID != null) {
      menuService.getActiveRestaurantMenus(restaurantUUID).then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setMenus(res.data);
          setLoading(false);
        }
      });
    }
  }, []);
  return (
    <Loader isOpen={loading}>
      <div style={{ overflowY: "auto" }}>
        <hr className="tab-separator" />
        <Tabs defaultActiveKey={0} id="my-tabs" style={{ padding: 10 }}>
          {menus.map((menus, index) => (
            <Tab
              eventKey={index}
              title={<span style={{ color: "black" }}>{menus.name}</span>}
              key={index}
            >
              <div>
                {menus.menuItems ? (
                  <>
                    <Container
                      style={{
                        backgroundColor: "#D9D9D9",
                        width: "100%",
                        margin: "auto",
                        marginTop: "20px",
                        marginBottom: "20px",
                        maxWidth: "60%",
                        float: "left",
                      }}
                    >
                      {menus.menuItems ? (
                        <ListContainer
                          items={menus.menuItems}
                          title={""}
                          showFilters={false}
                          perPage={5}
                          type={"menu"}
                          grid={false}
                        ></ListContainer>
                      ) : (
                        <div
                          style={{ display: "flex", justifyContent: "center" }}
                        >
                          <Spinner
                            animation="border"
                            style={{ color: "white", marginTop: "20%" }}
                          />
                        </div>
                      )}
                    </Container>
                    <Container
                      style={{
                        backgroundColor: "#D9D9D9",
                        width: "100%",
                        margin: "auto",
                        marginTop: "20px",
                        marginBottom: "20px",
                        maxWidth: "38%",
                        float: "right",
                      }}
                    >
                      Order
                    </Container>
                  </>
                ) : (
                  <></>
                )}
              </div>
            </Tab>
          ))}
        </Tabs>
      </div>
    </Loader>
  );
}

export default MenuOverview;
