import React, { useEffect } from 'react'
import { Container,Row,Col } from 'react-bootstrap'
import RestaurantCard from './RestaurantCard'
import PaginationControl from '../../shared/util/PaginationControl'
import { useState } from 'react'


function RestaurantList({restaurants,perPage=4}) {
  const [page,setPage] = useState()
  const [currentPage,setCurrentPage] = useState([])
  const [totalPages,setTotalPages] = useState()

    useEffect(()=>{
        goToPage(page)
    },[page])

    const goToPage = (p)=> {
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


  return (
    <Container style={{backgroundColor:"white",borderRadius:"5px",width:"90%",minWidth:"35rem"}} className="container-fluid">
        <h2 style={{textAlign:"start"}}>All Restaurants</h2>
        <hr></hr>
        <Row xs={1} md={2} className="gy-2 gx-2 mw-100" >
        {
            currentPage.map((r) => 
            <Col key={r.id}>
            <RestaurantCard res={r}/>
            </Col>
            )
        }
        </Row>
        
        {restaurants.length>perPage ? 
        <>
        <hr></hr>
        <PaginationControl page={page} setPage={setPage} total={restaurants.length} limit={perPage}/>
        </> :<></>}
        
    </Container>
  )
}

export default RestaurantList