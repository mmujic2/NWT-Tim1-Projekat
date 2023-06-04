import React, { useState } from "react";
import { Col, Row, Button, Container, ButtonGroup } from "react-bootstrap";
import Card from "react-bootstrap/Card";
import { Add, Edit, Delete } from "@mui/icons-material";
import "./MenuCard.css";
import CustomAlert from "../../shared/util/Alert";

import { left } from "@popperjs/core";
import menuService from "../../service/menu.service";

function MenuCard({
  menu,
  menus,
  setMenus,
  alert,
  setAlert,
  showAlert,
  setShowAlert,
  setLoading,
}) {
  const handleDelete = (e) => {
    e.preventDefault();
    e.stopPropagation();
    setLoading(true);
    menuService.deleteMenu(menu.id).then((res) => {
      if (res.status == 200) {
        document.body.style.cursor = "default";
        setAlert({
          ...alert,
          type: "success",
          msg: "Successfully deleted menu!",
        });
        setShowAlert(true);
        console.log(alert);
        console.log(showAlert);
        setMenus(menus.filter((m) => m.id != menu.id));
      } else {
        setAlert({ ...alert, type: "error", msg: res.data });
        setShowAlert(true);
      }
      setLoading(false);
    });

    console.log(menu.id);
  };

  return (
    <div>
      {menu ? (
        <Card
          onClick={() => console.log(menu.id)}
          style={{
            width: "100%",
            height: "7rem",
            overflow: "hidden",
            backgroundColor: "#D9D9D9",
          }}
          className="box"
        >
          <Row>
            <Col className="col-7 p-0">
              <Card.Body className="p-2">
                <Card.Title
                  style={{
                    fontSize: "20px",
                    fontWeight: "bold",
                    float: left,
                    paddingBottom: "2%",
                  }}
                >
                  {" "}
                  {menu.name}
                </Card.Title>
                <Card.Text style={{ clear: left, fontSize: "16px" }}>
                  <div style={{ display: "flex" }}>
                    {menu.active ? (
                      <div
                        style={{
                          color: "#106719",
                        }}
                      >
                        Active
                      </div>
                    ) : (
                      <div
                        style={{
                          color: "#FE724C",
                        }}
                      >
                        Inactive
                      </div>
                    )}

                    <ButtonGroup
                      style={{
                        position: "absolute",
                        bottom: "5%",
                        right: "2%",
                      }}
                    >
                      <Button
                        // onClick={revert}
                        type="button"
                        className="mb-3"
                        style={{ width: "fit-content" }}
                      >
                        Edit <Edit fontSize="small"></Edit>
                      </Button>

                      <Button
                        type="button"
                        className="mb-3"
                        style={{
                          width: "fit-content",
                          backgroundColor: "#fe724c",
                          border: "#fe724c",
                        }}
                        onClick={handleDelete}
                      >
                        Delete <Delete fontSize="small"></Delete>
                      </Button>
                    </ButtonGroup>
                  </div>
                </Card.Text>
              </Card.Body>
            </Col>
          </Row>
        </Card>
      ) : (
        <></>
      )}
    </div>
  );
}

export default MenuCard;
