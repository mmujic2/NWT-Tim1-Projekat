import {
  Add,
  KeyboardDoubleArrowDownRounded,
  KeyboardDoubleArrowUpRounded,
  Search,
} from "@mui/icons-material";
import { left } from "@popperjs/core";
import Multiselect from "multiselect-react-dropdown";
import React, { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import MenuItem from "../../../customer/Restaurant/MenuItem";
import RestaurantCard from "../../../customer/Restaurant/RestaurantCard";
import AddCoupon from "../../../restaurantManager/Coupons/AddCoupon";
import CouponCard from "../../../restaurantManager/Coupons/CouponCard";
import MenuCard from "../../../restaurantManager/Menus/MenuCard";
import authService from "../../../service/auth.service";
import restaurantService from "../../../service/restaurant.service";
import OrderCard from "../../Order/OrderCard";
import Loader from "../Loader/Loader";
import PaginationControl from "../Pagination/PaginationControl";
function ListContainer({
  title,
  showFilters,
  items,
  type = "restaurant",
  perPage = 4,
  categories = null,
  setItems,
  grid = true,
  alert,
  setAlert,
  showAlert,
  setShowAlert,
  setLoadingPage,
}) {
  const [page, setPage] = useState();
  const [currentPage, setCurrentPage] = useState([]);
  const [filterData, setFilterData] = useState({
    sortBy: "RATING",
    ascending: false,
  });
  const [loading, setLoading] = useState(false);
  const [openReportDialog, setOpenReportDialog] = useState(false);
  const [selectedValues, setSelectedValues] = useState([]);
  const user = authService.getCurrentUser();

  const navigate = useNavigate();

  useEffect(() => {
    goToPage(page);
  }, [page]);

  const goToPage = (p) => {
    setCurrentPage(items.slice((p - 1) * perPage, (p - 1) * perPage + perPage));
  };

  useEffect(() => {
    goToPage(1);
  }, [items]);

  const handleChange = (e) => {
    if (e.target.name == "offeringDiscount") {
      setFilterData({ ...filterData, [e.target.name]: e.target.checked });
      return;
    }
    setFilterData({ ...filterData, [e.target.name]: e.target.value });
  };

  const search = () => {
    setLoading(true);
    restaurantService.searchRestaurants(filterData).then((res) => {
      setLoading(false);
      if (res.status == 200) {
        setItems(res.data);
        console.log(res.data);
      }
    });
  };

  const handleCategoryChange = (e) => {
    let ids = e.map((c) => c.cat);
    setSelectedValues(e);
    setFilterData({ ...filterData, categoryIds: ids });
  };

  const filters = () => {
    const options = categories.map((c) => ({ key: c.name, cat: c.id }));

    return (
      <div style={{ display: "flex" }}>
        <div style={{ width: "25%", marginBottom: "10px" }}>
          <Multiselect
            options={options}
            closeIcon="circle"
            displayValue="key"
            placeholder="Categories"
            selectedValues={selectedValues}
            onSelect={handleCategoryChange}
            onRemove={handleCategoryChange}
            style={{
              chips: {
                background: "#fe724c",
                border: { borderRadius: "0px" },
                color: "white",
              },
              searchBox: {
                background: "#272D2F",
                color: "white",
                "border-radius": "5px",
              },
              option: { background: "#272D2F", color: "white" },
              optionContainer: { background: "#272D2F" },
              icon: { color: "black" },
              color: "white !important",
            }}
          />
        </div>

        <Form.Select
          aria-label="Sort by"
          style={{
            width: "18%",
            height: "40px",
            color: "white",
            marginLeft: "10px",
            backgroundColor: "#272D2F",
          }}
          value={filterData.sortBy}
          onChange={handleChange}
          name="sortBy"
        >
          <option value="RATING">Rating</option>
          <option value="POPULARITY">Popularity</option>
          <option value="DATE">Date</option>
        </Form.Select>
        {filterData.ascending ? (
          <KeyboardDoubleArrowDownRounded
            className="arrow-button"
            onClick={() => {
              setFilterData({ ...filterData, ascending: false });
            }}
          />
        ) : (
          <KeyboardDoubleArrowUpRounded
            className="arrow-button"
            onClick={() => {
              setFilterData({ ...filterData, ascending: true });
            }}
          />
        )}

        <Form.Check // prettier-ignore
          type="checkbox"
          label="Special offers"
          name="offeringDiscount"
          checked={filterData.offeringDiscount}
          onChange={handleChange}
          style={{ paddingTop: "5px", marginLeft: "10px", color: "#272D2F" }}
        />

        <Form.Control
          type="text"
          placeholder="Enter a restaurant name..."
          value={filterData.name}
          name="name"
          onChange={handleChange}
          style={{
            width: "32%",
            height: "40px",
            color: "white",
            marginLeft: "10px",
            backgroundColor: "#272D2F",
          }}
        />

        <Button
          style={{
            backgroundColor: "#fe724c",
            width: "70px",
            height: "40px",
            borderColor: "#fe724c",
          }}
          onClick={search}
        >
          <Search />
        </Button>
      </div>
    );
  };

  const createCoupon = () => {
    setOpenReportDialog(true);
  };

  return (
    <>
      <Loader isOpen={loading}>
        <Container
          style={{
            backgroundColor: "#F5F5F4",
            borderRadius: "5px",
            width: "100%",
            minWidth: "35rem",
            marginBottom: 0,
          }}
          className="container-fluid"
        >
          <h2 style={{ textAlign: "start", float: left }}>{title}</h2>
          {user.role == "ADMINISTRATOR" && type == "restaurant" ? (
            <Container
              style={{
                display: "flex",
                justifyContent: "flex-end",
                alignItems: "flex-end",
                backgroundColor: "#F5F5F4",
                height: "50px",
                marginBottom: 0,
                marginRight: 0,
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
                Add restaurant <Add></Add>
              </Button>
            </Container>
          ) : (
            <></>
          )}
          {user.role == "RESTAURANT_MANAGER" && type == "coupon" ? (
            <Container
              style={{
                display: "flex",
                justifyContent: "flex-end",
                alignItems: "flex-end",
                backgroundColor: "#F5F5F4",
                height: "50px",
                marginBottom: 0,
                marginRight: 0,
              }}
            >
              <Button
                onClick={createCoupon}
                style={{
                  clear: left,
                  textAlign: "center",
                  width: "fit-content",
                  height: "40px",
                }}
                class="rounded"
              >
                Add coupon <Add></Add>
              </Button>
              <AddCoupon
                open={openReportDialog}
                setOpen={setOpenReportDialog}
                ticket={user}
              ></AddCoupon>
            </Container>
          ) : (
            <></>
          )}
          {user.role == "RESTAURANT_MANAGER" && type == "menus" ? (
            <Container
              style={{
                display: "flex",
                justifyContent: "flex-end",
                alignItems: "flex-end",
                backgroundColor: "#F5F5F4",
                height: "50px",
                marginBottom: 0,
                marginRight: 0,
              }}
            >
              <Button
                onClick={() => navigate("/menu/add")}
                style={{
                  clear: left,
                  textAlign: "center",
                  width: "fit-content",
                  height: "40px",
                }}
                class="rounded"
              >
                Add a menu <Add></Add>
              </Button>
            </Container>
          ) : (
            <></>
          )}

          <hr style={{ clear: left }}></hr>
          {showFilters && categories ? filters() : <></>}
          <Row xs={1} md={grid ? 2 : 1} className="gy-2 gx-2 mw-100">
            {items.length > 0 && !loading ? (
              currentPage.map((i) => (
                <Col key={i.id}>
                  {type == "restaurant" ? (
                    <RestaurantCard
                      grid={grid}
                      style={{ width: "100%" }}
                      res={i}
                    />
                  ) : type == "order" ? (
                    <OrderCard
                      grid={grid}
                      style={{ width: "100%" }}
                      order={i}
                    />
                  ) : type == "coupon" ? (
                    <CouponCard grid={grid} style={{ width: "100%" }} res={i} />
                  ) : type == "menu" ? (
                    <MenuItem
                      grid={grid}
                      style={{ width: "100%" }}
                      menuItem={i}
                    />
                  ) : type == "menus" ? (
                    <MenuCard
                      grid={grid}
                      style={{ width: "100%" }}
                      menu={i}
                      menus={items}
                      setMenus={setItems}
                      alert={alert}
                      setAlert={setAlert}
                      showAlert={showAlert}
                      setShowAlert={setShowAlert}
                      setLoading={setLoadingPage}
                    />
                  ) : type == "menuItems" ? (
                    <MenuItem
                      grid={grid}
                      style={{ width: "100%" }}
                      menuItem={i}
                    />
                  ) : (
                    <></>
                  )}
                </Col>
              ))
            ) : (
              <span
                style={{
                  color: "grey",
                  textAlign: "center",
                  width: "100%",
                  height: "100%",
                  verticalAlign: "middle",
                }}
              >
                No results to show
              </span>
            )}
          </Row>

          {items.length > perPage ? (
            <div>
              <hr></hr>
              <PaginationControl
                page={page}
                setPage={setPage}
                total={items.length}
                limit={perPage}
              />
            </div>
          ) : (
            <></>
          )}
        </Container>
      </Loader>
    </>
  );
}

export default ListContainer;
