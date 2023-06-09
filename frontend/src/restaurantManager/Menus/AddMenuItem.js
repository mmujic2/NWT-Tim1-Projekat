import { Upload } from "@mui/icons-material";
import Button from "@mui/material/Button";
import Checkbox from "@mui/material/Checkbox";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogTitle from "@mui/material/DialogTitle";
import FormControlLabel from "@mui/material/FormControlLabel";
import TextField from "@mui/material/TextField";
import * as React from "react";
import { useRef, useState } from "react";
import { Figure } from "react-bootstrap";
import defaultImage from "../../images/default.png";
//import { Button, Form, Row, Col, ButtonGroup, Stack } from "react-bootstrap";
import menuService from "../../service/menu.service";

export default function AddMenuItem({
  open,
  setOpen,
  menuItems,
  setMenuItems,
  item,
  menuId,
  showAlert,
  setShowAlert,
  alert,
  setAlert,
}) {
  const [menuItem, setMenuItem] = useState({
    name: item?.name || "",
    description: item?.description || "",
    price: item?.price || 0,
    discount_price: item?.discount_price || 0,
    prep_time: item?.prep_time || null,
    image: item?.image || null,
  });

  const [discount, setDiscount] = useState(false);
  const [validation, setValidation] = useState({
    name: true,
    description: true,
    price: true,
    discount_price: true,
    prep_time: true,
    image: true,
  });
  const [valid, setValid] = useState(true);
  const inputRef = useRef(null);

  const handleClose = () => {
    setMenuItem({
      name: "",
      description: "",
      price: 0,
      discount_price: 0,
      prep_time: null,
      image: null,
    });
    setValidation({
      name: true,
      description: true,
      price: true,
      discount_price: true,
      prep_time: true,
      image: true,
    });
    setValid(true);
    setOpen(false);
  };

  const handleChange = (e) => {
    setMenuItem({ ...menuItem, [e.target.name]: e.target.value });
  };

  const checkValidation = (i) => {
    let isValid = true;
    let updatedValidation = { ...validation };

    if (
      i.name == null ||
      i.name.length === 0 ||
      i.name.length < 2 ||
      i.name.length > 30
    ) {
      isValid = false;
      updatedValidation.name = false;
    } else {
      updatedValidation.name = true;
    }

    if (i.description != null && i.description.length > 100) {
      isValid = false;
      updatedValidation.description = false;
    } else {
      updatedValidation.description = true;
    }

    if (i.price == null || i.price < 0) {
      isValid = false;
      updatedValidation.price = false;
    } else {
      updatedValidation.price = true;
    }

    if (i.prep_time != null && i.prep_time < 0) {
      isValid = false;
      updatedValidation.prep_time = false;
    } else {
      updatedValidation.prep_time = true;
    }

    if (i.discount_price != null && i.discount_price < 0) {
      isValid = false;
      updatedValidation.discount_price = false;
      updatedValidation.discount_price_error_m =
        "Discount price cannot be negative";
    } else if (
      i.discount_price != null &&
      i.price != null &&
      i.discount_price >= i.price
    ) {
      isValid = false;
      updatedValidation.discount_price = false;
      updatedValidation.discount_price_error_m =
        "Discounted price should not be higher than the regular price!";
    } else {
      updatedValidation.discount_price = true;
    }

    setValidation(updatedValidation);
    console.log(updatedValidation);
    return { isValid };
  };

  const handleCreate = () => {
    const { isValid } = checkValidation(menuItem);
    console.log(isValid);
    if (isValid) {
      console.log(menuItems);
      setMenuItems((current) => [...current, menuItem]);
      setMenuItem({
        name: "",
        description: "",
        price: 0,
        discount_price: 0,
        prep_time: null,
        image: null,
      });
      document.body.style.cursor = "wait";
      menuService.setMenuItems([menuItem], menuId).then((res) => {
        if (res.status == 200) {
          document.body.style.cursor = "default";
          setAlert({
            ...alert,
            msg: "Successfully added menu-item!",
            type: "success",
          });
          setShowAlert(true);
        } else {
          setAlert({ ...alert, msg: res.data, type: "error" });
          setShowAlert(true);
        }
      });

      setOpen(false);
    }
  };

  const uploadImage = () => {
    inputRef.current?.click();
  };

  const onFileChange = (e) => {
    let files = e.target.files;
    let fileReader = new FileReader();
    fileReader.readAsDataURL(files[0]);

    fileReader.onload = (event) => {
      setMenuItem({ ...menuItem, image: event.target.result });
    };
  };

  return (
    <div>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add a menu item</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="normal"
            name="name"
            id="name"
            label="Name"
            type="text"
            fullWidth
            variant="standard"
            required
            error={!validation.name}
            helperText={
              !validation.name
                ? "Menu item name must be between 2 and 30 characters!"
                : ""
            }
            onChange={handleChange}
          />
          <TextField
            margin="normal"
            name="description"
            id="description"
            label="Description"
            type="text"
            fullWidth
            multiline
            rows={4}
            helperText={
              !validation.description
                ? "Menu item description can contain a maximum of 100 characters!"
                : ""
            }
            error={!validation.description}
            onChange={handleChange}
          />
          <br></br>
          <br></br>
          <TextField
            type="number"
            label="Original price"
            variant="standard"
            id="discount"
            name="price"
            value={menuItem?.price}
            required
            error={!validation.price}
            helperText={
              !validation.price ? "Menu item price should not be null!" : ""
            }
            onChange={(event) => {
              const inputValue = event.target.value;
              const formattedValue = Number(parseFloat(inputValue).toFixed(2));
              setMenuItem({
                ...menuItem,
                price: formattedValue,
              });
            }}
          />

          <TextField
            type="number"
            label="Cooking time"
            variant="standard"
            id="prep_time"
            name="prep_time"
            error={!validation.prep_time}
            helperText={
              !validation.prep_time ? "Cooking time can not be negative!" : ""
            }
            value={menuItem?.prep_time || 0}
            onChange={(event) => {
              const inputValue = event.target.value;
              const formattedValue = Number(parseFloat(inputValue).toFixed(2));
              setMenuItem({
                ...menuItem,
                prep_time: formattedValue,
              });
            }}
            style={{ marginLeft: "140px" }}
          />
          <div style={{ float: "right", marginTop: "25px" }}>min</div>
          <br></br>
          <br></br>

          <FormControlLabel
            control={
              <Checkbox
                defaultChecked={discount}
                onChange={() => setDiscount(!discount)}
              />
            }
            label="Discounted price"
          />
          <br></br>

          <TextField
            type="number"
            variant="standard"
            id="discount"
            label="Discounted price"
            name="discount_price"
            disabled={!discount}
            error={!validation.discount_price}
            helperText={
              !validation.discount_price
                ? validation.discount_price_error_m
                : ""
            }
            value={menuItem?.discount_price}
            onChange={(event) => {
              const inputValue = event.target.value;
              const formattedValue = Number(parseFloat(inputValue).toFixed(2));
              setMenuItem({
                ...menuItem,
                discount_price: formattedValue,
              });
            }}
          />
          <br></br>
          <br></br>
          <input
            ref={inputRef}
            className="d-none"
            type="file"
            onChange={onFileChange}
            accept="image/*"
          />
          <Button
            style={{ width: "200px", background: "#606060", color: "black" }}
            onClick={uploadImage}
          >
            Choose an image
            <Upload style={{ marginLeft: "10px" }} />
          </Button>
          <br></br>

          <Figure className="mt-1 ms-4" style={{ width: "150px" }}>
            <Figure.Image
              style={{ height: "150px" }}
              src={menuItem.image ? `${menuItem.image} ` : defaultImage}
            />
          </Figure>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleCreate}>Create</Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}
