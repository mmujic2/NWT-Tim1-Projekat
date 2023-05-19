import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';
import { Nav } from 'react-bootstrap';
import { Restaurant } from '@mui/icons-material';
import authService from '../../service/auth.service';
import { useNavigate } from 'react-router-dom';

function Header() {

    const user = authService.getCurrentUser();
    const navigate = useNavigate();

    const customerOptions = () => {
        const logout = () => {
            document.body.style.cursor = "wait";
            authService.logout().then(res => {
                document.body.style.cursor = "default";
                if(res.status==200) {
                    
                    navigate("/");
                }
            })
        }

        const customerInfoPage = () => {
            navigate("/customer/details")
        }

        const restaurants = () => {
            navigate("/")
        }

        return (
            <>
               
                <Nav className="me-auto">
                </Nav>
                <Nav>
                    <Nav.Link className='text-white' onClick={restaurants}>Restaurants</Nav.Link>
                    <div className="vr text-white"></div>
                    <Nav.Link className='text-white' onClick={customerInfoPage}>{user.username}</Nav.Link> 
                    <div className="vr text-white"></div>
                    <Nav.Link className="text-white" onClick={logout}>Logout</Nav.Link>
                </Nav>
                
            </>
        )
    }

    const restaurantOptions = () => {
        
        const logout = () => {
            document.body.style.cursor = "wait";
            authService.logout().then(res => {
                document.body.style.cursor = "default";
                if(res.status==200) {
                    
                    navigate("/");
                }
            })
        }

        const restaurantInfoPage = () => {
            navigate("/restaurant/details")
        }

        const orders = () => {
            navigate("/")
        }

        return (
            <>
                <Nav className="me-auto">
                </Nav>
                <Nav>
                    <Nav.Link className='text-white' onClick={restaurantInfoPage}>My restaurant</Nav.Link> 
                    <div className="vr text-white"></div>
                    <Nav.Link className='text-white' onClick={orders}>Orders</Nav.Link>
                    <div className="vr text-white"></div>
                    <Nav.Link className="text-white" onClick={logout}>Logout</Nav.Link>
                </Nav>
                
            </>
        )
    }

    const administratorOptions = () => {
        const logout = () => {
            document.body.style.cursor = "wait";
            authService.logout().then(res => {
                document.body.style.cursor = "default";
                if(res.status==200) {
                    
                    navigate("/");
                }
            })
        }

        const restaurants = () => {
            navigate("/admin/restaurants")
        }

        const scores = () => {
            navigate("/admin/scores")
        }

        const couriers = () => {
            navigate("/admin/couriers")
        }

        return (
            <>
                <Nav className="me-auto">
                </Nav>
                <Nav>
                    <Nav.Link className='text-white' onClick={restaurants}>Restaurants</Nav.Link> 
                    <div className="vr text-white"></div>
                    <Nav.Link className='text-white' onClick={scores}>Scores</Nav.Link>
                    <div className="vr text-white"></div>
                    <Nav.Link className='text-white' onClick={couriers}>Couriers</Nav.Link>
                    <div className="vr text-white"></div>
                    <Nav.Link className="text-white" onClick={logout}>Logout</Nav.Link>
                </Nav>
                
            </>
        )
    }

    const courierOptions = () => {
        const logout = () => {
            document.body.style.cursor = "wait";
            authService.logout().then(res => {
                document.body.style.cursor = "default";
                if(res.status==200) {
                    
                    navigate("/");
                }
            })
        }

        const courierInfoPage = () => {
            navigate("/courier/details")
        }

       
        return (
            <>
                <Nav className="me-auto">
                </Nav>
                <Nav>
                    <Nav.Link className='text-white' onClick={courierInfoPage}>My profile</Nav.Link> 
                    <div className="vr text-white"></div>
                    <Nav.Link className="text-white" onClick={logout}>Logout</Nav.Link>
                </Nav>
                
            </>
        )
    }



    return (
        <>
            <Navbar bg="dark" variant="dark" className='p-2' sticky='top' style={{zIndex:1000}}>
                <Nav className="container-fluid">
                    <Nav.Item>
                        <Navbar.Brand href="#">
                            <Restaurant></Restaurant>{' '}
                            The Convenient Foodie
                        </Navbar.Brand>
                    </Nav.Item>

                    {user.role == "CUSTOMER" ? customerOptions() 
                    : user.role == "ADMINISTRATOR" ? administratorOptions() 
                    : user.role == "RESTAURANT_MANAGER" ? restaurantOptions() 
                    : courierOptions()}

                </Nav>
            </Navbar>
        </>
    );
}

export default Header;