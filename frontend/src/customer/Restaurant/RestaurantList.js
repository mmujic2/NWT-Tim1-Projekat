import React, { useEffect } from 'react'
import { Container, Row, Col, Button } from 'react-bootstrap'
import RestaurantCard from './RestaurantCard'
import PaginationControl from '../../shared/util/PaginationControl'
import { useState } from 'react'
import Multiselect from 'multiselect-react-dropdown'
import { Form } from 'react-bootstrap'
import './RestaurantList.css'
import { Api, Search, Add } from '@mui/icons-material'
import { ArrowDown, ArrowUp } from 'react-bootstrap-icons'
import { Spinner } from 'react-bootstrap'
import restaurantService from '../../service/restaurant.service'
import Loader from '../../shared/util/Loader/Loader'
import authService from '../../service/auth.service'
import { right, left } from "@popperjs/core";
import { KeyboardDoubleArrowUpRounded,KeyboardDoubleArrowDownRounded } from '@mui/icons-material'


function RestaurantList({ title, showFilters, restaurants, perPage = 4, categories = null, setRestaurants }) {
    const [page, setPage] = useState()
    const [currentPage, setCurrentPage] = useState([])
    const [filterData, setFilterData] = useState({ sortBy: "RATING", ascending: false })
    const [loading, setLoading] = useState(false)
    const user = authService.getCurrentUser();


    useEffect(() => {
        goToPage(page)
    }, [page])

    const goToPage = (p) => {
        setCurrentPage(
            restaurants.slice(
                (p - 1) * perPage,
                (p - 1) * perPage + perPage
            )
        );

    }

    useEffect(() => {
        goToPage(1);
    }, [restaurants]);

    const handleChange = (e) => {
        if (e.target.name == "offeringDiscount") {
            setFilterData({ ...filterData, [e.target.name]: e.target.checked })
            return
        }
        setFilterData({ ...filterData, [e.target.name]: e.target.value })

        console.log(e.target)
    }

    const search = () => {
        setLoading(true)
        restaurantService.searchRestaurants(filterData).then((res) => {
            setLoading(false)
            if (res.status == 200) {
                setRestaurants(res.data)
                console.log(res.data)
            }
        })
    }

    const handleCategoryChange = (e) => {
        let ids = e.map(c => c.cat)
        setSelectedValues(e)
        setFilterData({ ...filterData, categoryIds: ids })
    }


    const filters = () => {
        const options = categories.map(c => ({ 'key': c.name, 'cat': c.id }))

        return (
            <div style={{ display: "flex" }}>
                <div style={{ width: "25%", marginBottom: "10px" }}>
                    <Multiselect
                        options={options}
                        closeIcon="circle"
                        displayValue="key"
                        placeholder='Categories'
                        selectedValues={selectedValues}
                        onSelect={handleCategoryChange}
                        onRemove={handleCategoryChange}
                        style={{
                            chips: { background: "#fe724c", border: { borderRadius: "0px" }, color: "white" },
                            searchBox: { background: "#272D2F", color: "white", "border-radius": "5px" },
                            option: { background: "#272D2F", color: "white" },
                            optionContainer: { background: "#272D2F" },
                            icon: { color: "black" }, color: "white !important"
                        }}
                    />
                </div>


                <Form.Select aria-label="Sort by" style={{ width: "18%", height: "40px", color: "white", marginLeft: "10px", backgroundColor: "#272D2F" }}
                    value={filterData.sortBy} onChange={handleChange} name="sortBy" >
                    <option value="RATING">Rating</option>
                    <option value="POPULARITY">Popularity</option>
                    <option value="DATE">Date</option>
                </Form.Select>
                {filterData.ascending ? <KeyboardDoubleArrowDownRounded className='arrow-button' onClick={() => { setFilterData({ ...filterData, ascending: false }) }} /> : <KeyboardDoubleArrowUpRounded  className='arrow-button' onClick={() => { setFilterData({ ...filterData, ascending: true }) }} />}

                <Form.Check // prettier-ignore
                    type="checkbox"
                    label="Special offers"
                    name="offeringDiscount"
                    checked={filterData.offeringDiscount}
                    onChange={handleChange}
                    style={{ paddingTop: "5px", marginLeft: "10px", color: "#272D2F" }} />

                <Form.Control
                    type="text"
                    placeholder='Enter a restaurant name...'
                    value={filterData.name}
                    name="name"
                    onChange={handleChange}
                    style={{
                        width: "32%", height: "40px", color: "white", marginLeft: "10px", backgroundColor: "#272D2F"
                    }}
                />

                <Button style={{ backgroundColor: "#fe724c", width: "70px", height: "40px", borderColor: "#fe724c" }} onClick={search}><Search /></Button>


            </div>
        )
    }

    const [selectedValues, setSelectedValues] = useState([

    ])

    //user.role=="CUSTOMER" ? <Restaurants></Restaurants>: <></>
    

    return (
        <>
            <Loader isOpen={loading} >
            <Container style={{ backgroundColor: "#F5F5F4", borderRadius: "5px", width: "100%", minWidth: "35rem", marginBottom: 0, }} className="container-fluid">
                <h2 style={{ textAlign: "start", float: left  }}>{title}</h2>
                {user.role=="ADMINISTRATOR" ? 
                <Container style={{display:"flex",justifyContent:"flex-end",alignItems:"flex-end", backgroundColor: "#F5F5F4", height:"50px", marginBottom: 0,marginRight: 0,}}>
                                <Button style={{ clear: left, textAlign: "center",width: "250px", height: "40px",}} class="rounded">Add restaurant <Add ></Add></Button>
                </Container>
                : <></>}
                <hr style={{ clear: left }}></hr>
                {showFilters && categories ? filters() : <></>}
                <Row xs={1} md={2} className="gy-2 gx-2 mw-100" >
                    {restaurants.length > 0 && !loading ?
                        currentPage.map((r) =>
                            <Col key={r.id}>
                                <RestaurantCard res={r} className="box" />
                            </Col>
                        )

                        : <span style={{ color: "grey", textAlign: "center",left:"25%",top: "30%",position:"relative" }}>No results to show</span>

                    }
                </Row>

                {restaurants.length > perPage ?
                    <div >
                        <hr></hr>
                        <PaginationControl page={page} setPage={setPage} total={restaurants.length} limit={perPage} />
                    </div> : <></>}

            </Container>
            </Loader>
        </>
    )
}

export default RestaurantList