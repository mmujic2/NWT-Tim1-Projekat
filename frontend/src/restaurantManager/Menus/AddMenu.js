import { Add } from "@mui/icons-material";
import { left } from "@popperjs/core";
import { React, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import ListContainer from "../../shared/util/ListContainer/ListContainer";
function AddMenu() {
  const [formData, setFormData] = useState({});
  const [active, setActive] = useState(false);
  const [menuItems, setMenuItems] = useState([]);

  const handleSubmit = (e) => {
    const form = e.currentTarget;
    e.preventDefault();
    e.stopPropagation();
    console.log(formData);
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };
  return (
    <Row>
      <Col>
        <Form
          //validated={validated}
          // onSubmit={(e) => handleSubmit(e)}
          style={{ padding: "20px" }}
        >
          <Form.Group className="mb-1" controlId="formGroupActive">
            <Form.Check
              data-toggle="toggle"
              style={{
                marginTop: "40px",
                float: "right",
                marginRight: "20%",
              }}
              label="Active"
              name="active"
              type={"checkbox"}
              checked={active}
              onChange={(e) => {
                setActive(!active);
                setFormData({ ...formData, active: !active });
              }}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="formGroupFirstname">
            <Form.Label>Name</Form.Label>
            <Form.Control
              required
              type="text"
              name="name"
              value={formData?.name}
              onChange={handleChange}
              style={{ width: "60%" }}
            />
          </Form.Group>
          <h2
            style={{
              textAlign: "start",
              float: left,
              marginTop: "10px",
              marginLeft: "16px",
            }}
          >
            Menu items
          </h2>

          <div
            style={{
              float: "right",
              marginTop: "10px",
              marginRight: "16px",
            }}
          >
            <Button
              style={{
                clear: left,
                textAlign: "center",
                width: "fit-content",
                height: "40px",
              }}
              class="rounded"
            >
              Add a menu item <Add></Add>
            </Button>
          </div>

          <ListContainer
            items={menuItems}
            setItems={setMenuItems}
            showFilters={false}
            perPage={5}
            type={"menuItems"}
            grid={false}
          ></ListContainer>
          <Button
            type="submit"
            className="mb-3"
            style={{
              width: "fit-content",
              backgroundColor: "#fe724c",
              border: "#fe724c",
            }}
            onClick={handleSubmit}
          >
            Submit
          </Button>
        </Form>
      </Col>
    </Row>
  );
}

export default AddMenu;
