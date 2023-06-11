import { Button, Container, Form, Row } from "react-bootstrap"
import React, { useEffect, useState } from 'react'
import { Chart } from "react-google-charts";
import orderService from "../../service/order.service";
import Loader from "../../shared/util/Loader/Loader";
import discountService from "../../service/discount.service";



function AdminOverview() {
    var mounted = false;
    const [searchResults, setSearchResults] = useState([]);
    const [revenueResults, setrevenueResults] = useState([]);
    const [totalSpending, settotalSpending] = useState();
    const [requiredScore, setrequiredScore] = useState({orders_required:0,money_required:0});
    const [loading, setLoading] = useState(false);
    

    useEffect(() => {
        if (!mounted) {
            mounted = true;
            setLoading(true)
            discountService.getRequiredScore().then(res => setrequiredScore(res.data))
            orderService.getAdminOrders().then(res => {
                if (res.status == 200) {
                    
                    var list = [["Restaurant", "Number of orders"]]
                    
                    Object.entries(res.data).forEach(item => {
                        list = [...list,item]
                    })
                    setSearchResults(list);
                    setLoading(false)
                }
                else
                    console.log(res)

            })
            orderService.getAdminRestaurantRevenue().then(res => {
                if (res.status == 200) {
                    
                    var list = [["Restaurant", "Revenue"]]
                    
                    Object.entries(res.data).forEach(item => {
                        list = [...list,item]
                    })
                    setrevenueResults(list);
                    setLoading(false)
                }
                else
                    console.log(res)

            })
            orderService.getAdminSpending().then(res => {
                if (res.status == 200) {
                    settotalSpending(res.data)
                    setLoading(false)
                }
                else
                    settotalSpending(-1)

            })
            //var reqscore ={orders_required:50,money_required:100}
            
        }
    }, [])

    const updateRequiredScore=() =>{
        setLoading(true);
        discountService.updateRequiredScore(requiredScore).then(res => {
            console.log(res.data)
            setLoading(false);
        })
        
    }
    
    const options = {
        title: "Number of Orders",
    };
    const options2 = {
        title: "Restaurant revenue (KM)",
    };

    
    return (
        <>
        <Loader isOpen={loading} >
            <Container style={{ backgroundColor: "#D9D9D9",  margin: "auto", marginTop: "20px", marginBottom: "20px", width:"100%"}}>
                <h1>Admin overview</h1>
                <hr/>
                <h3>Amount spent to date: {totalSpending} KM</h3>
                <hr/>
                <Chart
                chartType="PieChart"
                data={searchResults}
                options={options}
                width={"100%"}
                height={"400px"}
                />
                <hr/>
                <Chart
                chartType="PieChart"
                data={revenueResults}
                options={options2}
                width={"100%"}
                height={"400px"}
                />
            <hr/>
            <h3>Required score:</h3>
            <hr/>
            <Form>
                <Row>
                <Form.Group className="mb-3">
                    <Form.Label>Money required:</Form.Label>
                    <Form.Control
                    type="text"
                    autoFocus
                    defaultValue={requiredScore.money_required}
                    margin="normal"
                    id="money"
                    label="Money required"
                    fullWidth
                    variant="standard"
                    onChange={(e) => {
                        setrequiredScore((current) => ({
                            ...current,
                            ...{ money_required: e.target.value },
                          }));
                    }}
                    />
                </Form.Group>
                </Row>
                <Row>
                <Form.Group className="mb-3">
                    <Form.Label>Orders required:</Form.Label>
                    <Form.Control
                    type="text"
                    defaultValue={requiredScore.orders_required}
                    autoFocus
                    margin="normal"
                    id="orders"
                    label="Orders required"
                    fullWidth
                    variant="standard"
                    onChange={(e) => {
                        setrequiredScore((current) => ({
                            ...current,
                            ...{ orders_required: e.target.value },
                          }));
                    }}
                    />
                </Form.Group>
                </Row>
                <Row>
                    <Button onClick={updateRequiredScore}>Update required score</Button>
                </Row>
            </Form>

                
            </Container>
        </Loader>
        </>
        
    )
    
}

export default AdminOverview