import React, { useEffect, useState } from 'react'
import restaurantService from '../../service/restaurant.service';
import ListContainer from '../../shared/util/ListContainer/ListContainer';
import { Spinner, Container ,Button } from 'react-bootstrap';
import Loader from '../../shared/util/Loader/Loader';
import Header from '../../shared/util/Header';
import BootstrapTable from "react-bootstrap-table-next";
import paginationFactory from "react-bootstrap-table2-paginator";
//import "bootstrap/dist/css/bootstrap.css";
import "react-bootstrap-table-next/dist/react-bootstrap-table2.min.css";
import { Search, Add } from '@mui/icons-material'
import authService from '../../service/auth.service';
import { right, left } from "@popperjs/core";
import userService from '../../service/user.service';


function AdminRestaurants() {
    var mounted = false;
    const [searchResults, setSearchResults] = useState();
    const [loading, setLoading] = useState(false);
    const [categories, setCategories] = useState();
    const user = authService.getCurrentUser();

    useEffect(() => {
        if (!mounted) {

            mounted = true;
            setLoading(true) // ... vrati ovo na true
            restaurantService.getAllFullRestaurants().then(res => {
                console.log(res.data)
                if (res.status == 200) {
                    res.data.forEach(element => {
                        var cat = element.categories
                        element.categories = ""
                        cat.forEach(c => {
                            element.categories += c.name
                            element.categories += ", "
                        })
                        element.categories = element.categories.substring(0,element.categories.length-2)
                        //console.log(element.categories)
                    });
                    userService.getAllManagers().then(mng => {
                        //setLoading(false)
                        if (mng.status == 200){
                            mng.data.forEach(el => {
                                var uuid = el.uuid
                                var managerName = el.firstname +" " +el.lastname
                                res.data.forEach(element => {
                                    if(element.managerUuid == uuid){
                                        element.manager = managerName
                                    } 
                                });
                            });
                            setSearchResults(res.data)
                            //console.log(res.data)
                            setLoading(false)
                        }
                    })
                    

                    
                    

                }
                else
                    console.log(res)

            })


        }
    }, [])

    const addNewRestaurant = () => {
        console.log("Posalji me na stranicu");
    }

    const columns = [
        {
          dataField: "manager",
          text: "Restaurant manager",
          sort: true
        },
        {
          dataField: "name",
          text: "Restaurant Name",
          sort: true
        },
        {
          dataField: "categories",
          text: "Category"
        },
        {
            dataField: "address",
            text: "Address"
        },
        {
            dataField: "rating",
            text: "Rating",
            sort: true
        },
        {
            dataField: "customersFavorited",
            text: "Favourited"
        },
      ];

      const rowEvents = {
        onClick: (e, row, rowIndex) => {
          console.log(row) // ovo je objekat u tom redu
        }
      };

// <ListContainer items={searchResults} title={"All restaurants"} showFilters={true} perPage={8} categories={categories} setRestaurants={setSearchResults}></ListContainer>


    return (
        <>
            <Loader isOpen={loading} >
                {searchResults ?
                    <Container style={{ backgroundColor: "#D9D9D9",  margin: "auto", marginTop: "20px", marginBottom: "20px", width:"100%"}}>
                        <Container style={{ backgroundColor: "#F5F5F4", borderRadius: "5px",borderBottomRightRadius:0,borderBottomLeftRadius:0, width: "100%", minWidth: "35rem", marginBottom: 0, }}>
                            <h2 style={{ textAlign: "start", float: left, margin:0 }}>{"All restaurants"}</h2>
                            <Container style={{ display: "flex", justifyContent: "flex-end", alignItems: "flex-end", backgroundColor: "#F5F5F4", marginBottom: 0, marginRight: 0,padding:0, paddingBottom:10 }}>
                                <Button style={{ clear: left, textAlign: "center", width: "fit-content",  }} class="rounded" onClick={addNewRestaurant}>Add restaurant <Add ></Add></Button>
                            </Container>
                                <hr style={{ clear: left, margin:0 }}></hr>
                        </Container>
                        <BootstrapTable
                            bootstrap4
                            striped 
                            hover 
                            rowEvents={ rowEvents }
                            keyField="id"
                            data={searchResults}
                            columns={columns}
                            pagination={paginationFactory({ sizePerPage: 5 })}
                        />
                        
                    </Container> : <></>}
            </Loader>
        </>
    )
}

export default AdminRestaurants