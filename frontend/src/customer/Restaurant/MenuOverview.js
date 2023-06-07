import React, { useEffect, useState } from "react";
import menuService from "../../service/menu.service";
import { Tabs, Tab } from "react-bootstrap";
import "./MenuOverview.css";
import { Trash, X, HeartFill } from "react-bootstrap-icons";
import MenuItem from "./MenuItem";
import Loader from "../../shared/util/Loader/Loader";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
import { Spinner, Container } from "react-bootstrap";
import {Row, Col, Button} from "react-bootstrap";

function MenuOverview({ restaurantUUID }) {
  const [menus, setMenus] = useState([]);
  const [loading, setLoading] = useState(true);
  const [orderList, setOrderList] = useState([]);

  useEffect(() => {
    console.log(orderList)
  }, [orderList])

  const removeItemFromOrder = (item) => {
    var newOrderList = []
    orderList.forEach(i => {if(i.menuItem.id != item.menuItem.id) newOrderList.push(i)})
    setOrderList(newOrderList)
  }

  const orderListToUI = () => {
    return (orderList.map(item => (
      <>
        <Row style={{marginBottom: "5px"}}>
          <Col xs={7}>
            {item.menuItem.name} x{item.count}
          </Col>
          <Col xs={3}>
            {(item.menuItem.discount_price != null ? item.menuItem.discount_price : item.menuItem.price) * item.count} KM
          </Col>
          <Col xs={1}>
            <Button
              onClick={() => removeItemFromOrder(item)}
              style={{
                backgroundColor: "#fe724c",
                borderColor: "#fe724c",
                width: "fit-content",
                height: "25px",
                width: "25px",
                padding: "0px"
              }}
            >
              <Trash></Trash>
            </Button>
          </Col>
        </Row>
      </>
    )))
  }

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
                          orderList={orderList}
                          setOrderList={setOrderList}
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
                      <Container
                      style={{
                        backgroundColor: "#FFFFFF",
                        width: "100%",
                        margin: "auto",
                        marginTop: "5px",
                        marginBottom: "5px",
                      }}
                      >
                        {orderListToUI()}
                        
                      </Container>

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
