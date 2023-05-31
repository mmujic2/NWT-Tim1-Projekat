import React from "react";
import { Button, Form, Row, Col, ButtonGroup, Stack } from "react-bootstrap";
import { useState } from "react";
import { LocationOn } from "@mui/icons-material";
import CustomAlert from "../../shared/util/Alert";
import MapModal from "../../shared/MapModal/MapModal";
import Loader from "../../shared/util/Loader/Loader";
import { useEffect,useRef } from "react";
import restaurantService from "../../service/restaurant.service";
import defaultImage from "../../images/default.png";
import Multiselect from "multiselect-react-dropdown";
import { Figure } from "react-bootstrap";
import "./Multiselect.css";
import { Upload, East } from "@mui/icons-material";

function RestaurantInformation() {
  const [validated, setValidated] = useState();
  const [formData, setFormData] = useState();
  const [showMap, setShowMap] = useState(false);
  const [alert, setAlert] = useState({});
  const [showAlert, setShowAlert] = useState(false);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState();
  const inputRef = useRef(null);
  
  var mounted = false;
  useEffect(() => {
    if (!mounted) {
      mounted = true;
      restaurantService.getManagersRestaurant().then((res) => {
        setLoading(false);
        if (res.status == 200) {
          setFormData(res.data);
          console.log(res.data);
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
        restaurantService.getCategories().then((res) => {
          setLoading(false);
          if (res.status == 200)
            setCategories(res.data.map((c) => ({ key: c.name, cat: c.id })));
          else {
            setAlert({ ...alert, msg: res.data, type: "error" });
            setShowAlert(true);
          }
        });
      });
    }
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const setAddress = (addr, coordinates) => {
    setFormData({ ...formData, address: addr, mapCoordinates: coordinates });
  };

  const handleSubmit = (e) => {
    const form = e.currentTarget;
    e.preventDefault();
    e.stopPropagation();
    if (form.checkValidity() === false) {
      return;
    }

    setValidated(true);
  };

  const handleCategoryChange = (e) => {
    setFormData({
      ...formData,
      categories: e.map((c) => ({ name: c.key, id: c.cat })),
    });
  };

  const uploadLogo = ()=> {
    inputRef.current?.click();
  }

  const onFileChange = (e)=> {
    let files = e.target.files;
    let fileReader = new FileReader();
    fileReader.readAsDataURL(files[0]);

    fileReader.onload = (event) => {
        
        setFormData({...formData,logo:event.target.result})
    }
}

  return (
    <Loader isOpen={loading}>
      <MapModal
        show={showMap}
        setShow={setShowMap}
        setAddress={setAddress}
      ></MapModal>
      <CustomAlert
        setShow={setShowAlert}
        show={showAlert}
        type={alert.type}
        msg={alert.msg}
      ></CustomAlert>
      {formData ? (
        <Row>
          <Col className="col-8">
            <Form
              validated={validated}
              onSubmit={(e) => handleSubmit(e)}
              style={{ padding: "20px" }}
            >
              <Form.Group className="mb-3" controlId="formGroupFirstname">
                <Form.Label>Restaurant name</Form.Label>
                <Form.Control
                  required
                  type="text"
                  name="restaurantName"
                  value={formData.name}
                  onChange={handleChange}
                />
              </Form.Group>
              <Form.Group className="mb-3" controlId="formGroupAddress">
                <Form.Label>Address</Form.Label>
                <Row>
                  <Col xs={11}>
                    <Form.Control
                      readonly
                      disabled
                      required
                      type="text"
                      name="address"
                      value={formData.address}
                    />
                  </Col>
                  <Col xs={1} className="p-0">
                    <LocationOn
                      className="clickable"
                      onClick={() => setShowMap(true)}
                    ></LocationOn>
                  </Col>
                </Row>
              </Form.Group>
              <Form.Group>
                <Row className="mb-3" controlId="formGroupCategories">
                  <Form.Label>Categories</Form.Label>
                  <Multiselect
                    closeIcon="circle"
                    displayValue="key"
                    hidePlaceholder="true"
                    showArrow="true"
                    avoidHighlightFirstOption="true"
                    placeholder=""
                    options={categories}
                    selectedValues={formData.categories.map((c) => ({
                      key: c.name,
                      cat: c.id,
                    }))}
                    onSelect={handleCategoryChange}
                    onRemove={handleCategoryChange}
                    loading={categories ? false : true}
                    style={{
                      chips: {
                        background: "#272D2F",
                        border: { borderRadius: "0px" },
                        color: "white",
                      },
                      searchBox: { "border-radius": "5px", color: "black" },
                      icon: { color: "black" },
                      color: "black !important",
                    }}
                  />
                </Row>
              </Form.Group>

              <Button
                type="submit"
                className="mb-3"
                style={{ width: "fit-content", float: "right" }}
              >
                Submit changes
              </Button>
            </Form>
          </Col>
          <Col className="col-2">
            <Figure className="mt-5" style={{ width: "200px" }}>
              <Figure.Image
                style={{ height: "200px" }}
                src={
                  formData.logo
                    ? `${formData.logo} `
                    : defaultImage
                }
              />
              <Figure.Caption>Logo image</Figure.Caption>
            </Figure>
            <Stack gap={2} className="mb-3">
            <input ref={inputRef} className="d-none" type="file" onChange={onFileChange} accept="image/*"/>
            <Button style={{ width: "200px" }} onClick={uploadLogo}>Choose an image<Upload style={{marginLeft:"10px"}}/></Button>
            <Button style={{ width: "200px" }}>Image gallery<East style={{marginLeft:"10px"}}/></Button>
            </Stack>
          </Col>
        </Row>
      ) : (
        <></>
      )}
    </Loader>
  );
}

export default RestaurantInformation;
